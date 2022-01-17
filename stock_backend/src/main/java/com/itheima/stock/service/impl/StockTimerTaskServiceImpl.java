package com.itheima.stock.service.impl;

import com.google.common.collect.Lists;
import com.itheima.stock.common.StockInfoConfig;
import com.itheima.stock.mapper.StockBlockRtInfoMapper;
import com.itheima.stock.mapper.StockBusinessMapper;
import com.itheima.stock.mapper.StockMarketIndexInfoMapper;
import com.itheima.stock.mapper.StockRtInfoMapper;
import com.itheima.stock.pojo.StockBlockRtInfo;
import com.itheima.stock.pojo.StockMarketIndexInfo;
import com.itheima.stock.pojo.StockRtInfo;
import com.itheima.stock.service.StockTimerTaskService;
import com.itheima.stock.utils.DateTimeUtil;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.utils.ParserStockInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author by itheima
 * @Date 2022/1/14
 * @Description 定义采集股票数据的定时任务的服务接口实现
 */
@Service("stockTimerTaskService")
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    @Autowired
    private ParserStockInfoUtil parserStockInfoUtil;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;

    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void getInnerMarketInfo() {
        //组装动态url
        //http://hq.sinajs.cn/list=s_sh000001,s_sz399001
        String innerUrl=stockInfoConfig.getMarketUrl()+String.join(",",stockInfoConfig.getInner());
        //发起请求
        String result = restTemplate.getForObject(innerUrl, String.class);
        String reg="var hq_str_(.+)=\"(.+)\";";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(result);
        //收集大盘封装后后的对象
        ArrayList<StockMarketIndexInfo> list = new ArrayList<>();
        while (matcher.find()) {
            //获取大盘的id
            String marketCode = matcher.group(1);
            System.out.println(marketCode);
            //其它信息
            String other = matcher.group(2);
            String[] others = other.split(",");
            //大盘名称
            String marketName=others[0];
            //当前点
            BigDecimal curPoint=new BigDecimal(others[1]);
            //当前价格
            BigDecimal curPrice=new BigDecimal(others[2]);
            //涨跌率
            BigDecimal upDownRate=new BigDecimal(others[3]);
            //成交量
            Long tradeAmount=Long.valueOf(others[4]);
            //成交金额
            Long tradeVol=Long.valueOf(others[5]);
            //当前日期
            Date now = DateTimeUtil.getDateTimeWithoutSecond(DateTime.now()).toDate();
            //封装对象
            StockMarketIndexInfo stockMarketIndexInfo = StockMarketIndexInfo.builder().id(idWorker.nextId())
                    .markName(marketName)
                    .tradeVolume(tradeVol)
                    .tradeAccount(tradeAmount)
                    .updownRate(upDownRate)
                    .curTime(now)
                    .curPoint(curPoint)
                    .currentPrice(curPrice)
                    .markId(marketCode)
                    .build();
            list.add(stockMarketIndexInfo);
        };
        log.info("集合长度：{}，内容：{}",list.size(),list);
        //批量插入
        if (CollectionUtils.isEmpty(list)) {
            log.info("");
            return;
        }
        String curTime = DateTime.now().toString(DateTimeFormat.forPattern("yyyyMMddHHmmss"));
        log.info("采集的大盘数据：{},当前时间：{}",list,curTime);
        //this.stockMarketIndexInfoMapper.insertBatch(list);
    }

    @Override
    public void getStockRtIndex() {
        //1.获取所有股票的id TODO 缓存优化
        List<String> stockIds=stockBusinessMapper.findAllStockIds();//40--->3000
        //深证：A：以0开头 上证：6开头
        stockIds = stockIds.stream().map(id -> {
            id = id.startsWith("6") ? "sh" + id : "sz" + id;
            return id;
        }).collect(Collectors.toList());
        //要求：将集合分组，每组的集合长度为20
        Lists.partition(stockIds,20).forEach(ids->{
           //每个分片的数据开启一个线程异步执行任务
           threadPoolTaskExecutor.execute(()->{
               //拼接获取A股信息的url地址
               String stockRtUrl=stockInfoConfig.getMarketUrl()+String.join(",",ids);
               //发送请求获取数据
               String result = restTemplate.getForObject(stockRtUrl, String.class);
               //解析获取股票数据
               List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(result, 3);
               //分批次批量插入
               log.info("当前股票数据：{}",list);
               //stockRtInfoMapper.insertBatch(list);
           });
        });
    }

    /**
     * 获取国内的大盘的板块数据
     */
    @Override
    public void getStockSectorRtIndex() {
        //1.组装url地址
        String blockUrl=stockInfoConfig.getBlockUrl();
        //2.获取响应数据
        String result = restTemplate.getForObject(blockUrl, String.class);
        //3.调用解析工具类获取板块集合
        List<StockBlockRtInfo> infos = parserStockInfoUtil.parse4StockBlock(result);
        //4.分片处理
        Lists.partition(infos,20).forEach(items->{
            threadPoolTaskExecutor.execute(()->{
                //批量插入
                log.info("当前板块数据：{}",items);
                //stockBlockRtInfoMapper.insertBatch(items);
            });
        });

    }

    /**
     * @author SaKoRua
     * @Description //TODO 测试定时任务
     * @Date 10:21 AM 2022/1/17
     * @Param
     * @return
     **/
    @Override
    public void testTimerTask() {
        System.out.println("Test...Running");
    }


}
