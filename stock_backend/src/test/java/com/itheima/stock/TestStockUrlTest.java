package com.itheima.stock;

import com.itheima.stock.common.StockInfoConfig;
import com.itheima.stock.config.HttpClientConfig;
import com.itheima.stock.service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author by itheima
 * @Date 2022/1/14
 * @Description 测试股票接口
 */
@SpringBootTest
public class TestStockUrlTest {

    @Autowired
    private StockTimerTaskService stockTimerTaskService;

    /**
     * 测试获取国内大盘的数据
     */
    @Test
    public void testInnderUrl(){
        stockTimerTaskService.getInnerMarketInfo();
    }

    @Test
    public void testReg1(){
        String content = "I am noob " +
                "from runoob.com.";
        //定义正则
        String pattern = ".*runoob.*";
        //匹配，如果能匹配到，则返回true，否则false
        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println("字符串中是否包含了 'runoob' 子字符串? " + isMatch);
    }

    @Test
    public void testReg2(){
        // 按指定模式在字符串查找
        String line = "This order was placed for QT3000! OK?";
        //定义正则表达式
        String pattern = "(\\D*)(\\d+)(.*)";

        // 编译表达式，生成匹配对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象 匹配到的一切结果都封装到Matcher对象下
        Matcher m = r.matcher(line);
        //m.find()查找匹配到的信息，如果有，则返回true，否则false
        if (m.find()) {
            System.out.println("Found value: " + m.group(0) );
            System.out.println("Found value: " + m.group(1) );
            System.out.println("Found value: " + m.group(2) );
            System.out.println("Found value: " + m.group(3) );
        } else {
            System.out.println("NO MATCH");
        }
    }

    @Test
    public void testStockMarketReg(){
        String target="var hq_str_s_sh000001=\"上证指数,3534.1672,-21.0913,-0.59,2245894,26963537\";\n" +
                "var hq_str_s_sz399001=\"深证成指,14157.58,19.241,0.14,301120550,37761702\";";
        String reg="var hq_str_(.+)=\"(.+)\";";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(target);
        while (matcher.find()) {
            //获取第一组
            String g0 = matcher.group(0);
            System.out.println(g0);
            //第一组
            String g1 = matcher.group(1);
            System.out.println(g1);
            //第二组
            String g2 = matcher.group(2);
            System.out.println(g2);
        }
    }

    /**
     * 测试获取A股股票信息
     */
    @Test
    public void testA(){
        stockTimerTaskService.getStockRtIndex();
    }

    /**
     * 测试获取板块数据
     */
    @Test
    public void testBlock(){
        stockTimerTaskService.getStockSectorRtIndex();
    }

}
