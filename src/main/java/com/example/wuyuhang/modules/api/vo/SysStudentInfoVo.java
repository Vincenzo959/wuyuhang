package com.example.wuyuhang.modules.api.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.wuyuhang.common.annotation.QueryUtil;
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
public class SysStudentInfoVo implements Serializable {

    @ApiModelProperty(value = "用户id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "姓名")
    @QueryUtil(colName = "name", queryMode = "like", customProcess = false)
    private String name;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号")
    private String telephoneNumber;

    @ApiModelProperty(value = "专业")
    @QueryUtil(colName = "umajor", queryMode = "like", customProcess = false)
    private String umajor;

    @ApiModelProperty(value = "年段")
    @QueryUtil(colName = "ugrade", queryMode = "like", customProcess = false)
    private String ugrade;

    @ApiModelProperty(value = "班级")
    @QueryUtil(colName = "uclass", queryMode = "like", customProcess = false)
    private String uclass;

    @ApiModelProperty(value = "学号")
    @QueryUtil(colName = "std_id", queryMode = "like", customProcess = false)
    private String stdId;

    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
