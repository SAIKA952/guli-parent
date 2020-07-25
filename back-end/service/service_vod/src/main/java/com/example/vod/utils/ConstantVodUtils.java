package com.example.vod.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantVodUtils implements InitializingBean {
    //读取配置文件内容

    @Value("${aliyun.vod.file.keyid}")
    private String keyId;

    @Value("${aliyun.vod.file.keysecret}")
    private String keySecret;

    //定义一些公开静态常量，为了外边能使用
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;

    @Override // 这个方法会在上面都初始化完成之后执行
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
    }
}
