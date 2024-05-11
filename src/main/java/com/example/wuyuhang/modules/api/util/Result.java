package com.example.wuyuhang.modules.api.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "响应")
@Data
public class Result<T> implements Serializable {
    @ApiModelProperty(value = "编码：0表示成功，其他值表示失败")
    private int code = 0;
    @ApiModelProperty(value = "消息内容")
    private String msg = "success";
    @ApiModelProperty(value = "响应数据")
    private T data;

    public Result<T> ok(T data) {
        this.setData(data);
        return this;
    }

    public boolean success(){
        return code == 0 ? true : false;
    }

    public Result<T> error() {
        this.code = 500;
        this.msg = getMsg();
        return this;
    }

    public Result<T> error(int code) {
        this.code = code;
        this.msg = getMsg();
        return this;
    }

    public Result<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public Result<T> error(String msg) {
        this.code = 500;
        this.msg = msg;
        return this;
    }
}
