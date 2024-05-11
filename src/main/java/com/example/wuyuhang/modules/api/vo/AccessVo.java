package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 卓佳伟
 * @date 2024/3/28 14:51
 * description
 */
@Data
@ApiModel("打分Vo")
public class AccessVo implements Serializable {

    @ApiModelProperty("教师id")
    private Long teacherId;

    @ApiModelProperty("学生id")
    private Long studentId;

    @ApiModelProperty("分数")
    private Integer score;

    @ApiModelProperty("实习报告id")
    private Long planId;
}
