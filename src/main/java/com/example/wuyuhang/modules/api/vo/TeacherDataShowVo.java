package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 卓佳伟
 * @date 2024/3/25 17:29
 * description
 */
@Data
public class TeacherDataShowVo implements Serializable {

    @ApiModelProperty("全部种类实习报告")
    private Integer allKinds;

    @ApiModelProperty("全部实习报告")
    private Integer allThing;

    @ApiModelProperty("未提交")
    private Integer unPush;

    @ApiModelProperty("已提交")
    private Integer push;

    @ApiModelProperty("已打分")
    private Integer score;

    @ApiModelProperty("未打分")
    private Integer unScore;

}
