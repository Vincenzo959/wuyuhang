package com.example.wuyuhang.common.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.wuyuhang.common.util.queryBuild.CustomerConditionBuild;
import com.example.wuyuhang.modules.api.util.SpringContextUtils;

/**
 * @author 卓佳伟
 * @date 2024/3/14 15:07
 * description
 */
public class QueryConditionAdapter {
    public static<T> Boolean handle(String colName, String clazz, Object value, QueryWrapper<T> queryWrapper){
        CustomerConditionBuild customerConditionBuild =
                (CustomerConditionBuild) SpringContextUtils.getBean(clazz);
        return customerConditionBuild.handle(colName, value, queryWrapper);
    }
}
