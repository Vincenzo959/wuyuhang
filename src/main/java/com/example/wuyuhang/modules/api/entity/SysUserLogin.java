package com.example.wuyuhang.modules.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 卓佳伟
 * @date 2024/3/22 17:54
 * description
 */
@Data
@ApiModel("用户登入、注册")
@TableName(value = "sys_user_login")
public class SysUserLogin implements Serializable {

    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "电话号码")
    private String telephoneNumber;

    @ApiModelProperty(value = "用户类型")
    private Integer type;

    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
