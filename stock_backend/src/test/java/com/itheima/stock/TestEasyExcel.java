package com.itheima.stock;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.itheima.stock.pojo.User;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author by itheima
 * @Date 2022/1/11
 * @Description
 */
public class TestEasyExcel {

    public List<User> init(){
        //组装数据
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setAddress("上海"+i);
            user.setUserName("张三"+i);
            user.setBirthday(new Date());
            user.setAge(10+i);
            users.add(user);
        }
        return users;
    }


    @Test
    public void testExport(){
        //准备需要导出的数据对象
        List<User> init = this.init();
        //导出 如果实体类中没有指定excel头信息，那么默认就是属性名称
        EasyExcel.write("C:\\Users\\46035\\Desktop\\excel\\export.xls",User.class).sheet("139")
                .doWrite(init);
    }

    @Test
    public void restImport(){
        ArrayList<User> users = new ArrayList<>();
        EasyExcel.read("C:\\Users\\46035\\Desktop\\excel\\export.xls", User.class, new ReadListener<User>() {
            /**
             * @param user 值每一行映射的user对象
             * @param context excel信息的上下文
             */
            @Override
            public void invoke(User user, AnalysisContext context) {
                //收集user对象
                users.add(user);
            }

            /**
             * excel解析完毕后触发的事件
             * @param context
             */
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                System.out.println("excel解析完毕.....");
            }
        }).sheet("139").doRead();
        System.out.println(users);
    }



}
