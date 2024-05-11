package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 卓佳伟
 * @date 2024/3/25 17:22
 * description
 */
@Data
@ApiModel("教师发布实践报告Vo")
public class TeacherPushPlanInfoVo implements Serializable {

    @ApiModelProperty(value = "实习实践id")
    private Long id;

    @ApiModelProperty(value = "实习实践名称")
    private String name;

    @ApiModelProperty(value = "指导教师")
    private String guideTeacherString;

    @ApiModelProperty(value = "课程名称")
    private String pclass;

    @ApiModelProperty(value = "年级")
    private String ugrade;

    @ApiModelProperty(value = "专业")
    private String umajor;

    @ApiModelProperty(value = "班级")
    private String uclass;

    @ApiModelProperty(value = "实习地点")
    private String address;

    @ApiModelProperty(value = "实习所需技术")
    private String techo;

    @ApiModelProperty(value = "实习人数")
    private Integer studentNum;

    @ApiModelProperty(value = "实习开始时间")
    private String beginTime;

    @ApiModelProperty(value = "实习结束时间")
    private String endTime;

    @ApiModelProperty(value = "实习主要内容")
    private String content;

    @ApiModelProperty(value = "备注")
    private String remark;

}
