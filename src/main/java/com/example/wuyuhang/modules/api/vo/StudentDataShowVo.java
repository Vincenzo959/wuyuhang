package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 卓佳伟
 * @date 2024/3/28 17:58
 * description
 */
@Data
@ApiModel("学生数据概览模型Vo")
public class StudentDataShowVo {

    @ApiModelProperty("全部实习报告")
    private Integer allThing;

    @ApiModelProperty("未提交")
    private Integer unPush;

    @ApiModelProperty("已提交")
    private Integer push;

    @ApiModelProperty("已打分")
    private Integer score;

    @ApiModelProperty("被打回")
    private Integer knockedBack;



}
