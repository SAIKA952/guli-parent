package com.example.demo.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.commonutils.JwtUtils;
import com.example.commonutils.R;
import com.example.demo.eduorder.entity.Order;
import com.example.demo.eduorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-07-04
 */
@RestController
@RequestMapping("/eduorder/order")
//@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 1、生成订单的方法
    @PostMapping("/createdOrder/{courseId}")
    public R createdOrder(@PathVariable String courseId, HttpServletRequest request) { // 因为要获取用户信息，而我们之前写的前端拦截器是将用户信息保存在header里
        String userId = JwtUtils.getMemberIdByJwtToken(request); // 获取到用户id
        if(userId == "") {
            return R.error().code(28004);
        }
        // 创建订单，返回订单号
        String orderId = orderService.createdOrders(courseId, userId);
        return R.ok().data("orderId", orderId);
    }

    // 2、根据订单id查询订单信息
    @GetMapping("/gerOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderId);
        Order order = orderService.getOne(wrapper);
        return R.ok().data("item", order);
    }

    // 3、根据课程id和用户id查询订单表中的状态
    @GetMapping("/isBuyCourse/{courseId}/{userId}")
    public boolean isBuyCourse(@PathVariable String courseId, @PathVariable String userId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.eq("member_id", userId);
        wrapper.eq("status", 1); // 支付状态，1表示已经支付
        int count = orderService.count(wrapper);

        return count > 0;
    }
}

