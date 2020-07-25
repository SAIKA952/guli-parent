package com.example.demo.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.eduservice.entity.comment.CourseCommentVo;
import com.example.demo.eduservice.entity.frontvo.CourseFrontVo;
import com.example.demo.eduservice.entity.frontvo.CourseWebVo;
import com.example.demo.eduservice.entity.vo.CourseInfoVo;
import com.example.demo.eduservice.entity.vo.CoursePublishVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-06-22
 */
public interface EduCourseService extends IService<EduCourse> {

    //添加课程基本信息的方法
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程查询课程基本信息
    CourseInfoVo getCourseInfo(String courseId);

    //修改课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id查询课程确认信息
    CoursePublishVo publishCourseInfo(String id);

    //删除课程
    void removeCourse(String courseId);

    List<EduCourse> getList();

    // 条件查询带分页的查询功能
    Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo);

    // 根据课程id查询课程信息
    CourseWebVo getBaseCourseInfo(String courseId);

    // 根据课程名查询课程（模糊查询）
    List<EduCourse> getCourseByNameVague(String name);

    // 分页查询课程评论的方法
}
