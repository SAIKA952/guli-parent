package com.example.demo.eduservice.mapper;

import com.example.demo.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.eduservice.entity.frontvo.CourseWebVo;
import com.example.demo.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-06-22
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    public CoursePublishVo getPublishCourseInfo(String courseId);

    CourseWebVo getBaseCourseInfo(String courseId);
}
