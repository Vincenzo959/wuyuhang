package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 卓佳伟
 * @date 2024/3/28 11:02
 * description
 */
@Data
@ApiModel("年级、专业、班级")
public class ClassInfoVo implements Serializable {

    @ApiModelProperty("年级")
    private String ugrade;

    @ApiModelProperty("专业")
    private String umajor;

    @ApiModelProperty("班级")
    private String uclass;

}
