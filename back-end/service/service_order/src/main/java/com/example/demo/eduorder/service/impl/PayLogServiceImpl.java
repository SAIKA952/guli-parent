package com.example.demo.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.eduorder.entity.Order;
import com.example.demo.eduorder.entity.PayLog;
import com.example.demo.eduorder.mapper.PayLogMapper;
import com.example.demo.eduorder.service.OrderService;
import com.example.demo.eduorder.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.eduorder.utils.HttpClient;
import com.example.exceptionhandler.GuliException;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-04
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    // 生成微信支付二维码
    @Override
    public Map createCode(String orderNo) {
        try {
            // 1、根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            Order order = orderService.getOne(wrapper);

            // 2、使用map设置生成二维码需要参数
            Map m = new HashMap();
            m.put("appid", "wx74862e0dfcf69954"); // wxid
            m.put("mch_id", "1558950191"); // 商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr()); // 生成随机字符串
            m.put("body", order.getCourseTitle()); // 课程标题
            m.put("out_trade_no", orderNo); // 订单号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+""); // 价格
            m.put("spbill_create_ip", "127.0.0.1"); // 项目的域名
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n"); // 回调地址
            m.put("trade_type", "NATIVE"); // 支付的类型

            // 3、发送httpclient请求，传递xml格式的参数，去请求微信支付提供的固定的地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            // 设置xml格式的数据
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true); // 支持https访问
            // 执行请求发送
            client.post();

            // 4、得到发送请求返回的结果
            // 返回的内容是使用xml格式返回的
            String xml = client.getContent();
            // 把xml格式转换为map集合，把map集合返回
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            // 还需要将其他信息添加到resultMap中
            // 最终返回数据的封装
            Map map = new HashMap();
            map.put("out_trade_no", orderNo); // 订单id
            map.put("course_id", order.getCourseId()); // 课程id
            map.put("total_fee", order.getTotalFee()); // 价格
            map.put("result_code", resultMap.get("result_code")); // 二维码操作的状态码
            map.put("code_url", resultMap.get("code_url")); // 二维码的地址

            return map;
        }catch (Exception e) {
            throw new GuliException(20001, "生成二维码失败");
        }
    }

    // 根据订单号查询订单支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、发送httpclient
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //4、转成Map返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 向支付表里添加记录。更新订单状态
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        // 从map中获取订单号
        String orderNo = map.get("out_trade_no");
        // 根据订单号查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(wrapper);

        // 更新订单表里的状态
        if(order.getStatus().intValue() == 1) {
            return;
        }
        order.setStatus(1); // 1代表已经支付，0代表未支付
        orderService.updateById(order);

        // 向支付表里添加支付记录
        PayLog payLog = new PayLog();
        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date()); // 支付时间
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));//流水号
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);
    }
}
