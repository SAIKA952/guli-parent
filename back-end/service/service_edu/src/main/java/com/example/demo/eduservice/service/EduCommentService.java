package com.example.demo.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-05
 */
public interface EduCommentService extends IService<EduComment> {
    Map<String, Object> getCourseCommentList(Page<EduComment> pageComment, EduComment eduComment, String courseId);
    // 分页查询课程评论
}
