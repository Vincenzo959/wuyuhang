package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 卓佳伟
 * @date 2024/3/28 18:03
 * description
 */
@Data

public class StudentDataShowChartsVo implements Serializable {

    @ApiModelProperty("报告名字")
    private String planName;

    @ApiModelProperty("自己的分数")
    private Float userScore;

    @ApiModelProperty("班级平均分")
    private Float classScore;

}
