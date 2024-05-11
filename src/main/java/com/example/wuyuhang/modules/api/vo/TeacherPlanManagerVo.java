package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 卓佳伟
 * @date 2024/4/1 15:22
 * description
 */
@Data
@ApiModel("实习实践计划管理Vo")
public class TeacherPlanManagerVo implements Serializable {

    @ApiModelProperty(value = "实习实践id")
    private Long id;

    @ApiModelProperty(value = "实习实践名称")
    private String name;

    @ApiModelProperty(value = "指导教师id")
    private Long guideTeacher;

    @ApiModelProperty(value = "课程名称")
    private String pclass;

    @ApiModelProperty(value = "实习开始时间")
    private String beginTime;

    @ApiModelProperty(value = "实习结束时间")
    private String endTime;

    @ApiModelProperty(value = "实习地点")
    private String address;

    @ApiModelProperty(value = "实习主要内容")
    private String content;

    @ApiModelProperty(value = "备注")
    private String remark;

}
