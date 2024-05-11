package com.example.wuyuhang.modules.api.util;

import lombok.Data;

/**
 * @author 卓佳伟
 * @date 2024/3/4 09:12
 * description
 */
@Data
public class QueryPage {
    private Long current;
    private Long total;
    private Long size;
}