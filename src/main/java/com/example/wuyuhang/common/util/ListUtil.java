package com.example.wuyuhang.common.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author 卓佳伟
 * @date 2024/3/21 16:50
 * description
 */
public class ListUtil<T> {

    /**
     * @Description //根据pageJson获取Mybatis Page对象
     * @Date 2023/2/7
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<T>
     **/
    public static<T> Page<T> buildPage(JSONObject page){
        return new Page<T>().setSize(page.getLong("size")).setCurrent(page.getLong("current"));
    }

    /**
     * @Description //根据pageJson获取mybatisXml Page对象
     * @Date 2023/2/7
     * @return com.alibaba.fastjson.JSONObject
     **/
    public static JSONObject getPageObject(JSONObject pageAndCondition) {
        JSONObject dbPage = new JSONObject();
        JSONObject page = pageAndCondition.getJSONObject("page");
        dbPage.put("start", (page.getLong("current") - 1) * page.getLong("size"));
        dbPage.put("end", page.getLong("size"));
        return dbPage;
    }

    /**
     * @Description //根据ids使用mapper获取list
     * @Date 2023/2/7
     * @return java.util.List<T>
     **/
    public static <T> List<T> getBatchInfoByIds(BaseMapper<T> mapper, Collection<? extends Serializable> ids) {
        if (ids.size() > 0) {
            return mapper.selectBatchIds(ids);
        }
        return new ArrayList<T>();
    }

    /**
     * @Description //根据ids使用service获取list
     * @Date 2023/2/7
     * @return java.util.List<T>
     **/
    public static <T> List<T> getBatchInfoByIds(IService<T> service, Collection<? extends Serializable> ids) {
        if (ids.size() > 0) {
            return service.listByIds(ids);
        }
        return new ArrayList<T>();
    }

    /**
     * @Description //根据cols使用service获取部分字段list
     * @Date 2023/2/7
     * @return java.util.List<T>
     **/
    public static <T> List<T> getBatchInfoByCol(IService<T> service, String colName,
                                                Collection<? extends Serializable> cols, String... selectCol) {
        if (cols.size() > 0) {
            return service.list(new QueryWrapper<T>().select(selectCol).in(colName, cols));
        }
        return new ArrayList<>();
    }

    /**
     * @Description //根据cols使用mapper获取部分字段list
     * @Date 2023/2/7
     * @return java.util.List<T>
     **/
    public static <T> List<T> getBatchInfoByCol(BaseMapper<T> mapper, String colName,
                                                Collection<? extends Serializable> cols, String... selectCol) {
        if (cols.size() > 0) {
            return mapper.selectList(new QueryWrapper<T>().select(selectCol).in(colName, cols));
        }
        return new ArrayList<>();
    }

    /**
     * @Description //根据cols使用service获取list
     * @Date 2023/2/7
     * @return java.util.List<T>
     **/
    public static <T> List<T> getBatchInfoByCol(IService<T> service, String colName,
                                                Collection<? extends Serializable> cols) {
        if (cols.size() > 0) {
            return service.list(new QueryWrapper<T>().in(colName, cols));
        }
        return new ArrayList<>();
    }

    /**
     * @Description //根据cols使用mapper获取list
     * @Date 2023/2/7
     * @return java.util.List<T>
     **/
    public static <T> List<T> getBatchInfoByCol(BaseMapper<T> mapper, String colName,
                                                Collection<? extends Serializable> cols) {
        if (cols.size() > 0) {
            return mapper.selectList(new QueryWrapper<T>().in(colName, cols));
        }
        return new ArrayList<>();
    }

    /**
     * @Description //根据ids使用service获取Map对象
     * @Date 2023/2/7
     * @return java.util.Map<java.lang.Long,T>
     **/
    public static <T> Map<Long,T> idsToObjectMap(IService<T> service, Collection<? extends Serializable> ids){
        return buildMap(getBatchInfoByIds(service,ids));
    }

    /**
     * @Description //根据ids使用mapper获取Map对象
     * @Date 2023/2/7
     * @return java.util.Map<java.lang.Long,T>
     **/
    public static <T> Map<Long,T> idsToObjectMap(BaseMapper<T> mapper, Collection<? extends Serializable> ids){
        return buildMap(getBatchInfoByIds(mapper,ids));
    }

    /**
     * @Description //根据list获取Map对象
     * @Date 2023/2/7
     * @return java.util.Map<java.lang.Long,T>
     **/
    public static <T> Map<Long, T> buildMap(List<T> list) {
        Map<Long, T> resMap = new HashMap<>();
        list.forEach(t -> {
            try {
                resMap.put(Long.valueOf(t.getClass()
                                .getMethod("getId")
                                .invoke(t, null)
                                .toString()),
                        t);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return resMap;
    }

    /**
     * @Description //根据list对象中的某个字段获取Map对象
     * @Date 2023/2/7
     * @return java.util.Map<java.lang.Long,T>
     **/
    public static <T> Map<Long, T> buildMap(List<T> list,String methodName) {
        Map<Long, T> resMap = new HashMap<>();
        list.forEach(t -> {
            try {
                resMap.put(Long.valueOf(t.getClass()
                                .getMethod(methodName)
                                .invoke(t, null)
                                .toString()),
                        t);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return resMap;
    }

    /**
     * @Description //根据list对象中的某个字段获取Map对象
     * @Date 2023/2/7
     * @return java.util.Map<R,T>
     **/
    public static <R,T> Map<R, T> buildMap2(List<T> list,String methodName) {
        Map<R, T> resMap = new HashMap<>();
        list.forEach(t -> {
            try {
                resMap.put((R)t.getClass().getMethod(methodName).invoke(t, null),
                        t);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return resMap;
    }

}