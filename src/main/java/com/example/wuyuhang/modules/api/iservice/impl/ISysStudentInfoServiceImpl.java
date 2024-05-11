package com.example.wuyuhang.modules.api.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wuyuhang.common.util.queryBuild.QueryBuilder;
import com.example.wuyuhang.common.util.resultBuild.ResultBuilder;
import com.example.wuyuhang.modules.api.entity.SysStudentInfo;
import com.example.wuyuhang.modules.api.entity.SysTeacherInfo;
import com.example.wuyuhang.modules.api.entity.SysUserLogin;
import com.example.wuyuhang.modules.api.iservice.ISysStudentInfoService;
import com.example.wuyuhang.modules.api.service.SysStudentInfoService;
import com.example.wuyuhang.modules.api.service.SysTeacherInfoService;
import com.example.wuyuhang.modules.api.service.SysUserLoginService;
import com.example.wuyuhang.modules.api.util.*;
import com.example.wuyuhang.modules.api.vo.SysStudentInfoVo;
import com.example.wuyuhang.modules.api.vo.SysUserLoginVo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 卓佳伟
 * @date 2024/3/4 09:38
 * description
 */
@Service
public class ISysStudentInfoServiceImpl implements ISysStudentInfoService {
    private final static Integer IS_DELETED = 0;

    @Autowired
    private SysStudentInfoService sysStudentInfoService;

    @Autowired
    private SysTeacherInfoService sysTeacherInfoService;

    @Autowired
    private SysUserLoginService sysUserLoginService;

    @Override
    public Result<PageData<SysStudentInfoVo>> sysStudentInfoListAPI_001(Condition<SysStudentInfoVo> condition) {
        QueryPage page = condition.getPage();
        SysStudentInfoVo sysStudentInfoVo = condition.getCondition();
        Page<SysStudentInfo> sysStudentInfoPage = (Page<SysStudentInfo>) new Page<SysStudentInfo>().setPages(page.getSize()).setCurrent(page.getCurrent());
        QueryWrapper<SysStudentInfo> sysStudentInfoQueryWrapper = new QueryWrapper<>();
        sysStudentInfoQueryWrapper.lambda().orderByDesc(SysStudentInfo::getUgrade);

        Boolean buildQueryWrapper = QueryBuilder.buildQueryWrapper(sysStudentInfoQueryWrapper, sysStudentInfoVo);
        if (!buildQueryWrapper) {
            return new Result<PageData<SysStudentInfoVo>>().ok(new PageData<>(new ArrayList<>(), 0L));
        }
        sysStudentInfoService.page(sysStudentInfoPage, sysStudentInfoQueryWrapper);

        List<SysStudentInfoVo> sysStudentInfoList = new ArrayList<>();
        if (sysStudentInfoPage.getRecords().size() > 0) {
            ResultBuilder.buildResult(sysStudentInfoPage.getRecords(), sysStudentInfoList, SysStudentInfoVo.class);
        }
        return new Result<PageData<SysStudentInfoVo>>().ok(new PageData<>(sysStudentInfoList, sysStudentInfoPage.getTotal()));
    }

    @Override
    public Result<Boolean> sysStudentInfoUpdateAPI_002(SysUserLoginVo sysUserLoginVo) {
        try {
            SysStudentInfo sysOldStudentInfo = sysStudentInfoService.getOne(new QueryWrapper<SysStudentInfo>()
                    .lambda()
                    .eq(SysStudentInfo::getIsDeleted, IS_DELETED)
                    .eq(SysStudentInfo::getId, sysUserLoginVo.getId()));

            if (Objects.isNull(sysOldStudentInfo)) {
                throw new IException("当前用户不存在");
            }

            SysUserLogin sysOldUserLoginInfo = sysUserLoginService.getOne(new QueryWrapper<SysUserLogin>()
                    .lambda()
                    .eq(SysUserLogin::getIsDeleted, IS_DELETED)
                    .eq(SysUserLogin::getId, sysUserLoginVo.getId()));
            Boolean flag1 = sysUserLoginService.update(new UpdateWrapper<SysUserLogin>()
                    .lambda()
                    .eq(SysUserLogin::getIsDeleted, IS_DELETED)
                    .eq(SysUserLogin::getId, sysUserLoginVo.getId())
                    .set(SysUserLogin::getTelephoneNumber, Objects.nonNull(sysUserLoginVo.getTelephoneNumber()) ?
                            sysUserLoginVo.getTelephoneNumber() : sysOldUserLoginInfo.getTelephoneNumber())
                    .set(SysUserLogin::getIsDeleted, Objects.nonNull(sysUserLoginVo.getIsDeleted()) ?
                            sysUserLoginVo.getIsDeleted() : sysOldUserLoginInfo.getIsDeleted()));
            Integer type = sysUserLoginVo.getType();
            if (Objects.nonNull(type) && Objects.nonNull(sysUserLoginVo.getId())) {
                // 更新专属角色表
                Boolean flag2 = sysStudentInfoService.update(new UpdateWrapper<SysStudentInfo>()
                        .lambda()
                        .eq(SysStudentInfo::getId, sysUserLoginVo.getId())
                        .eq(SysStudentInfo::getIsDeleted, IS_DELETED)
                        .set(SysStudentInfo::getAge, Objects.nonNull(sysUserLoginVo.getAge()) ?
                                sysUserLoginVo.getAge() : sysOldStudentInfo.getAge())
                        .set(SysStudentInfo::getSex, Objects.nonNull(sysUserLoginVo.getSex()) ?
                                sysUserLoginVo.getSex() : sysOldStudentInfo.getSex())
                        .set(SysStudentInfo::getTelephoneNumber, Objects.nonNull(sysUserLoginVo.getTelephoneNumber()) ?
                                sysUserLoginVo.getTelephoneNumber() : sysOldStudentInfo.getTelephoneNumber())
                        .set(SysStudentInfo::getEmail, Objects.nonNull(sysUserLoginVo.getEmail()) ?
                                sysUserLoginVo.getEmail() : sysOldStudentInfo.getEmail())
                        .set(SysStudentInfo::getIsDeleted, Objects.nonNull(sysUserLoginVo.getIsDeleted()) ?
                                sysUserLoginVo.getIsDeleted() : sysOldStudentInfo.getIsDeleted()));

                return new Result<Boolean>().ok(flag1 && flag2);
            } else {
                throw new IException("用户更新失败");
            }
        } catch (Exception ex) {
            throw new IException("未查到当前用户");
        }
    }

