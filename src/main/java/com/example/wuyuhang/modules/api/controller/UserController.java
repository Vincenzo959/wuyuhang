package com.example.wuyuhang.modules.api.controller;

import com.example.wuyuhang.modules.api.iservice.ISysStudentInfoService;
import com.example.wuyuhang.modules.api.util.Condition;
import com.example.wuyuhang.modules.api.util.PageData;
import com.example.wuyuhang.modules.api.util.Result;
import com.example.wuyuhang.modules.api.vo.SysStudentInfoVo;
import com.example.wuyuhang.modules.api.vo.SysUserLoginVo;
import com.example.wuyuhang.modules.api.vo.UserInfoShow;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 卓佳伟
 * @date 2024/3/4 08:59
 * description
 */

@Api("用户增删改查接口")
@RestController
@RequestMapping("/api/userCrud")
public class UserController {

    @Autowired
    private ISysStudentInfoService iSysStudentInfoService;

    @PostMapping("/sysStudentInfo/list/API_001")
    @ApiOperation("查询学生列表")
    public Result<PageData<SysStudentInfoVo>> sysStudentInfoListAPI_001(@RequestBody Condition<SysStudentInfoVo> condition){
        return iSysStudentInfoService.sysStudentInfoListAPI_001(condition);
    }

    @PostMapping("/sysStudentInfo/update/API_002")
    @ApiOperation("学生个人信息修改/学生管理")
    public Result<Boolean> sysStudentInfoUpdateAPI_002(@RequestBody SysUserLoginVo sysUserLoginVo){
        return iSysStudentInfoService.sysStudentInfoUpdateAPI_002(sysUserLoginVo);
    }

    @PostMapping("/sysTeacherInfo/update/API_003")
    @ApiOperation("教师个人信息修改")
    public Result<Boolean> sysTeacherInfoUpdateAPI_003(@RequestBody SysUserLoginVo sysUserLoginVo){
        return iSysStudentInfoService.sysTeacherInfoUpdateAPI_003(sysUserLoginVo);
    }

    @PostMapping("/sysUserInfo/select/API_004/{id}")
    @ApiOperation("个人信息查询")
    public Result<UserInfoShow> userInfoShow(@PathVariable Long id){
        return iSysStudentInfoService.userInfoShow(id);
    }
}
