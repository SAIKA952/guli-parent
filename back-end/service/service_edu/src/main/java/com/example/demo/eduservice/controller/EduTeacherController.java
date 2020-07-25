package com.example.demo.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.R;
import com.example.demo.eduservice.entity.EduTeacher;
import com.example.demo.eduservice.entity.vo.TeacherQuery;
import com.example.demo.eduservice.service.EduTeacherService;
import com.example.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-16
 */
@Api(description="讲师管理")
@RestController
@RequestMapping("/eduservice/edu-teacher")
//@CrossOrigin //解决跨域问题
public class EduTeacherController {

    //把service注入
    @Autowired
    private EduTeacherService eduTeacherService;

    //1、查询讲师表所有数据
    //使用rest风格
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("/findAll")
    public R findAll(){
        //调用service里的方法
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

    //2、逻辑删除讲师方法
    //id通过路径传递，@PathVariable获取路径中的id
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("/{id}")
    public R removeById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable  String id){
        boolean flag = eduTeacherService.removeById(id);
        if(flag){
            return R.ok();
        }else{
            return R.error();
        }
    }

    /**
     * 3、讲师分页查询方法
     * @param current 当前页
     * @param limit 每页显示记录数
     * @return
     */
    @GetMapping("/pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,
                             @PathVariable long limit){
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);

        //调用方法实现分页
        //在调用方法的时候，底层会进行封装，将我们分页的所有数据封装到pageTeacher对象里
        eduTeacherService.page(pageTeacher, null);

        try {
            int i = 10 / 0;
        }catch (Exception e){
            //执行自定义异常。直接传入值
            throw new GuliException(20001,"执行了自定义异常处理...");
        }

        //获取总记录数
        long total = pageTeacher.getTotal();

        //每页数据的list集合
        List<EduTeacher> records = pageTeacher.getRecords();

//        两种返回结果的方式
//        1、
//        Map map = new HashMap();
//        map.put("total", total);
//        map.put("rows", records);
//        return R.ok().data(map);
//        2、
        return R.ok().data("total",total).data("rows",records);
    }

    //4、条件查询带分页的方法
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);

        //构造条件，多条件组合查询
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //判断条件值是否为空，如果不为空就拼接条件。有点类似mybatis里学的动态sql
        String name = teacherQuery.getName();
        String end = teacherQuery.getEnd();
        String begin = teacherQuery.getBegin();
        Integer level = teacherQuery.getLevel();
        //如果名字不为空，进行模糊查询
        if(!StringUtils.isEmpty(name)){ wrapper.like("name", name); }
        if(!StringUtils.isEmpty(end)){ wrapper.le("gmt_modified", end); }
        if(!StringUtils.isEmpty(begin)){ wrapper.ge("gmt_create", begin); }
        if(!StringUtils.isEmpty(level)){ wrapper.eq("level", level); }

        //按时间排序，让新加的数据显示在第一行
        wrapper.orderByDesc("gmt_create");

        //调用方法
        eduTeacherService.page(pageTeacher, wrapper);

        //获取总记录数
        long total = pageTeacher.getTotal();

        //每页数据的list集合
        List<EduTeacher> records = pageTeacher.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }

    //5、添加讲师接口的方法
    @PostMapping("/addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        if (save){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //6、根据讲师id进行查询
    @GetMapping("/getTeacher/{id}")
    public R getTeacher(@PathVariable String id){
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("teacher", eduTeacher);
    }

    //7、讲师修改
    @PostMapping("/updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean b = eduTeacherService.updateById(eduTeacher);
        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }

}

