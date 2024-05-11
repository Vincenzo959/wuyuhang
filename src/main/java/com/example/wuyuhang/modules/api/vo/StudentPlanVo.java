package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 卓佳伟
 * @date 2024/3/28 14:18
 * description
 */
@Data
@ApiModel("学生实习报告模型Vo")
public class StudentPlanVo implements Serializable {

    @ApiModelProperty("教师id")
    private Long tchId;

    @ApiModelProperty("学号")
    private String stuId;

    @ApiModelProperty("班级")
    private String uclass;

    @ApiModelProperty("课程")
    private String pclass;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("实习实践报告名称")
    private String pname;

    @ApiModelProperty("是否评价")
    private Integer ifScore;
}
