package com.example.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.example.oss.service.OssService;
import com.example.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //上传文件到oss
    @Override
    public String uploadFileAvatar(MultipartFile file) {

        //直接通过工具类取值
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try{
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 获取上传文件输入流。
            InputStream inputStream = file.getInputStream();

            // 获取文件名称，添加随机唯一值，afgzq12as9t13...
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + file.getOriginalFilename();

            // 把文件按照日期进行分类
            // 比如2020/06/20/01.jpg
            // 获取当前日期
            String datePath = new DateTime().toString("yyyy/MM/dd");

            // 拼接文件名称
            fileName = datePath + "/" + fileName;

            // 调用oss方法实现上传
            // 第一和参数是Bucket名称，
            // 第二个参数是上传到oss文件路径和文件名称，比如/aa/bb/1.jpg
            // 第三个参数是上传文件的输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            // 把上传之后文件的路径返回
            // 需要把上传到阿里云oss路径手动拼接出来
            // https://edu-952.oss-cn-hangzhou.aliyuncs.com/79792801_p0.jpg
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return url;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
