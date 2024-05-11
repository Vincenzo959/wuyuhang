package com.example.wuyuhang.modules.api.util;

import lombok.Data;

/**
 * @author 卓佳伟
 * @date 2024/3/4 09:11
 * description
 */
@Data
public class Condition<T> {
    private T condition;
    private QueryPage page;
    //查询学生或者是老师
    private Integer flag;
}

