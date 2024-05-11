package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 卓佳伟
 * @date 2024/3/29 08:57
 * description
 */
@Data
@ApiModel("学生提交与查询实习报告参数Vo")
public class StudentPushPlanParamVo implements Serializable {

    @ApiModelProperty(value = "实验报告")
    private String belongsPlanString;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty(value = "课程名字")
    private String pclass;

    @ApiModelProperty(value = "提交名字")
    private String planName;

    @ApiModelProperty(value = "实验报告所在的minio地址")
    private String planUrl;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "是否评价")
    private Integer ifAssess;
}
