package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 卓佳伟
 * @date 2024/3/28 14:00
 * description
 */
@Data
@ApiModel("教师时间计划模板Vo")
public class TeacherPlanVo implements Serializable {

    @ApiModelProperty("实习实践名称")
    private String name;

    @ApiModelProperty("课程名称")
    private String pclass;

    @ApiModelProperty("指导教师")
    private String id;

    @ApiModelProperty("开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
}