    @Override
    public Result<Boolean> sysTeacherInfoUpdateAPI_003(SysUserLoginVo sysUserLoginVo) {
        try {
            SysTeacherInfo sysOldTeacherInfo = sysTeacherInfoService.getOne(new QueryWrapper<SysTeacherInfo>()
                    .lambda()
                    .eq(SysTeacherInfo::getIsDeleted, IS_DELETED)
                    .eq(SysTeacherInfo::getId, sysUserLoginVo.getId()));

            if (Objects.isNull(sysOldTeacherInfo)) {
                throw new IException("当前用户不存在");
            }

            SysUserLogin sysOldUserLoginInfo = sysUserLoginService.getOne(new QueryWrapper<SysUserLogin>()
                    .lambda()
                    .eq(SysUserLogin::getIsDeleted, IS_DELETED)
                    .eq(SysUserLogin::getId, sysUserLoginVo.getId()));
            Boolean flag1 = sysUserLoginService.update(new UpdateWrapper<SysUserLogin>()
                    .lambda()
                    .eq(SysUserLogin::getIsDeleted, IS_DELETED)
                    .eq(SysUserLogin::getId, sysUserLoginVo.getId())
                    .set(SysUserLogin::getTelephoneNumber, Objects.nonNull(sysUserLoginVo.getTelephoneNumber()) ?
                            sysUserLoginVo.getTelephoneNumber() : sysOldUserLoginInfo.getTelephoneNumber())
                    .set(SysUserLogin::getIsDeleted, Objects.nonNull(sysUserLoginVo.getIsDeleted()) ?
                            sysUserLoginVo.getIsDeleted() : sysOldUserLoginInfo.getIsDeleted()));
            Integer type = sysUserLoginVo.getType();
            if (Objects.nonNull(type) && Objects.nonNull(sysUserLoginVo.getId())) {
                // 更新专属角色表
                Boolean flag2 = sysTeacherInfoService.update(new UpdateWrapper<SysTeacherInfo>()
                        .lambda()
                        .eq(SysTeacherInfo::getId, sysUserLoginVo.getId())
                        .eq(SysTeacherInfo::getIsDeleted, IS_DELETED)
                        .set(SysTeacherInfo::getAge, Objects.nonNull(sysUserLoginVo.getAge()) ?
                                sysUserLoginVo.getAge() : sysOldTeacherInfo.getAge())
                        .set(SysTeacherInfo::getSex, Objects.nonNull(sysUserLoginVo.getSex()) ?
                                sysUserLoginVo.getSex() : sysOldTeacherInfo.getSex())
                        .set(SysTeacherInfo::getTelephoneNumber, Objects.nonNull(sysUserLoginVo.getTelephoneNumber()) ?
                                sysUserLoginVo.getTelephoneNumber() : sysOldTeacherInfo.getTelephoneNumber())
                        .set(SysTeacherInfo::getEmail, Objects.nonNull(sysUserLoginVo.getEmail()) ?
                                sysUserLoginVo.getEmail() : sysOldTeacherInfo.getEmail())
                        .set(SysTeacherInfo::getIsDeleted, Objects.nonNull(sysUserLoginVo.getIsDeleted()) ?
                                sysUserLoginVo.getIsDeleted() : sysOldTeacherInfo.getIsDeleted()));
                return new Result<Boolean>().ok(flag1 && flag2);
            } else {
                throw new IException("用户更新失败");
            }
        } catch (Exception ex) {
            throw new IException("未查到当前用户");
        }
    }
}
