package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 卓佳伟
 * @date 2024/3/29 08:52
 * description
 */
@Data
@ApiModel("学生查询教师已发布的实习报告参数Vo")
public class SelectTeacherPushPlanVo implements Serializable {

    @ApiModelProperty("实习实践名称")
    private String planName;

    @ApiModelProperty("课程名称")
    private String pclass;

    @ApiModelProperty("教师名称")
    private String teacherName;

}
