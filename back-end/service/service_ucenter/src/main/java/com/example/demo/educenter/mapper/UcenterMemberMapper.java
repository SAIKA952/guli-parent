package com.example.demo.educenter.mapper;

import com.example.demo.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-07-01
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    // 查询某一天的注册人数
    Integer countRegister(String day);
}
