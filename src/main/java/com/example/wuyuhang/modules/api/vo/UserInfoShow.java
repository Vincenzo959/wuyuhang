package com.example.wuyuhang.modules.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @author 卓佳伟
 * @date 2024/5/12 20:03
 * description
 */
@Data
@ApiModel("用户中心-查询用户信息类Vo")
public class UserInfoShow {

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("专业")
    private String major;

    @ApiModelProperty("年级")
    private String grade;

    @ApiModelProperty("班级")
    private String uclass;

    @ApiModelProperty("年龄")
    private String age;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("手机号")
    private String telephoneNumber;

    @ApiModelProperty("邮箱")
    private String email;
}
