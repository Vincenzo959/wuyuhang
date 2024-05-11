package com.example.wuyuhang.modules.api.controller;

import com.example.wuyuhang.modules.api.entity.StudentPushPlanInfo;
import com.example.wuyuhang.modules.api.entity.TeacherPushPlanInfo;
import com.example.wuyuhang.modules.api.iservice.IStudentPushPlanInfoService;
import com.example.wuyuhang.modules.api.util.Condition;
import com.example.wuyuhang.modules.api.util.PageData;
import com.example.wuyuhang.modules.api.util.Result;
import com.example.wuyuhang.modules.api.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 卓佳伟
 * @date 2024/3/25 21:03
 * description
 */

@RequestMapping("/api/student/plan")
@RestController
@Api("学生实习实践计划")
public class StudentController {

    @Autowired
    private IStudentPushPlanInfoService iStudentPushPlanInfoService;

    @GetMapping("/studentPushPlanInfo/select/API_001/{id}")
    @ApiOperation("查询所有的报告并区分出已提交、未提交、已打分、未打分、及格数、不及格数（select）")
    public Result<StudentDataShowVo> studentPushPlanInfoSelectAPI_001(@PathVariable Long id){
        return iStudentPushPlanInfoService.studentPushPlanInfoSelectAPI_001(id);
    }

    @GetMapping("/studentPushPlanInfo/select/API_002/{id}")
    @ApiOperation("查询近七次报告情况（select）")
    public Result<List<StudentDataShowChartsVo>> studentPushPlanInfoSelectAPI_002(@PathVariable Long id){
        return iStudentPushPlanInfoService.studentPushPlanInfoSelectAPI_002(id);
    }

    @PostMapping("/teacherPushPlanInfo/select/API_003")
    @ApiOperation("根据报告名称、教师名称、课程名称查询对应课程老师发布的报告（select）")
    public Result<List<TeacherPushPlanInfoVo>> teacherPushPlanInfoSelectAPI_003(@RequestBody SelectTeacherPushPlanVo selectTeacherPushPlanVo){
        return iStudentPushPlanInfoService.teacherPushPlanInfoSelectAPI_003(selectTeacherPushPlanVo);
    }

    @PostMapping("/studentPushPlanInfo/insert/API_004")
    @ApiOperation("填写消息并提交实验报告（insert）")
    public Result<Boolean> studentPushPlanInfoInsertAPI_004(@RequestBody StudentPushPlanParamVo studentPushPlanParamVo){
        return iStudentPushPlanInfoService.studentPushPlanInfoInsertAPI_004(studentPushPlanParamVo);
    }

    @PostMapping("/studentPushPlanInfo/select/API_005")
    @ApiOperation("通过班级、课程、标题以及老师是否评价（boolean）来查询已提交的实习报告（select）")
    public Result<PageData<StudentPushPlanInfoVo>> studentPushPlanInfoSelectAPI_005(@RequestBody Condition<StudentPushPlanParamVo> condition){
        return iStudentPushPlanInfoService.studentPushPlanInfoSelectAPI_005(condition);
    }

    @GetMapping("/studentPushPlanInfo/select/API_006/{id}")
    @ApiOperation("报告及格率")
    public Result<PassPercent> studentPushPlanInfoSelectAPI_006(@PathVariable Long id){
        return iStudentPushPlanInfoService.studentPushPlanInfoSelectAPI_006(id);
    }

    @GetMapping("/studentPushPlanInfo/select/API_007/{id}")
    @ApiOperation("查询是否还有未提交的报告")
    public Result<String> studentPushPlanInfoSelectAPI_007(@PathVariable Long id){
        return iStudentPushPlanInfoService.studentPushPlanInfoSelectAPI_007(id);
    }
}
