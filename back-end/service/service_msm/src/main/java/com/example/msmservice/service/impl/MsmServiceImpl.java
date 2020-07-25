package com.example.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.example.msmservice.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {

    // 发送短信的方法
    @Override
    public boolean send(Map<String, Object> param, String phone) {
        if(StringUtils.isEmpty(phone)) { return false; }

        DefaultProfile profile = DefaultProfile.getProfile(
                "default", "" ,"");
        IAcsClient client = new DefaultAcsClient(profile);

        // 设置相关固定的参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        // 设置发送相关的参数
        request.putQueryParameter("PhoneNumbers", phone); // 手机号
        request.putQueryParameter("SignName", "daydayup教育平台"); // 之前申请的阿里云签名的名称
        request.putQueryParameter("TemplateCode", "SMS_194525046"); // 之前申请的阿里云模板的模板code
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param)); // 验证码数据，需要转换成json数据传递，这也是为什么在controller中要将验证码放在map里

        // 最终发送
        try{
            CommonResponse response = client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
