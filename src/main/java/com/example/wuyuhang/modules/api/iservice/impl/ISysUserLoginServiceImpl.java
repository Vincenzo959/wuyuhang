package com.example.wuyuhang.modules.api.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.wuyuhang.modules.api.entity.SysStudentInfo;
import com.example.wuyuhang.modules.api.entity.SysTeacherInfo;
import com.example.wuyuhang.modules.api.entity.SysUserLogin;
import com.example.wuyuhang.modules.api.iservice.ISysUserLoginService;
import com.example.wuyuhang.modules.api.service.SysStudentInfoService;
import com.example.wuyuhang.modules.api.service.SysTeacherInfoService;
import com.example.wuyuhang.modules.api.service.SysUserLoginService;
import com.example.wuyuhang.modules.api.util.ConvertUtils;
import com.example.wuyuhang.modules.api.util.IException;
import com.example.wuyuhang.modules.api.util.Result;
import com.example.wuyuhang.modules.api.vo.SysUserLoginVo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author 卓佳伟
 * @date 2024/3/25 15:38
 * description
 */
@Service
public class ISysUserLoginServiceImpl implements ISysUserLoginService {
    private final static Integer IS_DELETED = 0;

    @Autowired
    private SysUserLoginService sysUserLoginService;

    @Autowired
    private SysStudentInfoService sysStudentInfoService;

    @Autowired
    private SysTeacherInfoService sysTeacherInfoService;

    @Override
    public Result<SysUserLogin> sysUserLoginSelectAPI_001(SysUserLoginVo sysUserLoginVo) {
        try{
            SysUserLogin user = sysUserLoginService.getOne(new QueryWrapper<SysUserLogin>()
                    .lambda()
                    .eq(SysUserLogin::getTelephoneNumber, sysUserLoginVo.getTelephoneNumber())
                    .eq(SysUserLogin::getPassword, sysUserLoginVo.getPassword())
                    .eq(SysUserLogin::getIsDeleted, IS_DELETED));

            if (Objects.nonNull(user)){
                return new Result<SysUserLogin>().ok(user);
            }
        }catch (Exception ex){
            throw new IException("登入失败，请重新登入");
        }
        return new Result<SysUserLogin>().error("用户名或密码错误");
    }

    @Override
    public Result<Boolean> sysUserLoginSelectAPI_002(SysUserLoginVo sysUserLoginVo) {
        try{
            Boolean flag = false;
            SysUserLogin userLoginInfo = sysUserLoginService.getOne(new QueryWrapper<SysUserLogin>()
                    .lambda()
                    .eq(SysUserLogin::getTelephoneNumber, sysUserLoginVo.getTelephoneNumber()));
            if (Objects.nonNull(userLoginInfo)){
                throw new IException("用户已存在");
            }
            SysUserLogin sysUserLogin = ConvertUtils.sourceToTarget(sysUserLoginVo, SysUserLogin.class);
            sysUserLogin.setIsDeleted(IS_DELETED);
            sysUserLogin.setCreateTime(LocalDateTime.now());
            Boolean ifSave = sysUserLoginService.save(sysUserLogin);
            if (ifSave){
                SysUserLogin newUserInfo = sysUserLoginService.getOne(new QueryWrapper<SysUserLogin>()
                        .lambda()
                        .eq(SysUserLogin::getTelephoneNumber, sysUserLogin.getTelephoneNumber()));
                // type = 0 为学生
                if (sysUserLogin.getType() == 0){
                    SysStudentInfo sysStudentInfo = ConvertUtils.sourceToTarget(sysUserLoginVo, SysStudentInfo.class);
                    sysStudentInfo.setId(newUserInfo.getId());
                    sysStudentInfo.setName(sysUserLoginVo.getUserName());
                    sysStudentInfo.setSex(sysUserLoginVo.getSex());
                    sysStudentInfo.setUmajor(sysUserLoginVo.getMajor());
                    sysStudentInfo.setUclass(sysUserLoginVo.getAclass());
                    sysStudentInfo.setUgrade(sysUserLoginVo.getGrade());
                    sysStudentInfo.setIsDeleted(IS_DELETED);
                    sysStudentInfo.setCreateTime(LocalDateTime.now());
                    flag = sysStudentInfoService.save(sysStudentInfo);
                }else if (sysUserLogin.getType() == 1){
                    SysTeacherInfo sysTeacherInfo = ConvertUtils.sourceToTarget(sysUserLoginVo, SysTeacherInfo.class);
                    sysTeacherInfo.setId(newUserInfo.getId());
                    sysTeacherInfo.setName(sysUserLoginVo.getUserName());
                    sysTeacherInfo.setTmajor(sysUserLoginVo.getMajor());
                    sysTeacherInfo.setTclass(sysUserLoginVo.getAclass());
                    sysTeacherInfo.setTgrade(sysUserLoginVo.getGrade());
                    sysTeacherInfo.setIsDeleted(IS_DELETED);
                    sysTeacherInfo.setCreateTime(LocalDateTime.now());
                    flag = sysTeacherInfoService.save(sysTeacherInfo);
                }
                return new Result<Boolean>().ok(flag);
            }else {
                throw new IException("注册失败，请刷新后重试！");
            }
        }catch (Exception ex){
            throw new IException("用户注册失败");
        }
    }
}
