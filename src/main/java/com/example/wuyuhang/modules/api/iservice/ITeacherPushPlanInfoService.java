package com.example.wuyuhang.modules.api.iservice;

import com.example.wuyuhang.modules.api.entity.StudentPushPlanInfo;
import com.example.wuyuhang.modules.api.entity.TeacherPushPlanInfo;
import com.example.wuyuhang.modules.api.util.Condition;
import com.example.wuyuhang.modules.api.util.PageData;
import com.example.wuyuhang.modules.api.util.Result;
import com.example.wuyuhang.modules.api.vo.*;

import java.util.List;

/**
 * @author 卓佳伟
 * @date 2024/3/25 17:25
 * description
 */
public interface ITeacherPushPlanInfoService {
    Result<TeacherDataShowVo> teacherPushPlanInfoSelectAPI_001(Long id);

    Result<List<TeacherDataShowChartsVo>> studentPushPlanInfoSelectAPI_002(String name);

    Result<List<TeacherDataShowChartsVo>> studentPushPlanInfoSelectAPI_003(String name);

    Result<String> studentPushPlanInfoSelectAPI_004(Long id);

    Result<Boolean> studentPushPlanInfoSelectAPI_005(TeacherPushPlanInfoVo teacherPushPlanInfoVO);

    Result<PageData<StudentPushPlanInfoVo>> studentPushPlanInfoSelectAPI_006(Condition<StudentPlanVo> condition);

    Result<Boolean> studentPushPlanInfoUpdateAPI_007(AccessVo accessVo);

    Result<PageData<TeacherPushPlanInfoVo>> teacherPushPlanInfoSelectAPI_008(Condition<TeacherPlanVo> condition);

    Result<Boolean> teacherPushPlanInfoUpdateAPI_009(TeacherPlanManagerVo teacherPlanManagerVo);
}
