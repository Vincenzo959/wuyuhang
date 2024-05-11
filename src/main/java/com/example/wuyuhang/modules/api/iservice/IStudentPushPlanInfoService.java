package com.example.wuyuhang.modules.api.iservice;

import com.example.wuyuhang.modules.api.entity.StudentPushPlanInfo;
import com.example.wuyuhang.modules.api.util.Condition;
import com.example.wuyuhang.modules.api.util.PageData;
import com.example.wuyuhang.modules.api.util.Result;
import com.example.wuyuhang.modules.api.vo.*;

import java.util.List;

/**
 * @author 卓佳伟
 * @date 2024/3/25 18:02
 * description
 */
public interface IStudentPushPlanInfoService {
    Result<StudentDataShowVo> studentPushPlanInfoSelectAPI_001(Long id);

    Result<List<StudentDataShowChartsVo>> studentPushPlanInfoSelectAPI_002(Long id);

    Result<List<TeacherPushPlanInfoVo>> teacherPushPlanInfoSelectAPI_003(SelectTeacherPushPlanVo selectTeacherPushPlanVo);

    Result<Boolean> studentPushPlanInfoInsertAPI_004(StudentPushPlanParamVo studentPushPlanParamVo);

    Result<PageData<StudentPushPlanInfoVo>> studentPushPlanInfoSelectAPI_005(Condition<StudentPushPlanParamVo> condition);

    Result<PassPercent> studentPushPlanInfoSelectAPI_006(Long id);

    Result<String> studentPushPlanInfoSelectAPI_007(Long id);
}
