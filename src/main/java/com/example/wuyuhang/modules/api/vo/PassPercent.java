package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 卓佳伟
 * @date 2024/3/29 11:42
 * description
 */
@Data
@ApiModel("及格率Vo")
public class PassPercent implements Serializable {

    @ApiModelProperty("及格率")
    private String pass;

    @ApiModelProperty("不及格率")
    private String fail;
}
