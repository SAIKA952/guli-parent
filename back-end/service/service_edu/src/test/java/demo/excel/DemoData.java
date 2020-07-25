package demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DemoData {

    //设置excel里表头的名称
    @ExcelProperty(value = "学生编号", index = 0)
    private Integer sid;

    @ExcelProperty(value = "学生姓名", index = 1)
    private String sname;
}
