package com.example.demo.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.R;
import com.example.demo.eduservice.entity.EduCourse;
import com.example.demo.eduservice.entity.EduTeacher;
import com.example.demo.eduservice.entity.vo.CourseInfoVo;
import com.example.demo.eduservice.entity.vo.CoursePublishVo;
import com.example.demo.eduservice.entity.vo.CourseQuery;
import com.example.demo.eduservice.entity.vo.TeacherQuery;
import com.example.demo.eduservice.service.EduCourseService;
import com.example.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-22
 */
@RestController
@RequestMapping("/eduservice/course")
//@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    //课程列表
    // TODO 条件查询带分页
    @PostMapping("/pageCourseCondition/{current}/{limit}")
    public R pageCourseCondition(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) CourseQuery courseQuery){
        //创建page对象
        Page<EduCourse> pageCourse = new Page<>(current,limit);

        //构造条件，多条件组合查询
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断条件值是否为空，如果不为空就拼接条件。有点类似mybatis里学的动态sql
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();
        //如果名字不为空，进行模糊查询
        if(!StringUtils.isEmpty(title)){ wrapper.like("title", title); }
        if(!StringUtils.isEmpty(status)){ wrapper.eq("status", status); }

        //调用方法
        courseService.page(pageCourse, wrapper);

        //获取总记录数
        long total = pageCourse.getTotal();

        //每页数据的list集合
        List<EduCourse> records = pageCourse.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }

//    //课程列表
//    // TODO 条件查询带分页
//    @GetMapping("/{current}/{limit}")
//    public R getCourse(@PathVariable long current, @PathVariable long limit){
//        //创建page对象
//        Page<EduCourse> pageCourse = new Page<>(current,limit);
//        courseService.page(pageCourse,null);
//        long total = pageCourse.getTotal();
//        //每页数据的list集合
//        List<EduCourse> records = pageCourse.getRecords();
//        return R.ok().data("total",total).data("list",records);
//    }


    //添加课程基本信息的方法
    @PostMapping("/addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    //根据课程查询课程基本信息
    @GetMapping("/getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo", courseInfoVo);
    }

    //修改课程信息
    @PostMapping("/updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程id查询课程确认信息
    @GetMapping("/getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo =  courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse", coursePublishVo);
    }

    //课程最终发布
    //修改课程状态
    @PostMapping("/publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal"); // 设置normal为已发布状态
        courseService.updateById(eduCourse);
        return R.ok();
    }

    //删除课程
    @DeleteMapping("/{courseId}")
    public R deleteCourse(@PathVariable String courseId) {
        courseService.removeCourse(courseId);
        return R.ok();
    }
}

