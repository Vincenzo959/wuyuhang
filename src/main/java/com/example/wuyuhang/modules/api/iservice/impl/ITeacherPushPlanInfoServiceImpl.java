package com.example.wuyuhang.modules.api.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wuyuhang.modules.api.entity.StudentPushPlanInfo;
import com.example.wuyuhang.modules.api.entity.SysStudentInfo;
import com.example.wuyuhang.modules.api.entity.SysTeacherInfo;
import com.example.wuyuhang.modules.api.entity.TeacherPushPlanInfo;
import com.example.wuyuhang.modules.api.iservice.ITeacherPushPlanInfoService;
import com.example.wuyuhang.modules.api.service.StudentPushPlanInfoService;
import com.example.wuyuhang.modules.api.service.SysStudentInfoService;
import com.example.wuyuhang.modules.api.service.SysTeacherInfoService;
import com.example.wuyuhang.modules.api.service.TeacherPushPlanInfoService;
import com.example.wuyuhang.modules.api.util.*;
import com.example.wuyuhang.modules.api.vo.*;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 卓佳伟
 * @date 2024/3/25 17:25
 * description
 */
@Service
@Transactional
@Slf4j
public class ITeacherPushPlanInfoServiceImpl implements ITeacherPushPlanInfoService {

    private final static Integer SCORE_PASS = 60;
    private final static Integer IS_DELETED = 0;
    private final static Integer ACCESS = 1;
    private final static Integer UN_ACCESS = 0;

    @Autowired
    private SysTeacherInfoService sysTeacherInfoService;

    @Autowired
    private SysStudentInfoService sysStudentInfoService;

    @Autowired
    private TeacherPushPlanInfoService teacherPushPlanInfoService;

    @Autowired
    private StudentPushPlanInfoService studentPushPlanInfoService;

    @Override
    public Result<TeacherDataShowVo> teacherPushPlanInfoSelectAPI_001(Long tid) {

        TeacherDataShowVo teacherDataShowVo = new TeacherDataShowVo();
        Integer fAllThing = 0;
        Integer fUnPush = 0;
        Integer fPush = 0;
        Integer fUnScore = 0;
        Integer fScore = 0;


        List<TeacherPushPlanInfo> teacherPushPlanInfoList =
                teacherPushPlanInfoService.list(new QueryWrapper<TeacherPushPlanInfo>()
                        .lambda()
                        .eq(TeacherPushPlanInfo::getGuideTeacher, tid)
                        .eq(TeacherPushPlanInfo::getIsDeleted, IS_DELETED));


        // 老师发布的所有的实习报告
        teacherDataShowVo.setAllKinds(teacherPushPlanInfoList.size());

        //
        for (TeacherPushPlanInfo teacherPushPlanInfo : teacherPushPlanInfoList) {
            String umajor = teacherPushPlanInfo.getUmajor();
            String uclass = teacherPushPlanInfo.getUclass();
            String ugrade = teacherPushPlanInfo.getUgrade();
            Long id = teacherPushPlanInfo.getId();

            // 这个班的学生id
            List<Long> studentId = sysStudentInfoService.list(new QueryWrapper<SysStudentInfo>().lambda()
                    .eq(SysStudentInfo::getIsDeleted, IS_DELETED)
                    .eq(SysStudentInfo::getUmajor, umajor)
                    .eq(SysStudentInfo::getUclass, uclass)
                    .eq(SysStudentInfo::getUgrade, ugrade)).stream().map(SysStudentInfo::getId).collect(Collectors.toList());

            // 这个班的人数
            Integer aClassNum = studentId.size();
            fAllThing += aClassNum;

            // 交了的是那些有记录且是这个班且是这份报告的
            Integer push = studentPushPlanInfoService.list(new QueryWrapper<StudentPushPlanInfo>().lambda()
                    .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED)
                    .in(StudentPushPlanInfo::getUserId, studentId)
                    .eq(StudentPushPlanInfo::getBelongsPlanId, id)).size();
            fPush += push;

            Integer unPush = aClassNum - push;
            fUnPush += unPush;

            Integer score = studentPushPlanInfoService.list(new QueryWrapper<StudentPushPlanInfo>().lambda()
                    .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED)
                    .in(StudentPushPlanInfo::getUserId, studentId)
                    .eq(StudentPushPlanInfo::getBelongsPlanId, id)
                    .eq(StudentPushPlanInfo::getIfAssess, 1)).size();
            fScore += score;

