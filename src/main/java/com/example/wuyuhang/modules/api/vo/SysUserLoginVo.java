package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 卓佳伟
 * @date 2024/3/25 16:05
 * description
 */
@Data
@ApiModel("用户登入传参")
public class SysUserLoginVo implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户类型")
    private Integer type;

    @ApiModelProperty(value = "手机号")
    private String telephoneNumber;

    @ApiModelProperty(value = "教师姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "教师年龄")
    private Integer age;

    @ApiModelProperty(value = "专业")
    private String major;

    @ApiModelProperty(value = "年段")
    private String grade;

    @ApiModelProperty(value = "班级")
    private String aclass;

    @ApiModelProperty(value = "学号")
    private String stdId;

    @ApiModelProperty(value = "教师邮箱")
    private String email;

    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
