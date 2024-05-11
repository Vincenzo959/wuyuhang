package com.example.wuyuhang.modules.api.controller;

import com.example.wuyuhang.modules.api.entity.StudentPushPlanInfo;
import com.example.wuyuhang.modules.api.iservice.ITeacherPushPlanInfoService;
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
 * @date 2024/3/25 17:31
 * description
 */

@RestController
@RequestMapping("/api/teacher/plan")
@Api("教师实习实践计划")
public class TeacherController {

    @Autowired
    private ITeacherPushPlanInfoService iTeacherPushPlanInfoService;

    @GetMapping("/teacherPushPlanInfo/select/API_001/{id}")
    @ApiOperation("查询自己发布的所有实习报告：发布的全部、未提交、已提交、未打分、已打分（select）")
    public Result<TeacherDataShowVo> teacherPushPlanInfoSelectAPI_001(@PathVariable Long id){
        return iTeacherPushPlanInfoService.teacherPushPlanInfoSelectAPI_001(id);
    }

    @GetMapping("/studentPushPlanInfo/select/API_002/{name}")
    @ApiOperation("根据报告名称查询包含的班级的提交人数、及格、不及格（select，联合查询）")
    public Result<List<TeacherDataShowChartsVo>> studentPushPlanInfoSelectAPI_002(@PathVariable String name){
        return iTeacherPushPlanInfoService.studentPushPlanInfoSelectAPI_002(name);
    }

    @GetMapping("/studentPushPlanInfo/select/API_003/{name}")
    @ApiOperation("根据班级名称查询包含报告的提交人数、结果、不及格（select，联合查询）")
    public Result<List<TeacherDataShowChartsVo>> studentPushPlanInfoSelectAPI_003(@PathVariable String name){
        return iTeacherPushPlanInfoService.studentPushPlanInfoSelectAPI_003(name);
    }

    @GetMapping("/studentPushPlanInfo/select/API_004/{id}")
    @ApiOperation("查询是否有未评分的报告（select）")
    public Result<String> studentPushPlanInfoSelectAPI_004(@PathVariable Long id){
        return iTeacherPushPlanInfoService.studentPushPlanInfoSelectAPI_004(id);
    }

    @PostMapping("/teacherPushPlanInfo/insert/API_005")
    @ApiOperation("发布实习计划（insert）")
    public Result<Boolean> teacherPushPlanInfoInsertAPI_005(@RequestBody TeacherPushPlanInfoVo teacherPushPlanInfoVO){
        return iTeacherPushPlanInfoService.studentPushPlanInfoSelectAPI_005(teacherPushPlanInfoVO);
    }

    @PostMapping("/studentPushPlanInfo/select/API_006")
    @ApiOperation("学生实习报告查询（select）")
    public Result<PageData<StudentPushPlanInfoVo>> studentPushPlanInfoSelectAPI_006(@RequestBody Condition<StudentPlanVo> condition){
        return iTeacherPushPlanInfoService.studentPushPlanInfoSelectAPI_006(condition);
    }

    @PostMapping("/studentPushPlanInfo/update/API_007")
    @ApiOperation("打分（update）")
    public Result<Boolean> studentPushPlanInfoUpdateAPI_007(@RequestBody AccessVo accessVo){
        return iTeacherPushPlanInfoService.studentPushPlanInfoUpdateAPI_007(accessVo);
    }

    @PostMapping("/teacherPushPlanInfo/select/API_008")
    @ApiOperation("实习实践计划查询（select）")
    public Result<PageData<TeacherPushPlanInfoVo>> teacherPushPlanInfoSelectAPI_008(@RequestBody Condition<TeacherPlanVo> condition){
        return iTeacherPushPlanInfoService.teacherPushPlanInfoSelectAPI_008(condition);
    }

    @PostMapping("/teacherPushPlanInfo/update/API_009")
    @ApiOperation("实习实践计划管理（update）")
    public Result<Boolean> teacherPushPlanInfoUpdateAPI_009(@RequestBody TeacherPlanManagerVo teacherPlanManagerVo){
        return iTeacherPushPlanInfoService.teacherPushPlanInfoUpdateAPI_009(teacherPlanManagerVo);
    }

}
