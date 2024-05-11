package com.example.wuyuhang.common.util.queryBuild;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.wuyuhang.common.adapter.QueryConditionAdapter;
import com.example.wuyuhang.common.annotation.QueryUtil;
import com.example.wuyuhang.modules.api.util.IException;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * @author 卓佳伟
 * @date 2024/3/14 09:27
 * description
 */
@Slf4j
public class QueryBuilder {

    public static <T, R> Boolean buildQueryWrapper(QueryWrapper<T> queryWrapper, R condition) {
        Field[] declaredFieldList = condition.getClass().getDeclaredFields();
        for (Field declaredField : declaredFieldList) {
            QueryUtil annotation = declaredField.getAnnotation(QueryUtil.class);
            if (Objects.nonNull(annotation)) {
                try {
                    String colName = annotation.colName();
                    String queryMode = annotation.queryMode();
                    Boolean customProcess = annotation.customProcess();
                    //构建查询条件
                    String methodName = new StringBuilder("get")
                            .append(declaredField.getName().substring(0, 1).toUpperCase())
                            .append(declaredField.getName().substring(1)).toString();
                    Object value = condition.getClass().getMethod(methodName).invoke(condition);

                    if (!customProcess) {
                        if (Objects.nonNull(value)) {
                            switch (queryMode) {
                                case "eq": {
                                    queryWrapper.eq(colName, value);
                                    break;
                                }
                                case "like": {
                                    queryWrapper.like(colName, value);
                                    break;
                                }
                                case "ne": {
                                    queryWrapper.ne(colName, value);
                                    break;
                                }
                                case "in": {
                                    List<? extends Serializable> validList = (List<? extends Serializable>) value;
                                    if (((List<?>) value).size() > 0) {
                                        queryWrapper.in(colName, value);
                                    }
                                    break;
                                }
                                case "isNotNull": {
                                    Integer isCondition = (Integer) value;
                                    if (isCondition == 1) {
                                        queryWrapper.isNotNull(colName);
                                    }
                                    break;
                                }
                                case "isNull": {
                                    Integer isCondition = (Integer) value;
                                    if (isCondition == 0) {
                                        queryWrapper.isNull(colName);
                                    }
                                    break;
                                }
                                case "notIn": {
                                    List<? extends Serializable> validList = (List<? extends Serializable>) value;
                                    if (validList.size() > 0) {
                                        queryWrapper.notIn(colName, validList);
                                    }
                                    break;
                                }
                            }
                        }
                    }else {
                        QueryConditionAdapter.handle(colName, annotation.customClass(), value, queryWrapper);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    log.error("{}", ex);
                    throw new IException("查询条件构建失败");
                }
            }
        }
        return true;
    }
}
