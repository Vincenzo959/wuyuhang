package com.example.wuyuhang.modules.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 卓佳伟
 * @date 2024/3/4 09:21
 * description
 */

@ApiModel(value = "SysStudentInfo对象", description = "学生信息表")
@TableName("sys_student_info")
@Data
public class SysStudentInfo implements Serializable {

    @ApiModelProperty(value = "用户id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "专业")
    private String umajor;

    @ApiModelProperty(value = "年段")
    private String ugrade;

    @ApiModelProperty(value = "班级")
    private String uclass;

    @ApiModelProperty(value = "学号")
    private String stdId;

    @ApiModelProperty(value = "手机号")
    private String telephoneNumber;

    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
