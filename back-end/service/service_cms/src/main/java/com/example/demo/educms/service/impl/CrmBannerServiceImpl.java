package com.example.demo.educms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.educms.entity.CrmBanner;
import com.example.demo.educms.mapper.CrmBannerMapper;
import com.example.demo.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-29
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {
    // 查询所有banner
    @Cacheable(value = "banner", key = "'selectIndexList'")
    @Override
    public List<CrmBanner> selectAllBanner() {
        // 根据id进行降序排序，显示排列之后的前两条记录
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        // last方法拼接sql语句
        wrapper.last("limit 2");

        List<CrmBanner> list = baseMapper.selectList(null);
        return list;
    }
}
