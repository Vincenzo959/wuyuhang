package com.example.wuyuhang.modules.api.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wuyuhang.modules.api.entity.StudentPushPlanInfo;
import com.example.wuyuhang.modules.api.entity.SysStudentInfo;
import com.example.wuyuhang.modules.api.entity.SysTeacherInfo;
import com.example.wuyuhang.modules.api.entity.TeacherPushPlanInfo;
import com.example.wuyuhang.modules.api.iservice.IStudentPushPlanInfoService;
import com.example.wuyuhang.modules.api.service.StudentPushPlanInfoService;
import com.example.wuyuhang.modules.api.service.SysStudentInfoService;
import com.example.wuyuhang.modules.api.service.SysTeacherInfoService;
import com.example.wuyuhang.modules.api.service.TeacherPushPlanInfoService;
import com.example.wuyuhang.modules.api.util.*;
import com.example.wuyuhang.modules.api.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卓佳伟
 * @date 2024/3/25 18:02
 * description
 */
@Service
@Transactional
@Slf4j
public class IStudentPushPlanInfoServiceImpl implements IStudentPushPlanInfoService {
    private final static Integer ASSESS = 1;
    private final static Integer RESET = 2;
    private final static Integer MAX_DATA_SHOW = 7;
    private final static Integer IS_DELETED = 0;
    private final static Integer SCORE_PASS = 60;

    @Autowired
    private StudentPushPlanInfoService studentPushPlanInfoService;

    @Autowired
    private SysStudentInfoService studentInfoService;

    @Autowired
    private SysTeacherInfoService teacherInfoService;

    @Autowired
    private TeacherPushPlanInfoService teacherPushPlanInfoService;

    @Override
    public Result<StudentDataShowVo> studentPushPlanInfoSelectAPI_001(Long id) {
        try {
            // 求出学生所在的班级
            SysStudentInfo studentInfo = studentInfoService.getOne(new QueryWrapper<SysStudentInfo>()
                    .lambda()
                    .eq(SysStudentInfo::getId, id));
            if (Objects.isNull(studentInfo)) {
                throw new IException("学生不存在");
            }
            ClassInfoVo classInfoVo = ConvertUtils.sourceToTarget(studentInfo, ClassInfoVo.class);
            // 查询老师在这个班发布了几个实习实践计划
            List<TeacherPushPlanInfo> teacherPushPlanInfoList = teacherPushPlanInfoService.list(new QueryWrapper<TeacherPushPlanInfo>()
                    .lambda()
                    .eq(TeacherPushPlanInfo::getUgrade, classInfoVo.getUgrade())
                    .eq(TeacherPushPlanInfo::getUmajor, classInfoVo.getUmajor())
                    .eq(TeacherPushPlanInfo::getUclass, classInfoVo.getUclass()));

            // 查询学生已提交的报告
            QueryWrapper<StudentPushPlanInfo> studentPushPlanInfoQueryWrapper = new QueryWrapper<>();
            studentPushPlanInfoQueryWrapper.lambda()
                    .eq(StudentPushPlanInfo::getUserId, id);
            List<StudentPushPlanInfo> studentPushPlanInfoList =
                    studentPushPlanInfoService.list(studentPushPlanInfoQueryWrapper);
            // 已打分
            studentPushPlanInfoQueryWrapper.lambda()
                    .eq(StudentPushPlanInfo::getIfAssess, ASSESS);
            List<StudentPushPlanInfo> studentPushPlanInfoList1 =
                    studentPushPlanInfoService.list(studentPushPlanInfoQueryWrapper);

            // 打回重做
            QueryWrapper<StudentPushPlanInfo> studentPushPlanInfoQueryWrapper2 = new QueryWrapper<>();
            studentPushPlanInfoQueryWrapper2.lambda()
                    .eq(StudentPushPlanInfo::getUserId, id)
                    .eq(StudentPushPlanInfo::getIfAssess, RESET);
            List<StudentPushPlanInfo> studentPushPlanInfoList2 =
                    studentPushPlanInfoService.list(studentPushPlanInfoQueryWrapper2);
            StudentDataShowVo studentDataShowVo = new StudentDataShowVo();
            studentDataShowVo.setAllThing(teacherPushPlanInfoList.size());
            studentDataShowVo.setUnPush(teacherPushPlanInfoList.size() - studentPushPlanInfoList.size());
            studentDataShowVo.setPush(studentPushPlanInfoList.size());
            studentDataShowVo.setScore(studentPushPlanInfoList1.size());
            studentDataShowVo.setKnockedBack(studentPushPlanInfoList2.size());
            return new Result<StudentDataShowVo>().ok(studentDataShowVo);
        } catch (Exception ex) {
            throw new IException("查询失败");
        }
    }

