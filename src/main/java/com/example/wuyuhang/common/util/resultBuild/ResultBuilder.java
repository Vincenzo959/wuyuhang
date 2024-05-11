package com.example.wuyuhang.common.util.resultBuild;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wuyuhang.common.annotation.ResultUtil;
import com.example.wuyuhang.common.util.ListUtil;
import com.example.wuyuhang.modules.api.util.ConvertUtils;
import com.example.wuyuhang.modules.api.util.IException;
import com.example.wuyuhang.modules.api.util.Result;
import com.example.wuyuhang.modules.api.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.StringContent;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 卓佳伟
 * @date 2024/3/20 17:20
 * description
 */
@Slf4j
public class ResultBuilder {

    /**
     * @Description //TODO
     * @Date 2023/3/5
     * @params [list:粗数据集合, resultList:返回值数据集合, resultClass:返回值集合对应的泛型]
     * @return void
     **/
    public static<T,R> void buildResult(List<T> list,List<R> resultList,Class<R> resultClass)  {
        //键为返回值实体类需要组装的属性名称,值为辅表对象构建后的Map集合
        Map<String,Map> resultBuildMap=new HashMap<>();

        buildResultBuildMap(list,resultClass,resultBuildMap);

        assembleResultList(list,resultList,resultClass,resultBuildMap);
    }

    private static <T,R> void buildResultBuildMap(List<T> list,
                                                  Class<R> resultClass,
                                                  Map<String,Map> resultBuildMap){
        //获取返回值对象中的所有字段
        Field[] declaredFields = resultClass.getDeclaredFields();

        //遍历返回值对象的每个字段
        for (Field declaredField : declaredFields) {

            //获取字段上的ResultUtil注解
            ResultUtil annotation = declaredField.getAnnotation(ResultUtil.class);
            if(Objects.nonNull(annotation)){

                //获取当前注解字段使用的service或mapper
                String resultListEntityServiceName = annotation.resultListUsedServiceName();
                String resultListEntityMapperName = annotation.resultListUsedMapperName();
                BaseMapper baseMapper=null;
                IService iService = null;
                if(Objects.nonNull(resultListEntityMapperName)&&
                        !Objects.equals(resultListEntityMapperName.replaceAll(" ",""),"")){
                    baseMapper = (BaseMapper) SpringContextUtils.getBean(resultListEntityMapperName);
                }else if(Objects.nonNull(resultListEntityServiceName)&&
                        !Objects.equals(resultListEntityServiceName.replaceAll(" ",""),"")){
                    iService = (IService) SpringContextUtils.getBean(resultListEntityServiceName);
                }

                Set<Serializable> cols=new HashSet<>();

                //遍历粗数据
                for (T item : list) {

                    //获取粗数据需要加工的字段并且合并为Set集合
                    String listEntityPropName = annotation.listEntityPropName();
                    Class itemClass = item.getClass();
                    String getMethodNameByField = getGetMethodNameByField(listEntityPropName);
                    try {
                        Method method = itemClass.getMethod(getMethodNameByField);
                        if(Objects.nonNull(method)){
                            Serializable col = (Serializable) method.invoke(item);
                            cols.add(col);
                        }
                    } catch (NoSuchMethodException e) {
                        throw new IException("构建返回值失败,未找到字段:"+listEntityPropName);
                    } catch (IllegalAccessException e) {
                        throw new IException("构建返回值失败,未找到字段:"+listEntityPropName);
                    } catch (InvocationTargetException e) {
                        throw new IException("构建返回值失败,未找到字段:"+listEntityPropName);
                    }
                }

                //根据粗数据对应的字段,获取需要组装数据的Map集合
                List resultComponentList=null;
                cols.removeAll(Collections.singleton(null));
                if(Objects.nonNull(iService)){
                    resultComponentList = ListUtil.getBatchInfoByCol(iService, annotation.resultListEntityColName(), cols);
                }else if(Objects.nonNull(baseMapper)){
                    resultComponentList = ListUtil.getBatchInfoByCol(baseMapper, annotation.resultListEntityColName(), cols);
                }else{
                    throw new IException("未找到Mapper或Service");
                }
                Map map = ListUtil.buildMap2(resultComponentList, getGetMethodNameByField(annotation.resultListMapKeyName()));

                //将返回值对象中需要组装的属性与对应的Map放入Map集合中避免重复遍历对象中所有字段
                resultBuildMap.put(declaredField.getName(),map);
            }
        }
    }

    private static <T,R> void assembleResultList(List<T> list,
                                                 List<R> resultList,
                                                 Class<R> resultClass,
                                                 Map<String,Map> resultBuildMap){
        for (T item : list) {
            R result = (R) ConvertUtils.sourceToTarget(item, resultClass);
//            key:deptName value:Map<Long,SysDeptEntity>
            resultBuildMap.forEach((key, value) -> {

                //获取返回值对象中的set方法
                String setMethodNameByField = getSetMethodNameByField(key);

                try {

                    //获取返回值对象
                    Class resultItem = result.getClass();

                    //获取返回值对象中需要组装的字段名称
                    Field declaredField = resultItem.getDeclaredField(key);

                    //获取注解
                    ResultUtil annotation = declaredField.getAnnotation(ResultUtil.class);
                    if(Objects.nonNull(annotation)){

                        //获取数据库中粗数据单个对象的字段值
                        String methodNameByField = getGetMethodNameByField(annotation.listEntityPropName());
//                        部门id
                        Object itemFieldValue = item.getClass().getDeclaredMethod(methodNameByField).invoke(item);

                        //使用需要组装的属性与对应的Map获取辅表对象实体类
//                        获取到部门实体类
                        Object resultItemEntity = value.get(itemFieldValue);


                        if(Objects.nonNull(resultItemEntity)){
                            Object resultItemEntityValue=null;
                            if(!annotation.isComplexObject()){
                                resultItemEntityValue = resultItemEntity
                                        .getClass()
                                        .getDeclaredMethod(getGetMethodNameByField(annotation.resultListEntityPropName()))
                                        .invoke(resultItemEntity);
                            }else{
                                resultItemEntityValue = ConvertUtils.sourceToTarget(resultItemEntity, declaredField.getType());
                            }

                            //使用反射调用set方法
                            result.getClass().getDeclaredMethod(setMethodNameByField,declaredField.getType())
                                    .invoke(result,new Object[]{resultItemEntityValue});
                        }
                    }

                } catch (NoSuchMethodException e) {
                    throw new IException("未找到方法:"+e.getMessage());
                } catch (NoSuchFieldException e) {
                    throw new IException("未找到字段:"+e.getMessage());
                } catch (IllegalAccessException e) {
                    throw new IException("执行方法异常:"+e.getMessage());
                } catch (InvocationTargetException e) {
                    throw new IException("执行方法:"+e.getMessage()+"异常");
                }
            });
            resultList.add(result);
        }
    }

    private static String getGetMethodNameByField(String fieldName){
        String methodName=new StringBuilder("get")
                .append(fieldName.substring(0,1).toUpperCase())
                .append(fieldName.substring(1)).toString();
        return methodName;
    }

    private static String getSetMethodNameByField(String fieldName){
        String methodName=new StringBuilder("set")
                .append(fieldName.substring(0,1).toUpperCase())
                .append(fieldName.substring(1)).toString();
        return methodName;
    }
}