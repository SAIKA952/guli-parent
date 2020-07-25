package com.example.demo.educenter.controller;


import com.example.commonutils.JwtUtils;
import com.example.commonutils.R;
import com.example.commonutils.ordervo.UcenterMemberOrder;
import com.example.demo.educenter.entity.EduComment;
import com.example.demo.educenter.entity.UcenterMember;
import com.example.demo.educenter.entity.vo.RegisterVo;
import com.example.demo.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-01
 */
@RestController
@RequestMapping("/educenter/member")
//@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    // 登录
    @PostMapping("/login")
    public R loginUser(@RequestBody UcenterMember member) {
        // 调用service方法实现登录
        // 做单点登录，返回token值，使用jwt生成
        String token = memberService.login(member);
        System.out.println(token);
        return R.ok().data("token", token);
    }


    // 注册
    @PostMapping("/register")
    public R registerUser(@RequestBody RegisterVo registerVo) {
        memberService.register(registerVo);
        return R.ok();
    }

    // 根据token获取用户信息
    @GetMapping("/getMemberInfo")
    public R getMemberInfo(HttpServletRequest request) {
        // 调用jwt工具类的方法，根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        // 查询数据库，根据用户id获取用户信息
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("userInfo", member);
    }

    // 根据用户id获取用户信息
    @PostMapping("/getUserInfoOrder/{userId}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String userId) {
        UcenterMember member = memberService.getById(userId);
        // 把member对象里的值复制给UcenterMemberOrder对象
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member, ucenterMemberOrder);
        return ucenterMemberOrder;
    }


    @GetMapping("/getUserInfo/{userId}")
    public EduComment getUserInfo(@PathVariable String userId) {
        UcenterMember member = memberService.getById(userId);
        EduComment eduComment = new EduComment();
        eduComment.setAvatar(member.getAvatar());
        eduComment.setMemberId(member.getId());
        eduComment.setNickname(member.getNickname());

        return eduComment;
    }

    // 查询某一天的注册人数
    @GetMapping("/countRegister/{day}")
    public R countRegister(@PathVariable String day){
        Integer count = memberService.countRegister(day);
        return R.ok().data("count", count);
    }
}

