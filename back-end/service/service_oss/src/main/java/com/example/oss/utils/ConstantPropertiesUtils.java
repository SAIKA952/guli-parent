package com.example.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
//当项目一启动，spring中有一个接口，spring加载之后会执行接口中的方法
public class ConstantPropertiesUtils implements InitializingBean {

    //读取配置文件内容
    //通过注解将配置文件中内容读取出来，赋值给endpoint
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;

    //定义一些公开静态常量，为了外边能使用
    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @Override // 这个方法会在上面都初始化完成之后执行
    public void afterPropertiesSet() throws Exception {
        END_POINT = endpoint;
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
}
