package com.example.wuyuhang.modules.api.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 卓佳伟
 * @date 2024/3/4 09:42
 * description
 */
@Data
@Slf4j
public class IException extends RuntimeException{

    private Integer code=500;

    public IException(String message) {
        super(message);
    }

    public IException(String message,Integer code) {
        super(message);
        this.code=code;
    }
}