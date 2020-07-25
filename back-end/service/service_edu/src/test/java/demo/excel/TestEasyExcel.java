package demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.*;
import java.util.ArrayList;

public class TestEasyExcel {
    public static void main(String[] args) {
        //实现excel写的操作
        //1、设置写入的文件夹地址和excel文件的名称
//        String filename = "D:\\i_cant_study\\Demo\\testEasyExcel\\test.xlsx";

        //2、调用easyexcel里的方法实现写操作
        //write中有两个参数，第一个参数是文件路径的名称，第二个参数是实体类的class
        //.sheet可以指定sheet的名称，.doWrite将数据写到excel中。之后文件流会自动关闭
//        EasyExcel.write(filename, DemoData.class).sheet("学生列表").doWrite(getData());

        //实现excel读操作
        String filename = "D:\\i_cant_study\\Demo\\testEasyExcel\\test.xlsx";
        EasyExcel.read(filename, DemoData.class, new ExcelListener()).sheet().doRead();

    }

    //创建一个方法进行简单测试，返回list集合
    private static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for(int i  = 0; i < 10; i++){
            DemoData data = new DemoData();
            data.setSid(i);
            data.setSname("Lucy:" + i);
            list.add(data);
        }
        return list;
    }
}
