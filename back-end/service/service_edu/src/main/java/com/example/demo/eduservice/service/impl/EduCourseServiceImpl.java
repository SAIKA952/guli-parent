package com.example.demo.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.eduservice.entity.EduCourse;
import com.example.demo.eduservice.entity.EduCourseDescription;
import com.example.demo.eduservice.entity.EduTeacher;
import com.example.demo.eduservice.entity.comment.CourseCommentVo;
import com.example.demo.eduservice.entity.frontvo.CourseFrontVo;
import com.example.demo.eduservice.entity.frontvo.CourseWebVo;
import com.example.demo.eduservice.entity.vo.CourseInfoVo;
import com.example.demo.eduservice.entity.vo.CoursePublishVo;
import com.example.demo.eduservice.mapper.EduCourseMapper;
import com.example.demo.eduservice.service.EduChapterService;
import com.example.demo.eduservice.service.EduCourseDescriptionService;
import com.example.demo.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.eduservice.service.EduVideoService;
import com.example.exceptionhandler.GuliException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-22
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //课程描述注入
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    //注入小节和章节service
    @Autowired
    private EduVideoService videoService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private EduCourseService courseService;

    //添加课程基本信息的方法
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1、向课程表里添加课程基本信息
        //courseInfoVo转换为eduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int insert = baseMapper.insert(eduCourse); // 返回值是影响行数，就是返回添加了几条数据
        //如果没有成功添加数据就抛出异常
        if(insert <= 0) {
            //添加课程信息失败
            throw new GuliException(20001, "添加课程信息失败");
        }

        //获取添加之后的课程id
        String cid = eduCourse.getId();

        //2、向课程简介表edu_course_description添加课程简介
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        //设置课程描述的id就是课程id
        courseDescription.setId(cid);
        courseDescriptionService.save(courseDescription);

        return cid;
    }

    //根据课程查询课程基本信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        // 1、查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse, courseInfoVo);

        // 2、查询课程描述表
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());

        return courseInfoVo;
    }

    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if(update == 0) {
            throw new GuliException(20001, "修改课程信息失败");
        }

        //修改描述表
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(description);
    }

    // 根据课程id查询课程确认信息
    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        //调用mapper
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    //删除课程
    @Override
    public void removeCourse(String courseId) {
        // 1、根据课程id删除小节
        videoService.removeVideoByCourseId(courseId);

        // 2、根据课程id删除章节
        chapterService.removeChapterByCourseId(courseId);

        // 3、根据课程id删除描述
        courseDescriptionService.removeById(courseId);

        // 4、根据课程id删除课程本身
        int result = baseMapper.deleteById(courseId);

        if(result == 0){
            throw new GuliException(20001, "删除失败");
        }
    }

    @Cacheable(value = "course", key = "'courseList'")
    @Override
    public List<EduCourse> getList() {
        QueryWrapper<EduCourse> wrapperCourse = new QueryWrapper<>();
        wrapperCourse.orderByDesc("id");
        wrapperCourse.last("limit 8");
        List<EduCourse> courseList = courseService.list(wrapperCourse);
        return courseList;
    }

    // 条件查询带分页的查询功能
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageParam, CourseFrontVo courseFrontVo) {
        // 根据讲师id查询所有课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        // 判断条件值是否为空，不为空就拼接条件
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){ // 判断一级分类是否存在
            wrapper.eq("subject_parent_id", courseFrontVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectId())){ // 判断二级分类是否存在
            wrapper.eq("subject_id", courseFrontVo.getSubjectId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){ // 关注度排序
            wrapper.orderByDesc("buy_count");
        }
        if(!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())){ // 按时间排序
            wrapper.orderByDesc("gmt_create");
        }
        if(!StringUtils.isEmpty(courseFrontVo.getPriceSort())){ // 按价格排序
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(pageParam, wrapper);

        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext(); // 是否有下一页
        boolean hasPrevious = pageParam.hasPrevious(); // 是否有上一页

        // 获取分页数据，放到map集合里
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    // 根据课程id查询课程信息
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }

    // 根据课程名查询课程（模糊查询）
    @Override
    public List<EduCourse> getCourseByNameVague(String title) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.like("title", title);
        List<EduCourse> eduCourses = baseMapper.selectList(wrapper);
        return eduCourses;
    }


}
