package com.atguigu.aclservice.config;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmtCreate", new Date(), metaObject);
        this.setFieldValByName("gmtUpdate", new Date(), metaObject);
//        this.setFieldValByName("version", Long.valueOf(1), metaObject);
//        this.setFieldValByName("appSecret", UUID.randomUUID().toString().replaceAll("-", ""), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtUpdate", new Date(), metaObject);
//        this.setFieldValByName("version", this.getFieldValByName("version", metaObject), metaObject);
    }
}