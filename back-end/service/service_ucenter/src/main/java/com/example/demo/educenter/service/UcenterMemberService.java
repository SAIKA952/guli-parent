package com.example.demo.educenter.service;

import com.example.demo.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-07-01
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    // 登录的方法
    String login(UcenterMember member);

    // 注册的方法
    void register(RegisterVo registerVo);

    // 根据openid查询
    UcenterMember getMemberByOpenId(String openid);

    // 查询某一天的注册人数
    Integer countRegister(String day);
}
