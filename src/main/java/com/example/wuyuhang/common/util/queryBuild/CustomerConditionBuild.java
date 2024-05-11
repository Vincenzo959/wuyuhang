package com.example.wuyuhang.common.util.queryBuild;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * @author 卓佳伟
 * @date 2024/3/14 15:08
 * description
 */
public interface CustomerConditionBuild {
    Boolean handle(String colName, Object value, QueryWrapper queryWrapper);
}