    @Override
    public Result<List<StudentDataShowChartsVo>> studentPushPlanInfoSelectAPI_002(Long id) {
        try {
            // 求出学生所在的班级
            SysStudentInfo studentInfo = studentInfoService.getOne(new QueryWrapper<SysStudentInfo>()
                    .lambda()
                    .eq(SysStudentInfo::getId, id));
            if (Objects.isNull(studentInfo)) {
                throw new IException("学生不存在");
            }
            ClassInfoVo classInfoVo = ConvertUtils.sourceToTarget(studentInfo, ClassInfoVo.class);
            // 求出班级所有的学生id
            Set<Long> studentIdSet = studentInfoService.list(new QueryWrapper<SysStudentInfo>()
                    .lambda()
                    .eq(SysStudentInfo::getUgrade, classInfoVo.getUgrade())
                    .eq(SysStudentInfo::getUmajor, classInfoVo.getUmajor())
                    .eq(SysStudentInfo::getUclass, classInfoVo.getUclass())
                    .eq(SysStudentInfo::getIsDeleted, IS_DELETED))
                    .stream().map(SysStudentInfo::getId).collect(Collectors.toSet());
            // 实习实践报告id-学生id的Map
            Map<Long, List<Integer>> planIdScoreListMap = studentPushPlanInfoService.list(new QueryWrapper<StudentPushPlanInfo>()
                    .lambda()
                    .in(StudentPushPlanInfo::getUserId, studentIdSet)
                    .eq(StudentPushPlanInfo::getIfAssess, ASSESS)
                    .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED))
                    .stream()
                    .collect(Collectors.groupingBy(StudentPushPlanInfo::getBelongsPlanId,
                            Collectors.mapping(StudentPushPlanInfo::getScore, Collectors.toList())));
            // 已打分的本学生报告
            List<StudentPushPlanInfo> studentPushPlanInfoList = studentPushPlanInfoService.list(new QueryWrapper<StudentPushPlanInfo>()
                    .lambda()
                    .eq(StudentPushPlanInfo::getUserId, id)
                    .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED)
                    .orderByDesc(StudentPushPlanInfo::getCreateTime))
                    .stream()
                    .filter(studentPushPlanInfo -> studentPushPlanInfo.getIfAssess() == 1)
                    .collect(Collectors.toList());


