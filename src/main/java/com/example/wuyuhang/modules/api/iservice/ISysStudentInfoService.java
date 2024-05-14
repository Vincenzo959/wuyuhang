package com.example.wuyuhang.modules.api.iservice;

import com.example.wuyuhang.modules.api.util.Condition;
import com.example.wuyuhang.modules.api.util.PageData;
import com.example.wuyuhang.modules.api.util.Result;
import com.example.wuyuhang.modules.api.vo.SysStudentInfoVo;
import com.example.wuyuhang.modules.api.vo.SysUserLoginVo;
import com.example.wuyuhang.modules.api.vo.UserInfoShow;

/**
 * @author 卓佳伟
 * @date 2024/3/4 09:38
 * description
 */
public interface ISysStudentInfoService {


    Result<PageData<SysStudentInfoVo>> sysStudentInfoListAPI_001(Condition<SysStudentInfoVo> condition);

    Result<Boolean> sysStudentInfoUpdateAPI_002(SysUserLoginVo sysUserLoginVo);

    Result<Boolean> sysTeacherInfoUpdateAPI_003(SysUserLoginVo sysUserLoginVo);

    Result<UserInfoShow> userInfoShow(Long id);
}
