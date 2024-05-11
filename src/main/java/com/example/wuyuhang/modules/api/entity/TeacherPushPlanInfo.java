package com.example.wuyuhang.modules.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 卓佳伟
 * @date 2024/3/25 17:16
 * description
 */
@Data
@TableName(value = "teacher_push_plan_info")
@ApiModel(value = "教师发布实习实践报告表")
public class TeacherPushPlanInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "实习实践名称")
    private String name;

    @ApiModelProperty(value = "指导教师")
    private Long guideTeacher;

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
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "实习结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "实习主要内容")
    private String content;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;
}
