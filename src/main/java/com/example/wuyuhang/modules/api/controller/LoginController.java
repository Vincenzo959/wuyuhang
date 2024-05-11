package com.example.wuyuhang.modules.api.controller;

import com.example.wuyuhang.modules.api.entity.SysUserLogin;
import com.example.wuyuhang.modules.api.iservice.ISysUserLoginService;
import com.example.wuyuhang.modules.api.util.Result;
import com.example.wuyuhang.modules.api.vo.SysUserLoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 卓佳伟
 * @date 2024/3/22 17:53
 * description
 */
@Api("用户登入、注册")
@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private ISysUserLoginService iSysUserLoginService;

    @ApiOperation("用户登入")
    @PostMapping("/sysUserLogin/select/API_001")
    public Result<SysUserLogin> sysUserLoginSelectAPI_001(@RequestBody SysUserLoginVo sysUserLoginVo){
        return iSysUserLoginService.sysUserLoginSelectAPI_001(sysUserLoginVo);
    }

    @ApiOperation("用户注册")
    @PostMapping("/sysUserLogin/insert/API_002")
    public Result<Boolean> sysUserLoginInsertAPI_002(@RequestBody SysUserLoginVo sysUserLoginVo){
        return iSysUserLoginService.sysUserLoginSelectAPI_002(sysUserLoginVo);
    }
}
