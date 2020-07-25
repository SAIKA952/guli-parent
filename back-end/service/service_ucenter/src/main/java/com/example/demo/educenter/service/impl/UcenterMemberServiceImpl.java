package com.example.demo.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.commonutils.JwtUtils;
import com.example.commonutils.MD5;
import com.example.demo.educenter.entity.UcenterMember;
import com.example.demo.educenter.entity.vo.RegisterVo;
import com.example.demo.educenter.mapper.UcenterMemberMapper;
import com.example.demo.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.exceptionhandler.GuliException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-01
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 登录的方法
    @Override
    public String login(UcenterMember member) {
        // 获取登录的手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();

        // 判断手机号和密码是否为空
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "登录失败，手机号或密码为空");
        }

        // 判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        // 判断查询的对象是否为空
        if(mobileMember == null) {
            // 没有这个手机号
            throw new GuliException(20001, "登录失败，没有这个手机号");
        }

        // 判断密码
        // 因为我们存储到数据库中的密码是经过MD5加密后的密码，
        // 所以我们需要将输入的密码也进行加密，再进行比较
        // MD5加密的特点：只能加密，不能解密
        if(!MD5.encrypt(password).equals(mobileMember.getPassword())) {
            // 密码错误
            throw new GuliException(20001, "登录失败，密码错误");
        }

        // 判断用户是否被禁用
        if(mobileMember.getIsDisabled()) {
            // 用户被禁用
            throw new GuliException(20001, "登录失败，该用户已被禁用");
        }

        // 登录成功
        // 使用jwt工具类，根据id和昵称生成token字符串
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return jwtToken;
    }

    // 注册的方法
    @Override
    public void register(RegisterVo registerVo) {
        // 获取注册的数据
        String code = registerVo.getCode(); // 获取验证码
        String mobile = registerVo.getMobile(); // 获取手机号
        String nickname = registerVo.getNickname(); // 获取昵称
        String password = registerVo.getPassword(); // 获取密码

        // 非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(nickname) || StringUtils.isEmpty(code)) {
            throw new GuliException(20001, "注册失败，有空值");
        }

        // 判断验证码是否正确
        // 获取redis里的验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode)) {
            throw new GuliException(20001, "注册失败，验证码不正确");
        }

        // 判断手机号是否重复，如果表里存在相同的手机号，就不进行添加
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0) {
            throw new GuliException(20001, "注册失败，手机号已注册");
        }

        // 添加到数据库中
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false); // 用户不禁用
        member.setAvatar("https://edu-952.oss-cn-hangzhou.aliyuncs.com/2020/06/20/db8c5a9637f44509b5bb6eba70f4f809file.png"); // 设置默认头像
        baseMapper.insert(member);
    }

    // 根据openid查询
    @Override
    public UcenterMember getMemberByOpenId(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    // 查询某一天的注册人数
    @Override
    public Integer countRegister(String day) {
        Integer count = baseMapper.countRegister(day);
        return count;
    }
}
