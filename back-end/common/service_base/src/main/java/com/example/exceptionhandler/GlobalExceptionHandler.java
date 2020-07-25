package com.example.exceptionhandler;

import com.example.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)  //指定什么异常执行这个方法
    @ResponseBody   //返回数据
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理...");
    }

    @ExceptionHandler(ArithmeticException.class)  //特定异常
    @ResponseBody   //返回数据
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常处理...");
    }

    @ExceptionHandler(GuliException.class)  //自定义异常
    @ResponseBody   //返回数据
    public R error(GuliException e){
        //将错误信息写到日志文件中
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }

}
