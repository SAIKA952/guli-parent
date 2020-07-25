package com.example.demo.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.R;
import com.example.demo.educms.entity.CrmBanner;
import com.example.demo.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 后台banner管理接口
 * </p>
 *
 * @author testjava
 * @since 2020-06-29
 */
@RestController
@RequestMapping("/educms/banneradmin")
//@CrossOrigin
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    // 1、分页查询banner
    @GetMapping("/pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable long page,
                        @PathVariable long limit) {
        Page<CrmBanner> pageBanner = new Page<>(page, limit);
        bannerService.page(pageBanner, null);
        return R.ok().data("items", pageBanner.getRecords()).data("total", pageBanner.getTotal());
    }

    // 2、添加banner
    @PostMapping("/addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner) {
        bannerService.save(crmBanner);
        return R.ok();
    }

    // 3、修改banner
    @PostMapping("/updateBanner")
    public R updateBanner(@RequestBody CrmBanner crmBanner) {
        bannerService.updateById(crmBanner);
        return R.ok();
    }

    // 4、删除banner
    @DeleteMapping("/deleteBanner/{id}")
    public R deleteBanner(@PathVariable String id) {
        bannerService.removeById(id);
        return R.ok();
    }

    // 5、根据id查询banner
    @GetMapping("/get/{id}")
    public R getBanner(@PathVariable String id) {
        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("item", banner);
    }
}

