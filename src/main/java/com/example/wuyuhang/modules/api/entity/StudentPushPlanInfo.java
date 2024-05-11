package com.example.wuyuhang.modules.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 卓佳伟
 * @date 2024/3/25 18:00
 * description
 */

@Data
@Api("学生提交报告表")
@TableName(value = "student_push_plan_info")
public class StudentPushPlanInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "实验报告id")
    private Long belongsPlanId;

    @ApiModelProperty(value = "所属用户id")
    private Long userId;

    @ApiModelProperty(value = "课程名字")
    private String pclass;

    @ApiModelProperty(value = "报告提交名字")
    private String planName;

    @ApiModelProperty(value = "实验报告所在的minio地址")
    private String planUrl;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "分数")
    private Integer score;

    @ApiModelProperty(value = "是否评价")
    private Integer ifAssess;

    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}