            List<StudentDataShowChartsVo> studentDataShowChartsVoList = new ArrayList<>();
            studentPushPlanInfoList.forEach(studentPushPlanInfo -> {
                StudentDataShowChartsVo studentDataShowChartsVo = new StudentDataShowChartsVo();
                studentDataShowChartsVo.setPlanName(studentPushPlanInfo.getPclass() + studentPushPlanInfo.getPlanName());
                studentDataShowChartsVo.setUserScore(studentPushPlanInfo.getScore().floatValue());
                if (Objects.nonNull(studentPushPlanInfo.getBelongsPlanId())) {
                    Integer finalScore = 0;
                    Integer studentNum = planIdScoreListMap.get(studentPushPlanInfo.getBelongsPlanId()).size();
                    List<Integer> scoreList = planIdScoreListMap.get(studentPushPlanInfo.getBelongsPlanId());
                    for (Integer score : scoreList) {
                        finalScore += score;
                    }
                    studentDataShowChartsVo.setClassScore((float) finalScore / studentNum);
                }

                studentDataShowChartsVoList.add(studentDataShowChartsVo);
            });
            if (studentDataShowChartsVoList.size() < MAX_DATA_SHOW) {
                return new Result<List<StudentDataShowChartsVo>>().ok(studentDataShowChartsVoList);
            }
            return new Result<List<StudentDataShowChartsVo>>().ok(studentDataShowChartsVoList.subList(0, 7));
        } catch (Exception ex) {
            throw new IException("查询失败");
        }
    }

    @Override
    public Result<List<TeacherPushPlanInfoVo>> teacherPushPlanInfoSelectAPI_003(SelectTeacherPushPlanVo selectTeacherPushPlanVo) {
        Map<Long, SysTeacherInfo> teacherIdTeacherInfoMap = teacherInfoService.list(new QueryWrapper<SysTeacherInfo>()
                .lambda()
                .eq(SysTeacherInfo::getIsDeleted, IS_DELETED))
                .stream()
                .collect(Collectors.toMap(
                        SysTeacherInfo::getId,
                        SysTeacherInfo -> SysTeacherInfo,
                        (per, next) -> next
                ));

        if (Objects.nonNull(selectTeacherPushPlanVo.getTeacherName())) {
            SysTeacherInfo sysTeacherInfo = teacherInfoService.getOne(new QueryWrapper<SysTeacherInfo>()
                    .lambda()
                    .like(SysTeacherInfo::getName, selectTeacherPushPlanVo.getTeacherName()));

            if (Objects.nonNull(sysTeacherInfo)) {
                Long id = sysTeacherInfo.getId();
                List<TeacherPushPlanInfo> teacherPushPlanInfoList = teacherPushPlanInfoService.list(new QueryWrapper<TeacherPushPlanInfo>()
                        .lambda()
                        .like(TeacherPushPlanInfo::getGuideTeacher, id)
                        .like(TeacherPushPlanInfo::getName, selectTeacherPushPlanVo.getPlanName())
                        .eq(TeacherPushPlanInfo::getIsDeleted, IS_DELETED));

                List<TeacherPushPlanInfoVo> teacherPushPlanInfoVoList = new ArrayList<>();
                teacherPushPlanInfoList.forEach(teacherPushPlanInfo -> {
                    TeacherPushPlanInfoVo teacherPushPlanInfoVo = ConvertUtils.sourceToTarget(teacherPushPlanInfo, TeacherPushPlanInfoVo.class);
                    Long id1 = teacherPushPlanInfo.getGuideTeacher();
                    if (Objects.nonNull(teacherIdTeacherInfoMap.get(id1))){
                        SysTeacherInfo sysTeacherInfo1 = teacherIdTeacherInfoMap.get(id1);
                        if (Objects.nonNull(sysTeacherInfo1)){
                            teacherPushPlanInfoVo.setGuideTeacherString(sysTeacherInfo1.getName());
                            teacherPushPlanInfoVoList.add(teacherPushPlanInfoVo);
                        }
                    }else {
                        throw new IException("未查询到当前教师");
                    }
                });

                return new Result<List<TeacherPushPlanInfoVo>>().ok(teacherPushPlanInfoVoList);
            } else {
                return new Result<List<TeacherPushPlanInfoVo>>().ok(new ArrayList<>());
            }
        } else {
            List<TeacherPushPlanInfo> teacherPushPlanInfoList = teacherPushPlanInfoService.list(new QueryWrapper<TeacherPushPlanInfo>()
                    .lambda()
                    .like(TeacherPushPlanInfo::getName, selectTeacherPushPlanVo.getPlanName())
                    .eq(TeacherPushPlanInfo::getIsDeleted, IS_DELETED));
            List<TeacherPushPlanInfoVo> teacherPushPlanInfoVoList = new ArrayList<>();
            teacherPushPlanInfoList.forEach(teacherPushPlanInfo -> {
                TeacherPushPlanInfoVo teacherPushPlanInfoVo = ConvertUtils.sourceToTarget(teacherPushPlanInfo, TeacherPushPlanInfoVo.class);
                if (Objects.nonNull(teacherPushPlanInfo)){
                    Long id1 = teacherPushPlanInfo.getGuideTeacher();
                    if (Objects.nonNull(id1)){
                        SysTeacherInfo sysTeacherInfo1 = teacherIdTeacherInfoMap.get(id1);
                        if (Objects.nonNull(sysTeacherInfo1)){
                            teacherPushPlanInfoVo.setGuideTeacherString(sysTeacherInfo1.getName());
                            teacherPushPlanInfoVoList.add(teacherPushPlanInfoVo);
                        }
                    }
                }
            });
            return new Result<List<TeacherPushPlanInfoVo>>().ok(teacherPushPlanInfoVoList);
        }
    }

    @Override
    public Result<Boolean> studentPushPlanInfoInsertAPI_004(StudentPushPlanParamVo studentPushPlanParamVo) {
        StudentPushPlanInfo studentPushPlanInfo = ConvertUtils.sourceToTarget(studentPushPlanParamVo, StudentPushPlanInfo.class);
        if (Objects.nonNull(studentPushPlanInfo)) {
            if (Objects.nonNull(studentPushPlanInfo.getPlanUrl())) {
                List<TeacherPushPlanInfo> teacherPushPlanInfoList = teacherPushPlanInfoService.list(new QueryWrapper<TeacherPushPlanInfo>()
                        .lambda()
                        .eq(TeacherPushPlanInfo::getIsDeleted, IS_DELETED)
                        .eq(TeacherPushPlanInfo::getName, studentPushPlanParamVo.getBelongsPlanString()));
                if (teacherPushPlanInfoList.size() == 0 || Objects.isNull(teacherPushPlanInfoList.get(0))) {
                    throw new IException("未查询到当前实验报告");
                }
                List<SysStudentInfo> sysStudentInfoList = studentInfoService.list(new QueryWrapper<SysStudentInfo>()
                        .lambda()
                        .eq(SysStudentInfo::getIsDeleted, IS_DELETED)
                        .eq(SysStudentInfo::getId, studentPushPlanParamVo.getUserId()));
                if (sysStudentInfoList.size() == 0 || Objects.isNull(sysStudentInfoList.get(0))) {
                    throw new IException("用户为空");
                }
                SysStudentInfo sysStudentInfo = sysStudentInfoList.get(0);
                TeacherPushPlanInfo teacherPushPlanInfo = teacherPushPlanInfoList.get(0);

                Boolean qel1 = Objects.equals(sysStudentInfo.getUmajor(), teacherPushPlanInfo.getUmajor());
                Boolean qel2 = Objects.equals(sysStudentInfo.getUgrade(), teacherPushPlanInfo.getUgrade());
                Boolean qel3 = Objects.equals(sysStudentInfo.getUclass(), teacherPushPlanInfo.getUclass());

                if (qel1 && qel2 && qel3) {
                    studentPushPlanInfo.setBelongsPlanId(teacherPushPlanInfo.getId());
                    studentPushPlanInfo.setPlanName(studentPushPlanInfo.getPlanUrl().substring(37));
                    studentPushPlanInfo.setIfAssess(0);
                    studentPushPlanInfo.setCreateTime(LocalDateTime.now());
                    studentPushPlanInfo.setIsDeleted(0);
                    boolean save = studentPushPlanInfoService.save(studentPushPlanInfo);
                    return new Result<Boolean>().ok(save);
                } else {
                    throw new IException("该学生不属于这个课程");
                }

            }
        }
        return new Result<Boolean>().ok(false);
    }

    @Override
    public Result<PageData<StudentPushPlanInfoVo>> studentPushPlanInfoSelectAPI_005(Condition<StudentPushPlanParamVo> condition) {
        try{
            StudentPushPlanParamVo studentPushPlanParamVo = condition.getCondition();
            QueryPage queryPage = condition.getPage();
            Page<StudentPushPlanInfo> page = new Page<StudentPushPlanInfo>()
                    .setCurrent(queryPage.getCurrent())
                    .setSize(queryPage.getSize());

            List<SysStudentInfo> sysStudentInfoList = studentInfoService.list(new QueryWrapper<SysStudentInfo>()
                    .lambda()
                    .eq(SysStudentInfo::getIsDeleted, IS_DELETED)
                    .eq(SysStudentInfo::getId, studentPushPlanParamVo.getUserId()));

            if (sysStudentInfoList.size() == 0 || Objects.isNull(sysStudentInfoList.get(0))){
                throw new IException("用户为空");
            }
            List<TeacherPushPlanInfo> teacherPushPlanInfoList = teacherPushPlanInfoService.list(new QueryWrapper<TeacherPushPlanInfo>()
                    .lambda()
                    .eq(TeacherPushPlanInfo::getIsDeleted, IS_DELETED)
                    .like(TeacherPushPlanInfo::getName, studentPushPlanParamVo.getBelongsPlanString()));

            Set<Long> teacherPlanIdSet = teacherPushPlanInfoList.stream().map(TeacherPushPlanInfo::getId).collect(Collectors.toSet());
            Map<Long, String> collect = teacherPushPlanInfoList.stream().collect(Collectors.toMap(
                    TeacherPushPlanInfo::getId,
                    TeacherPushPlanInfo::getName,
                    (pre, next) -> next
            ));
            SysStudentInfo sysStudentInfo = sysStudentInfoList.get(0);
            if (Objects.nonNull(studentPushPlanParamVo.getBelongsPlanString())){

                if (teacherPushPlanInfoList.size() > 0){
                    QueryWrapper<StudentPushPlanInfo> studentPushPlanInfoQueryWrapper = new QueryWrapper<>();
                    studentPushPlanInfoQueryWrapper
                            .lambda()
                            .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED)
                            .eq(StudentPushPlanInfo::getUserId, studentPushPlanParamVo.getUserId())
                            .in(StudentPushPlanInfo::getBelongsPlanId, teacherPlanIdSet)
                            .like(StudentPushPlanInfo::getPlanName, studentPushPlanParamVo.getPlanName())
                            .eq(StudentPushPlanInfo::getIfAssess, studentPushPlanParamVo.getIfAssess())
                            .orderByDesc(StudentPushPlanInfo::getCreateTime);

                    studentPushPlanInfoService.page(page, studentPushPlanInfoQueryWrapper);
                    if (page.getRecords().size() == 0){
                        return new Result<PageData<StudentPushPlanInfoVo>>().ok(new PageData<>(new ArrayList<>(), 0L));
                    }
                    List<StudentPushPlanInfoVo> result = new ArrayList<>();
                    page.getRecords().forEach(studentPushPlanInfo -> {
                        StudentPushPlanInfoVo studentPushPlanInfoVo = ConvertUtils.sourceToTarget(studentPushPlanInfo, StudentPushPlanInfoVo.class);
                        if (Objects.nonNull(studentPushPlanInfoVo)){
                            studentPushPlanInfoVo.setStuId(sysStudentInfo.getStdId());
                            studentPushPlanInfoVo.setUclass(sysStudentInfo.getUgrade() + sysStudentInfo.getUmajor() + sysStudentInfo.getUclass());
                            studentPushPlanInfoVo.setName(sysStudentInfo.getName());
                            studentPushPlanInfoVo.setPlanRealName(collect.get(studentPushPlanInfo.getBelongsPlanId()));
                            result.add(studentPushPlanInfoVo);
                        }
                    });
                    return new Result<PageData<StudentPushPlanInfoVo>>().ok(new PageData<>(result, page.getTotal()));
                }
            }
            else {
                QueryWrapper<StudentPushPlanInfo> studentPushPlanInfoQueryWrapper = new QueryWrapper<>();
                studentPushPlanInfoQueryWrapper
                        .lambda()
                        .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED)
                        .eq(StudentPushPlanInfo::getUserId, studentPushPlanParamVo.getUserId())
                        .like(StudentPushPlanInfo::getPlanName, studentPushPlanParamVo.getPlanName())
                        .eq(StudentPushPlanInfo::getIfAssess, studentPushPlanParamVo.getIfAssess())
                        .orderByDesc(StudentPushPlanInfo::getCreateTime);

                studentPushPlanInfoService.page(page, studentPushPlanInfoQueryWrapper);
                if (page.getRecords().size() == 0){
                    return new Result<PageData<StudentPushPlanInfoVo>>().ok(new PageData<>(new ArrayList<>(), 0L));
                }
                List<StudentPushPlanInfoVo> result = new ArrayList<>();
                page.getRecords().forEach(studentPushPlanInfo -> {
                    StudentPushPlanInfoVo studentPushPlanInfoVo = ConvertUtils.sourceToTarget(studentPushPlanInfo, StudentPushPlanInfoVo.class);
                    if (Objects.nonNull(studentPushPlanInfoVo)){
                        studentPushPlanInfoVo.setStuId(sysStudentInfo.getStdId());
                        studentPushPlanInfoVo.setUclass(sysStudentInfo.getUgrade() + sysStudentInfo.getUmajor() + sysStudentInfo.getUclass());
                        studentPushPlanInfoVo.setName(sysStudentInfo.getName());studentPushPlanInfoVo.setPlanRealName(collect.get(studentPushPlanInfo.getBelongsPlanId()));

                        result.add(studentPushPlanInfoVo);
                    }
                });
                return new Result<PageData<StudentPushPlanInfoVo>>().ok(new PageData<>(result, page.getTotal()));
            }

        }catch (Exception e){
            throw new IException("查询提交实习报告错误");
        }
        return null;
    }

    @Override
    public Result<PassPercent> studentPushPlanInfoSelectAPI_006(Long id) {
        List<StudentPushPlanInfo> studentPushPlanInfoList = studentPushPlanInfoService.list(new QueryWrapper<StudentPushPlanInfo>()
                .lambda()
                .eq(StudentPushPlanInfo::getUserId, id)
                .eq(StudentPushPlanInfo::getIfAssess, ASSESS)
                .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED));
        //报告总数
        int allNum = studentPushPlanInfoList.size();

        //及格报告
        int passNum = (int) studentPushPlanInfoList.stream()
                .filter(studentPushPlanInfo -> studentPushPlanInfo.getScore() >= SCORE_PASS).count();

        //不及格报告
        int failNum = allNum - passNum;

        PassPercent passPercent = new PassPercent();
        passPercent.setPass(String.valueOf((float) passNum / allNum));
        passPercent.setFail(String.valueOf((float) failNum / allNum));
        return new Result<PassPercent>().ok(passPercent);
    }

    @Override
    public Result<String> studentPushPlanInfoSelectAPI_007(Long id) {
        try {
            // 求出学生所在的班级
            SysStudentInfo studentInfo = studentInfoService.getOne(new QueryWrapper<SysStudentInfo>()
                    .lambda()
                    .eq(SysStudentInfo::getId, id));
            if (Objects.isNull(studentInfo)) {
                throw new IException("学生不存在");
            }
            ClassInfoVo classInfoVo = ConvertUtils.sourceToTarget(studentInfo, ClassInfoVo.class);
            // 本班级实习实践安排
            List<TeacherPushPlanInfo> teacherPushPlanInfoList = teacherPushPlanInfoService.list(new QueryWrapper<TeacherPushPlanInfo>()
                    .lambda()
                    .eq(TeacherPushPlanInfo::getUgrade, classInfoVo.getUgrade())
                    .eq(TeacherPushPlanInfo::getUmajor, classInfoVo.getUmajor())
                    .eq(TeacherPushPlanInfo::getUclass, classInfoVo.getUclass())
                    .eq(TeacherPushPlanInfo::getIsDeleted, IS_DELETED));

            // 本班级实习实践安排id
            List<Long> planIdList = teacherPushPlanInfoList.stream().map(TeacherPushPlanInfo::getId)
                    .distinct()
                    .collect(Collectors.toList());
            // 已提交的实习报告
            List<Long> pushPlanIdList = studentPushPlanInfoService.list(new QueryWrapper<StudentPushPlanInfo>()
                    .lambda()
                    .eq(StudentPushPlanInfo::getUserId, id)
                    .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED))
                    .stream()
                    .map(StudentPushPlanInfo::getBelongsPlanId)
                    .distinct()
                    .collect(Collectors.toList());
            // 未提交的报告
            List<Long> unPushIdList =
                    planIdList.stream().filter(planId -> Objects.equals(pushPlanIdList.contains(planId), false))
                            .collect(Collectors.toList());
            // 老师发布的报告id-报告名字的Map
            Map<Long, String> planIdPlanNameMap = teacherPushPlanInfoList.stream().collect(Collectors.toMap(
                    TeacherPushPlanInfo::getId,
                    TeacherPushPlanInfo::getName,
                    (pre, next) -> next
            ));
            // 未提交的报告名字
            List<String> unPushNameList = new ArrayList<>();
            unPushIdList.forEach(unPushId -> {
                String planName = planIdPlanNameMap.get(unPushId);
                if (Objects.nonNull(planName)) {
                    unPushNameList.add(planName);
                }
            });

            StringJoiner joiner = new StringJoiner("、");
            unPushNameList.forEach(joiner::add);
            return new Result<String>().ok(joiner.toString());
        } catch (Exception ex) {
            throw new IException("查询失败");
        }
    }
}
