package com.example.wuyuhang.modules.api.iservice;

import com.example.wuyuhang.modules.api.entity.SysUserLogin;
import com.example.wuyuhang.modules.api.util.Result;
import com.example.wuyuhang.modules.api.vo.SysUserLoginVo;

/**
 * @author 卓佳伟
 * @date 2024/3/25 15:38
 * description
 */
public interface ISysUserLoginService {
    Result<SysUserLogin> sysUserLoginSelectAPI_001(SysUserLoginVo sysUserLoginVo);

    Result<Boolean> sysUserLoginSelectAPI_002(SysUserLoginVo sysUserLoginVo);
}
