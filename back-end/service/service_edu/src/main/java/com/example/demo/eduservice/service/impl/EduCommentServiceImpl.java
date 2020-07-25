package com.example.demo.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.eduservice.client.UcenterClient;
import com.example.demo.eduservice.entity.EduComment;
import com.example.demo.eduservice.entity.EduCourse;
import com.example.demo.eduservice.entity.EduTeacher;
import com.example.demo.eduservice.mapper.EduCommentMapper;
import com.example.demo.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-05
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private UcenterClient ucenterClient;

    // 分页查询课程评论
    @Override
    public Map<String, Object> getCourseCommentList(Page<EduComment> pageComment, EduComment eduComment, String courseId) {
        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.orderByDesc("gmt_create");
        baseMapper.selectPage(pageComment, wrapper);

        List<EduComment> records = pageComment.getRecords();
        long current = pageComment.getCurrent();
        long pages = pageComment.getPages();
        long size = pageComment.getSize();
        long total = pageComment.getTotal();
        boolean hasNext = pageComment.hasNext(); // 是否有下一页
        boolean hasPrevious = pageComment.hasPrevious(); // 是否有上一页

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
}
