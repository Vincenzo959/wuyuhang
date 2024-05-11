package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 卓佳伟
 * @date 2024/3/26 09:17
 * description
 */
@Data
@ApiModel("教师数据展示页表格参数")
public class TeacherDataShowChartsVo implements Serializable {

    private String name;

    // 提交人数
    private Integer pushNum;

    // 合格人数
    private Integer passNum;

    // 不合格人数
    private Integer failNum;
}
