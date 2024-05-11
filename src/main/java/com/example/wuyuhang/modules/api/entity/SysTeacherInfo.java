package com.example.wuyuhang.modules.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 卓佳伟
 * @date 2024/3/25 15:42
 * description
 */
@Data
@ApiModel("系统教师表")
@TableName(value = "sys_teacher_info")
public class SysTeacherInfo implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "教师姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String telephoneNumber;

    @ApiModelProperty(value = "教师性别")
    private String sex;

    @ApiModelProperty(value = "教师年龄")
    private Integer age;

    @ApiModelProperty(value = "教师专业")
    private String tmajor;

    @ApiModelProperty(value = "教师年级")
    private String tgrade;

    @ApiModelProperty(value = "教师班级")
    private String tclass;

    @ApiModelProperty(value = "教师邮箱")
    private String email;

    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
