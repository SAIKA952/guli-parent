package com.example.msmservice.controller;

import com.example.commonutils.R;
import com.example.msmservice.service.MsmService;
import com.example.msmservice.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
//@CrossOrigin
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 发送短信的方法
    @GetMapping("/send/{phone}")
    public R sendMsm(@PathVariable String phone) {
        // 先从redis中获取验证码，如果能获取到就直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)) {
            return R.ok();
        }

        // 如果redis中获取不到，就进行阿里云发送
        // 生成随机的值，传到阿里云中进行发送
        code = RandomUtil.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);

        //调用service发送短信的方法
        boolean isSend = msmService.send(param, phone);
        if(isSend) {
            // 发送成功，把发送成功的验证码放到redis里，同时设置有效时间
            // 参数分别为key，value，时间，时间的单位
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("短信发送失败");
        }
    }
}
