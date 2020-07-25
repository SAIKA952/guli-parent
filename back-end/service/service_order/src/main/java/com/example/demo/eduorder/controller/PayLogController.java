package com.example.demo.eduorder.controller;


import com.example.commonutils.R;
import com.example.demo.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-04
 */
@RestController
@RequestMapping("/eduorder/paylog")
//@CrossOrigin
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    // 生成微信支付二维码的接口，参数是订单号
    @GetMapping("/createCode/{orderNo}")
    public R createCode(@PathVariable String orderNo) {
        // 返回信息，包含二维码地址以及其他需要的信息
        Map map = payLogService.createCode(orderNo);
        System.out.println("***返回二维码map集合：" + map);
        return R.ok().data(map);
    }

    // 根据订单号查询订单支付状态
    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
        Map<String, String> map = payLogService.queryPayStatus(orderNo);
        System.out.println("***查询订单状态map集合：" + map);
        if(map == null) {
            return R.error().message("支付失败");
        }
        // 如果map里不为空 ，通过map获取订单的状态
        if(map.get("trade_state").equals("SUCCESS")) { // 支付成功
            // 添加记录到支付表，更新订单表订单状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }
        return R.ok().code(25000).message("支付中...");
    }
}

