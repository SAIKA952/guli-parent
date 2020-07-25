package com.example.demo.eduservice.controller;

import com.example.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
//@CrossOrigin //解决跨域问题
public class EduLoginController {
    //login
    @PostMapping("/login")
    public R login(){
        return R.ok().data("token", "admin");
    }

    //info
    @GetMapping("/info")
    public R info(){
        return R.ok().data("roles","[admin]").data("name", "admin").data("avatar","\n" + "https://edu-952.oss-cn-hangzhou.aliyuncs.com/2020/06/20/79792801_p0.jpg");
    }
}
