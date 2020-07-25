package com.example.demo.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.JwtUtils;
import com.example.commonutils.R;
import com.example.demo.eduservice.client.UcenterClient;
import com.example.demo.eduservice.entity.EduComment;
import com.example.demo.eduservice.entity.EduCourse;
import com.example.demo.eduservice.service.EduCommentService;
import com.example.demo.eduservice.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-05
 */
@RestController
@RequestMapping("/eduservice/educomment")
//@CrossOrigin
public class EduCommentController {

    @Autowired
    private EduCommentService commentService;

    @Autowired
    private UcenterClient ucenterClient;

    @Autowired
    private EduCourseService courseService;

    // 分页查询课程评论
    @PostMapping("/getCourseComment/{current}/{limit}/{courseId}")
    public R getCourseComment(@PathVariable long current, @PathVariable long limit, @PathVariable String courseId,
                              @RequestParam(required = false) EduComment eduComment) {
        Page<EduComment> pageComment = new Page<>(current, limit);
        Map<String, Object> map = commentService.getCourseCommentList(pageComment, eduComment, courseId);
        return R.ok().data(map);
    }

    // 添加评论的功能
    @PostMapping("/addCourseComment/{courseId}/{content}")
    public R addCourseComment(@PathVariable String courseId,@PathVariable String content, HttpServletRequest request) {
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(userId == "") {
            return R.error().code(20000).message("请先登录");
        }

        EduComment user = ucenterClient.getUserInfo(userId);
        EduComment comment = new EduComment();

        BeanUtils.copyProperties(user, comment);

        EduCourse course = courseService.getById(courseId);
        comment.setCourseId(courseId);
        comment.setTeacherId(course.getTeacherId());
        comment.setContent(content);

        System.out.println("final:" + comment);
        commentService.save(comment);
        return R.ok().data("comment", comment);
    }
}

