package com.example.demo.educms.controller;


import com.example.commonutils.R;
import com.example.demo.educms.entity.CrmBanner;
import com.example.demo.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-06-29
 */
@RestController
@RequestMapping("/educms/bannerfront")
//@CrossOrigin
public class BannerFrontController {

    @Autowired
    private CrmBannerService bannerService;

    // 查询所有banner
    @GetMapping("/getAllBanner")
    public R getAllBanner() {
        List<CrmBanner> list = bannerService.selectAllBanner();
        return R.ok().data("list", list);
    }
}

