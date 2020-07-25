package com.example.demo.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.commonutils.R;
import com.example.demo.eduservice.entity.EduCourse;
import com.example.demo.eduservice.entity.EduTeacher;
import com.example.demo.eduservice.service.EduCourseService;
import com.example.demo.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
//@CrossOrigin
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    // 查询前8条热门课程，查询前4条名师
    @GetMapping("/index")
    public R index() {
        // 查询前8条热门课程
        List<EduCourse> courseList = courseService.getList();

        // 查询前4条名师
        List<EduTeacher> teacherList = teacherService.getList();
        return R.ok().data("courseList", courseList).data("teacherList", teacherList);
    }
}
