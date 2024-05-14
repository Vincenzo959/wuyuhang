package com.example.wuyuhang.modules.api.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 卓佳伟
 * @date 2024/3/28 14:37
 * description
 */
@Data
@ApiModel("实习报告评定模板Vo")
public class StudentPushPlanInfoVo implements Serializable {


    @ApiModelProperty("学号")
    private String stuId;

    @ApiModelProperty(value = "用户名字")
    private String name;

    @ApiModelProperty(value = "班级")
    private String uclass;

    @ApiModelProperty(value = "所属用户id")
    private Long userId;

    @ApiModelProperty(value = "实习实践报告名称")
    private String planRealName;

    @ApiModelProperty(value = "课程名字")
    private String pclass;

    @ApiModelProperty(value = "报告提交名字")
    private String planName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "实验报告所在的minio地址")
    private String planUrl;

    @ApiModelProperty(value = "分数")
    private Integer score;

    @ApiModelProperty(value = "是否评价")
    private Integer ifAssess;

    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "planId")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

}
