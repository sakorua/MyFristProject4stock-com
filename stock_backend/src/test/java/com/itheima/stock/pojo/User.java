package com.itheima.stock.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author by itheima
 * @Date 2022/1/11
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@HeadRowHeight(value = 35) // 表头行高
@ContentRowHeight(value = 25) // 内容行高
@ColumnWidth(value = 30) // 列宽
public class User {
    @ExcelProperty(value = {"用户信息","用户名"},index = 0)
    private String userName;
    @ExcelProperty(value = {"用户信息","年龄"},index = 1)
//    @ExcelIgnore//忽略当前的字段，不导出
    private Integer age;
    @ExcelProperty(value = {"用户信息2","地址"} ,index = 2)
    private String address;
    @ExcelProperty(value = {"用户信息2","生日"},index = 3)
    @DateTimeFormat("yyyy/MM/dd")
    private Date birthday;
}