            Integer unScore = push - score;
            fUnScore += unScore;

        }
        teacherDataShowVo.setAllThing(fAllThing);
        teacherDataShowVo.setUnPush(fUnPush);
        teacherDataShowVo.setPush(fPush);
        teacherDataShowVo.setUnScore(fUnScore);
        teacherDataShowVo.setScore(fScore);
        return new Result<TeacherDataShowVo>().ok(teacherDataShowVo);
    }

    @Override
    public Result<List<TeacherDataShowChartsVo>> studentPushPlanInfoSelectAPI_002(String name) {
        try {
            TeacherPushPlanInfo planInfo = teacherPushPlanInfoService.getOne(new QueryWrapper<TeacherPushPlanInfo>()
                    .lambda()
                    .eq(TeacherPushPlanInfo::getIsDeleted, IS_DELETED)
                    .like(TeacherPushPlanInfo::getName, name));

            if (Objects.nonNull(planInfo)) {
                // 这个班的学生id
                List<Long> studentId = sysStudentInfoService.list(new QueryWrapper<SysStudentInfo>().lambda()
                        .eq(SysStudentInfo::getIsDeleted, IS_DELETED)
                        .eq(SysStudentInfo::getUmajor, planInfo.getUmajor())
                        .eq(SysStudentInfo::getUclass, planInfo.getUclass())
                        .eq(SysStudentInfo::getUgrade, planInfo.getUgrade())).stream().map(SysStudentInfo::getId).collect(Collectors.toList());


                // 交了的是那些有记录且是这个班且是这份报告的
                List<StudentPushPlanInfo> passList = studentPushPlanInfoService.list(new QueryWrapper<StudentPushPlanInfo>().lambda()
                        .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED)
                        .in(StudentPushPlanInfo::getUserId, studentId)
                        .eq(StudentPushPlanInfo::getBelongsPlanId, planInfo.getId())
                        .gt(StudentPushPlanInfo::getScore, SCORE_PASS));

                List<StudentPushPlanInfo> failList = studentPushPlanInfoService.list(new QueryWrapper<StudentPushPlanInfo>().lambda()
                        .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED)
                        .in(StudentPushPlanInfo::getUserId, studentId)
                        .eq(StudentPushPlanInfo::getBelongsPlanId, planInfo.getId())
                        .lt(StudentPushPlanInfo::getScore, SCORE_PASS));

                Integer passNum = passList.size();
                Integer failNum = failList.size();
                Integer pushNum = passList.size() + failList.size();

                List<TeacherDataShowChartsVo> teacherDataShowChartsVoList = new ArrayList<>();
                TeacherDataShowChartsVo teacherDataShowChartsVo = new TeacherDataShowChartsVo();
                StringJoiner joiner = new StringJoiner("-");
                List<String> classInfo = new ArrayList<>();
                classInfo.add(planInfo.getUgrade());
                classInfo.add(planInfo.getUmajor());
                classInfo.add(planInfo.getUclass());
                classInfo.forEach(joiner::add);
                teacherDataShowChartsVo.setName(joiner.toString());
                teacherDataShowChartsVo.setPassNum(passNum);
                teacherDataShowChartsVo.setFailNum(failNum);
                teacherDataShowChartsVo.setPushNum(pushNum);
                teacherDataShowChartsVoList.add(teacherDataShowChartsVo);
                return new Result<List<TeacherDataShowChartsVo>>().ok(teacherDataShowChartsVoList);
            }
        } catch (Exception ex) {
            throw new IException("查询失败");
        }
        throw new IException("查询失败");
    }

    @Override
    public Result<List<TeacherDataShowChartsVo>> studentPushPlanInfoSelectAPI_003(String name) {
        List<TeacherDataShowChartsVo> teacherDataShowChartsVoList = new ArrayList<>();
        // 0-年级 1-专业 2-班级
        List<String> majorGradeClass = Arrays.asList(name.split("-"));
        if (majorGradeClass.size() != 3 || Objects.isNull(majorGradeClass.get(0)) ||
                Objects.isNull(majorGradeClass.get(1)) || Objects.isNull(majorGradeClass.get(2))) {
            throw new IException("教师数据可视化界面参数传错！");
        }
        List<TeacherPushPlanInfo> teacherPushPlanInfoList = teacherPushPlanInfoService.list(new QueryWrapper<TeacherPushPlanInfo>()
                .lambda()
                .eq(TeacherPushPlanInfo::getIsDeleted, IS_DELETED)
                .eq(TeacherPushPlanInfo::getUgrade, majorGradeClass.get(0))
                .eq(TeacherPushPlanInfo::getUmajor, majorGradeClass.get(1))
                .eq(TeacherPushPlanInfo::getUclass, majorGradeClass.get(2)));

        teacherPushPlanInfoList.forEach(teacherPushPlanInfo -> {
            TeacherDataShowChartsVo teacherDataShowChartsVo = new TeacherDataShowChartsVo();
            teacherDataShowChartsVo.setName(teacherPushPlanInfo.getName());
            long count = studentPushPlanInfoService.count(new QueryWrapper<StudentPushPlanInfo>()
                    .lambda()
                    .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED)
                    .eq(StudentPushPlanInfo::getBelongsPlanId, teacherPushPlanInfo.getId()));
            long passNum = studentPushPlanInfoService.count(new QueryWrapper<StudentPushPlanInfo>()
                    .lambda()
                    .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED)
                    .eq(StudentPushPlanInfo::getBelongsPlanId, teacherPushPlanInfo.getId())
                    .gt(StudentPushPlanInfo::getScore, SCORE_PASS));
            long failNum = count - passNum;
            teacherDataShowChartsVo.setPushNum((int) count);
            teacherDataShowChartsVo.setPassNum((int) passNum);
            teacherDataShowChartsVo.setFailNum((int) failNum);
            teacherDataShowChartsVoList.add(teacherDataShowChartsVo);
        });
        return new Result<List<TeacherDataShowChartsVo>>().ok(teacherDataShowChartsVoList);
    }

    @Override
    public Result<String> studentPushPlanInfoSelectAPI_004(Long id) {
        List<String> unScoreList = new ArrayList<>();
        // 查询老师发布了几种报告
        List<TeacherPushPlanInfo> teacherPushPlanInfoList = teacherPushPlanInfoService.list(new QueryWrapper<TeacherPushPlanInfo>()
                .lambda()
                .eq(TeacherPushPlanInfo::getIsDeleted, IS_DELETED)
                .eq(TeacherPushPlanInfo::getGuideTeacher, id));

        Set<Long> teacherPushPlanInfoIdSet = teacherPushPlanInfoList.stream().map(TeacherPushPlanInfo::getId).collect(Collectors.toSet());

        //在学生提交的报告列表里查询属于该老师发布的报告
        List<StudentPushPlanInfo> studentPushPlanInfoList = studentPushPlanInfoService.list(new QueryWrapper<StudentPushPlanInfo>()
                .lambda()
                .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED)
                .in(StudentPushPlanInfo::getBelongsPlanId, teacherPushPlanInfoIdSet));

        // 获取提交的学生的id的Set
        Set<Long> studentIdSet = studentPushPlanInfoList.stream().map(StudentPushPlanInfo::getUserId).collect(Collectors.toSet());
        // 获取学生id-学生姓名的Map
        Map<Long, String> studentIdStudentNameMap = sysStudentInfoService.list(new QueryWrapper<SysStudentInfo>()
                .lambda()
                .eq(SysStudentInfo::getIsDeleted, IS_DELETED)
                .in(SysStudentInfo::getId, studentIdSet))
                .stream()
                .collect(Collectors.toMap(
                        SysStudentInfo::getId,
                        SysStudentInfo::getName,
                        (pre, next) -> next
                ));

        studentPushPlanInfoList.forEach(studentPushPlanInfo -> {
            // 遍历学生提交的报告列表，要是有得分为空的，则判定为没评分
            if (Objects.isNull(studentPushPlanInfo.getScore())) {
                // 统计报告名字 返回格式为 学生姓名-报告名字
                StringJoiner joiner = new StringJoiner("-");
                if (Objects.nonNull(studentPushPlanInfo.getUserId())) {
                    joiner.add(studentIdStudentNameMap.get(studentPushPlanInfo.getUserId()));
                }
                Map<Long, String> planIdNameMap = teacherPushPlanInfoList.stream().collect(Collectors.toMap(
                        TeacherPushPlanInfo::getId,
                        TeacherPushPlanInfo::getName,
                        (pre, next) -> next
                ));
                if (Objects.nonNull(studentPushPlanInfo.getBelongsPlanId()) &&
                        Objects.nonNull(planIdNameMap.get(studentPushPlanInfo.getBelongsPlanId()))) {
                    String planName = planIdNameMap.get(studentPushPlanInfo.getBelongsPlanId());
                    unScoreList.add(joiner.add(planName).toString());
                }
            }
        });
        StringJoiner joiner = new StringJoiner("、");
        unScoreList.forEach(joiner::add);
        return new Result<String>().ok(joiner.toString());
    }

    @Override
    public Result<Boolean> studentPushPlanInfoSelectAPI_005(TeacherPushPlanInfoVo teacherPushPlanInfoVO) {
        TeacherPushPlanInfo teacherPushPlanInfo = ConvertUtils.sourceToTarget(teacherPushPlanInfoVO, TeacherPushPlanInfo.class);
        String teachName = teacherPushPlanInfoVO.getGuideTeacherString();
        SysTeacherInfo teacherInfo = sysTeacherInfoService.getOne(new QueryWrapper<SysTeacherInfo>()
                .lambda()
                .eq(SysTeacherInfo::getIsDeleted, IS_DELETED)
                .eq(SysTeacherInfo::getName, teachName));
        if (Objects.isNull(teacherInfo)){
            throw new IException("用户查询失败");
        }
        teacherPushPlanInfo.setGuideTeacher(teacherInfo.getId());
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        teacherPushPlanInfo.setBeginTime(LocalDateTime.now());
        teacherPushPlanInfo.setEndTime(LocalDateTime.now().plusMonths(1));
        teacherPushPlanInfo.setIsDeleted(0);
        teacherPushPlanInfo.setCreateTime(LocalDateTime.now());
        teacherPushPlanInfoService.save(teacherPushPlanInfo);
        return new Result<Boolean>().ok(true);
    }

    @Override
    public Result<PageData<StudentPushPlanInfoVo>> studentPushPlanInfoSelectAPI_006(Condition<StudentPlanVo> condition) {
        // 查询老师发布了几种报告（课程）
        StudentPlanVo studentPlanVo = condition.getCondition();
        QueryPage queryPage = condition.getPage();

        Page<StudentPushPlanInfo> page = new Page<StudentPushPlanInfo>()
                .setCurrent(queryPage.getCurrent())
                .setSize(queryPage.getSize());

        if (!Strings.isNullOrEmpty(studentPlanVo.getStuId())){
            List<SysStudentInfo> studentInfoList = sysStudentInfoService.list(new QueryWrapper<SysStudentInfo>()
                    .lambda()
                    .eq(SysStudentInfo::getStdId, studentPlanVo.getStuId())
                    .eq(SysStudentInfo::getIsDeleted, IS_DELETED));
            if (studentInfoList.size() == 0){
                throw new IException("用户为空");
            }
            Map<Long, SysStudentInfo> studentIdStudentInfoMap = studentInfoList.stream().collect(Collectors.toMap(
                    SysStudentInfo::getId,
                    SysStudentInfo -> SysStudentInfo,
                    (pre, next) -> next
            ));


            List<TeacherPushPlanInfo> teacherPushPlanInfoList = teacherPushPlanInfoService.list(new QueryWrapper<TeacherPushPlanInfo>()
                    .lambda()
                    .eq(TeacherPushPlanInfo::getIsDeleted, IS_DELETED)
                    .eq(TeacherPushPlanInfo::getGuideTeacher, studentPlanVo.getTchId())
                    .like(TeacherPushPlanInfo::getPclass, studentPlanVo.getPclass())
                    .like(TeacherPushPlanInfo::getName, studentPlanVo.getPname()));

            Set<Long> teacherPushPlanInfoIdSet = teacherPushPlanInfoList.stream().map(TeacherPushPlanInfo::getId).collect(Collectors.toSet());
            Map<Long, String> collect = teacherPushPlanInfoList.stream().collect(Collectors.toMap(
                    TeacherPushPlanInfo::getId,
                    TeacherPushPlanInfo::getName,
                    (pre, next) -> next
            ));
            // 查询学生(班级、学号)
            List<SysStudentInfo> sysStudentInfoList = sysStudentInfoService.list(new QueryWrapper<SysStudentInfo>()
                    .lambda()
                    .eq(SysStudentInfo::getIsDeleted, IS_DELETED)
                    .like(SysStudentInfo::getUclass, studentPlanVo.getUclass())
                    .eq(SysStudentInfo::getStdId, studentPlanVo.getStuId()));
            Set<Long> studentIdSet = sysStudentInfoList.stream().map(SysStudentInfo::getId).collect(Collectors.toSet());
            QueryWrapper<StudentPushPlanInfo> studentPushPlanInfoQueryWrapper = new QueryWrapper<>();
            studentPushPlanInfoQueryWrapper
                    .lambda()
                    .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED)
                    .in(StudentPushPlanInfo::getBelongsPlanId, teacherPushPlanInfoIdSet)
                    .in(StudentPushPlanInfo::getUserId, studentIdSet)
                    .like(StudentPushPlanInfo::getPlanName, studentPlanVo.getTitle());
            if (studentPlanVo.getIfScore() == 0) {
                studentPushPlanInfoQueryWrapper.lambda().eq(StudentPushPlanInfo::getIfAssess, UN_ACCESS);
                studentPushPlanInfoService.page(page, studentPushPlanInfoQueryWrapper);
                if (page.getRecords().size() == 0){
                    return new Result<PageData<StudentPushPlanInfoVo>>().ok(new PageData<>(new ArrayList<>(), 0L));
                }
                List<StudentPushPlanInfoVo> studentPushPlanInfoList = new ArrayList<>();
                page.getRecords().forEach(studentPushPlanInfo -> {
                    StudentPushPlanInfoVo studentPushPlanInfo1 =  ConvertUtils.sourceToTarget(studentPushPlanInfo, StudentPushPlanInfoVo.class);
                    studentPushPlanInfo1.setStuId(studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getStdId());
                    studentPushPlanInfo1.setName(studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getName());
                    studentPushPlanInfo1.setUclass(
                            studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getUgrade() +
                                    studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getUmajor() +
                                    studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getUclass()
                    );
                    studentPushPlanInfo1.setPlanRealName(collect.get(studentPushPlanInfo.getBelongsPlanId()));
                    studentPushPlanInfoList.add(studentPushPlanInfo1);
                });
                return new Result<PageData<StudentPushPlanInfoVo>>().ok(new PageData<>(studentPushPlanInfoList, page.getTotal()));
            } else {
                studentPushPlanInfoQueryWrapper.lambda().isNotNull(StudentPushPlanInfo::getScore);
                studentPushPlanInfoService.page(page, studentPushPlanInfoQueryWrapper);
                if (page.getRecords().size() == 0){
                    return new Result<PageData<StudentPushPlanInfoVo>>().ok(new PageData<>(new ArrayList<>(), 0L));
                }
                List<StudentPushPlanInfoVo> studentPushPlanInfoList = new ArrayList<>();
                page.getRecords().forEach(studentPushPlanInfo -> {
                    StudentPushPlanInfoVo studentPushPlanInfo1 =  ConvertUtils.sourceToTarget(studentPushPlanInfo, StudentPushPlanInfoVo.class);
                    studentPushPlanInfo1.setStuId(studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getStdId());
                    studentPushPlanInfo1.setName(studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getName());
                    studentPushPlanInfo1.setUclass(
                            studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getUgrade() +
                                    studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getUmajor() +
                                    studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getUclass()
                    );
                    studentPushPlanInfo1.setPlanRealName(collect.get(studentPushPlanInfo.getBelongsPlanId()));
                    studentPushPlanInfoList.add(studentPushPlanInfo1);
                });
                return new Result<PageData<StudentPushPlanInfoVo>>().ok(new PageData<>(studentPushPlanInfoList, page.getTotal()));
            }
        }else {
            List<SysStudentInfo> studentInfoList = sysStudentInfoService.list(new QueryWrapper<SysStudentInfo>()
                    .lambda()
                    .eq(SysStudentInfo::getIsDeleted, IS_DELETED));
            Map<Long, SysStudentInfo> studentIdStudentInfoMap = studentInfoList.stream().collect(Collectors.toMap(
                    SysStudentInfo::getId,
                    SysStudentInfo -> SysStudentInfo,
                    (pre, next) -> next
            ));

            List<TeacherPushPlanInfo> teacherPushPlanInfoList = teacherPushPlanInfoService.list(new QueryWrapper<TeacherPushPlanInfo>()
                    .lambda()
                    .eq(TeacherPushPlanInfo::getIsDeleted, IS_DELETED)
                    .eq(TeacherPushPlanInfo::getGuideTeacher, studentPlanVo.getTchId())
                    .like(TeacherPushPlanInfo::getPclass, studentPlanVo.getPclass())
                    .like(TeacherPushPlanInfo::getName, studentPlanVo.getPname()));

            Set<Long> teacherPushPlanInfoIdSet = teacherPushPlanInfoList.stream().map(TeacherPushPlanInfo::getId).collect(Collectors.toSet());
            Map<Long, String> collect = teacherPushPlanInfoList.stream().collect(Collectors.toMap(
                    TeacherPushPlanInfo::getId,
                    TeacherPushPlanInfo::getName,
                    (pre, next) -> next
            ));
            // 查询学生(班级、学号)
            List<SysStudentInfo> sysStudentInfoList = sysStudentInfoService.list(new QueryWrapper<SysStudentInfo>()
                    .lambda()
                    .eq(SysStudentInfo::getIsDeleted, IS_DELETED)
                    .like(SysStudentInfo::getUclass, studentPlanVo.getUclass()));
            Set<Long> studentIdSet = sysStudentInfoList.stream().map(SysStudentInfo::getId).collect(Collectors.toSet());
            QueryWrapper<StudentPushPlanInfo> studentPushPlanInfoQueryWrapper = new QueryWrapper<>();
            studentPushPlanInfoQueryWrapper
                    .lambda()
                    .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED)
                    .in(StudentPushPlanInfo::getBelongsPlanId, teacherPushPlanInfoIdSet)
                    .in(StudentPushPlanInfo::getUserId, studentIdSet)
                    .like(StudentPushPlanInfo::getPlanName, studentPlanVo.getTitle());
            if (studentPlanVo.getIfScore() == 0) {
                studentPushPlanInfoQueryWrapper.lambda().eq(StudentPushPlanInfo::getIfAssess, UN_ACCESS);
                studentPushPlanInfoService.page(page, studentPushPlanInfoQueryWrapper);
                if (page.getRecords().size() == 0){
                    return new Result<PageData<StudentPushPlanInfoVo>>().ok(new PageData<>(new ArrayList<>(), 0L));
                }
                List<StudentPushPlanInfoVo> studentPushPlanInfoList = new ArrayList<>();
                page.getRecords().forEach(studentPushPlanInfo -> {
                    StudentPushPlanInfoVo studentPushPlanInfo1 =  ConvertUtils.sourceToTarget(studentPushPlanInfo, StudentPushPlanInfoVo.class);
                    studentPushPlanInfo1.setStuId(studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getStdId());
                    studentPushPlanInfo1.setName(studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getName());
                    studentPushPlanInfo1.setUclass(
                            studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getUgrade() +
                                    studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getUmajor() +
                                    studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getUclass()
                    );
                    studentPushPlanInfo1.setPlanRealName(collect.get(studentPushPlanInfo.getBelongsPlanId()));
                    studentPushPlanInfoList.add(studentPushPlanInfo1);
                });
                return new Result<PageData<StudentPushPlanInfoVo>>().ok(new PageData<>(studentPushPlanInfoList, page.getTotal()));
            } else {
                studentPushPlanInfoQueryWrapper.lambda().isNotNull(StudentPushPlanInfo::getScore);
                studentPushPlanInfoService.page(page, studentPushPlanInfoQueryWrapper);
                if (page.getRecords().size() == 0){
                    return new Result<PageData<StudentPushPlanInfoVo>>().ok(new PageData<>(new ArrayList<>(), 0L));
                }
                List<StudentPushPlanInfoVo> studentPushPlanInfoList = new ArrayList<>();
                page.getRecords().forEach(studentPushPlanInfo -> {
                    StudentPushPlanInfoVo studentPushPlanInfo1 =  ConvertUtils.sourceToTarget(studentPushPlanInfo, StudentPushPlanInfoVo.class);
                    studentPushPlanInfo1.setStuId(studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getStdId());
                    studentPushPlanInfo1.setName(studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getName());
                    studentPushPlanInfo1.setUclass(
                            studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getUgrade() +
                                    studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getUmajor() +
                                    studentIdStudentInfoMap.get(studentPushPlanInfo.getUserId()).getUclass()
                    );
                    studentPushPlanInfo1.setPlanRealName(collect.get(studentPushPlanInfo.getBelongsPlanId()));
                    studentPushPlanInfoList.add(studentPushPlanInfo1);
                });
                return new Result<PageData<StudentPushPlanInfoVo>>().ok(new PageData<>(studentPushPlanInfoList, page.getTotal()));
            }
        }
    }

    @Override
    public Result<Boolean> studentPushPlanInfoUpdateAPI_007(AccessVo accessVo) {
        try {
            StudentPushPlanInfo studentPushPlanInfo = studentPushPlanInfoService.getOne(new QueryWrapper<StudentPushPlanInfo>()
                    .lambda()
                    .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED)
                    .eq(StudentPushPlanInfo::getId, accessVo.getPlanId()));
            if (Objects.nonNull(studentPushPlanInfo)) {
                boolean update = studentPushPlanInfoService.update(new UpdateWrapper<StudentPushPlanInfo>()
                        .lambda()
                        .eq(StudentPushPlanInfo::getIsDeleted, IS_DELETED)
                        .eq(StudentPushPlanInfo::getId, accessVo.getPlanId())
                        .set(StudentPushPlanInfo::getScore, accessVo.getScore())
                        .set(StudentPushPlanInfo::getIfAssess, ACCESS));
                return new Result<Boolean>().ok(update);
            } else {
                throw new IException("实验报告查询错误！");
            }
        } catch (Exception ex) {
            throw new IException("评分失败");
        }
    }

    @Override
    public Result<PageData<TeacherPushPlanInfoVo>> teacherPushPlanInfoSelectAPI_008(Condition<TeacherPlanVo> condition) {
        List<TeacherPushPlanInfoVo> teacherPushPlanInfoVoList = new ArrayList<>();
        TeacherPlanVo teacherPlanVo = condition.getCondition();
        QueryPage queryPage = condition.getPage();
        Page<TeacherPushPlanInfo> page = new Page<TeacherPushPlanInfo>()
                .setSize(queryPage.getSize())
                .setCurrent(queryPage.getCurrent());

        try {
            SysTeacherInfo sysTeacherInfo = sysTeacherInfoService.getOne(new QueryWrapper<SysTeacherInfo>()
                    .lambda()
                    .eq(SysTeacherInfo::getIsDeleted, IS_DELETED)
                    .eq(SysTeacherInfo::getId, teacherPlanVo.getId()));

            if (Objects.nonNull(sysTeacherInfo)) {
                String teacherName = sysTeacherInfo.getName();

                QueryWrapper<TeacherPushPlanInfo> teacherPushPlanInfoQueryWrapper = new QueryWrapper<>();
                teacherPushPlanInfoQueryWrapper
                        .lambda()
                        .eq(TeacherPushPlanInfo::getIsDeleted, IS_DELETED)
                        .like(TeacherPushPlanInfo::getName, teacherPlanVo.getName())
                        .eq(TeacherPushPlanInfo::getGuideTeacher, teacherPlanVo.getId())
                        .like(TeacherPushPlanInfo::getPclass, teacherPlanVo.getPclass());

                teacherPushPlanInfoService.page(page, teacherPushPlanInfoQueryWrapper);
                if (page.getRecords().size() == 0) {
                    return new Result<PageData<TeacherPushPlanInfoVo>>().ok(new PageData<>(new ArrayList<>(), 0L));
                }
                page.getRecords().forEach(teacherPushPlanInfo -> {
                    TeacherPushPlanInfoVo teacherPushPlanInfoVo = ConvertUtils.sourceToTarget(teacherPushPlanInfo, TeacherPushPlanInfoVo.class);
                    teacherPushPlanInfoVo.setGuideTeacherString(teacherName);
                    teacherPushPlanInfoVoList.add(teacherPushPlanInfoVo);
                });
                return new Result<PageData<TeacherPushPlanInfoVo>>().ok(new PageData<>(teacherPushPlanInfoVoList, page.getTotal()));
            } else {
                throw new IException("教师不存在");
            }
        } catch (Exception ex) {
            throw new IException("查询失败");
        }
    }

    @Override
    public Result<Boolean> teacherPushPlanInfoUpdateAPI_009(TeacherPlanManagerVo teacherPlanManagerVo) {
        try{
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            TeacherPushPlanInfo oldTeacherPushPlanInfo = teacherPushPlanInfoService.getOne(new QueryWrapper<TeacherPushPlanInfo>()
                    .lambda()
                    .eq(TeacherPushPlanInfo::getIsDeleted, IS_DELETED)
                    .eq(TeacherPushPlanInfo::getId, teacherPlanManagerVo.getId()));
            if (Objects.isNull(oldTeacherPushPlanInfo)){
                throw new IException("未找到实习实践报告");
            }
            boolean update = teacherPushPlanInfoService.update(new UpdateWrapper<TeacherPushPlanInfo>()
                    .lambda()
                    .eq(TeacherPushPlanInfo::getIsDeleted, IS_DELETED)
                    .eq(TeacherPushPlanInfo::getId, teacherPlanManagerVo.getId())
                    .set(TeacherPushPlanInfo::getBeginTime, Objects.nonNull(teacherPlanManagerVo.getBeginTime())?
                            LocalDateTime.parse(teacherPlanManagerVo.getBeginTime(), fmt) : oldTeacherPushPlanInfo.getBeginTime())
                    .set(TeacherPushPlanInfo::getEndTime, Objects.nonNull(teacherPlanManagerVo.getEndTime())?
                            LocalDateTime.parse(teacherPlanManagerVo.getEndTime(), fmt) : oldTeacherPushPlanInfo.getEndTime())
                    .set(TeacherPushPlanInfo::getAddress, Objects.nonNull(teacherPlanManagerVo.getAddress())?
                            teacherPlanManagerVo.getAddress() : oldTeacherPushPlanInfo.getAddress())
                    .set(TeacherPushPlanInfo::getContent, Objects.nonNull(teacherPlanManagerVo.getContent())?
                            teacherPlanManagerVo.getContent() : oldTeacherPushPlanInfo.getContent())
                    .set(TeacherPushPlanInfo::getRemark, Objects.nonNull(teacherPlanManagerVo.getRemark())?
                            teacherPlanManagerVo.getRemark() : oldTeacherPushPlanInfo.getRemark())
                    .set(TeacherPushPlanInfo::getName, Objects.nonNull(teacherPlanManagerVo.getName())?
                            teacherPlanManagerVo.getName() : oldTeacherPushPlanInfo.getName()));
            return new Result<Boolean>().ok(update);
        }catch (Exception e){
            throw new IException("查询教师实习实践报告出问题了");
        }
    }
}
