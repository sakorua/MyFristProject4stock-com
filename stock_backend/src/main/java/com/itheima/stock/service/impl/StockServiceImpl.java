package com.itheima.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.itheima.stock.common.StockInfoConfig;
import com.itheima.stock.common.domain.InnerMarketDomain;
import com.itheima.stock.common.domain.StockExcelDomain;
import com.itheima.stock.mapper.StockBlockRtInfoMapper;
import com.itheima.stock.mapper.StockBusinessMapper;
import com.itheima.stock.mapper.StockMarketIndexInfoMapper;
import com.itheima.stock.mapper.StockRtInfoMapper;
import com.itheima.stock.pojo.StockBusiness;
import com.itheima.stock.service.StockService;
import com.itheima.stock.utils.DateTimeUtil;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author by itheima
 * @Date 2022/1/8
 * @Description 定义股票服务接口是实现
 */
@Service("stockService")
@Slf4j
public class StockServiceImpl implements StockService {

    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    /**
     * 注入股票操纵的mapper
     */
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    /**
     * 注入板块mapper
     */
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;


    @Override
    public R<List<StockBusiness>> findAllBusiness() {
        List<StockBusiness> list= stockBusinessMapper.findAll();
        return R.ok(list);
    }

    /**
     * 获取国内大盘的实时数据
     * @return
     */
    @Override
    public R<List<InnerMarketDomain>> innerIndexAll() {
        //1.获取国内大盘的id集合
        List<String> innerIds = stockInfoConfig.getInner();
        //2.获取最近最新的股票有效交易日
        DateTime lDate = DateTimeUtil.getLastDate4Stock(DateTime.now());
        String mockDate="20211226105600";//TODO 后续大盘数据实时拉去，将该行注释掉 传入的日期秒必须为O
        Date date = DateTime.parse(mockDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        //3.调用mapper查询指定日期下对应的国内大盘数据
        List<InnerMarketDomain> maps=stockMarketIndexInfoMapper.selectByIdsAndDate(innerIds,date);
        //组装响应的额数据
        if (CollectionUtils.isEmpty(maps)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(maps);
    }

    /**
     *需求说明: 沪深两市板块分时行情数据查询，以交易时间和交易总金额降序查询，取前10条数据
     * @return
     */
    @Override
    public R<List<Map>> sectorAllLimit() {
        //1.调用mapper接口获取数据 TODO 优化 避免全表查询 根据时间范围查询，提高查询效率
         List<Map> infos=stockBlockRtInfoMapper.sectorAllLimit();
        //2.组装数据
        if (CollectionUtils.isEmpty(infos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(infos);

    }

    /**
     * 沪深两市个股涨幅分时行情数据查询，以时间顺序和涨幅查询前10条数据
     * @return
     */
    @Override
    public R<List<Map>> stockIncreaseLimit() {
        //1.直接调用mapper查询前10的数据 TODO 以时间顺序取前10
       List<Map> infos=stockRtInfoMapper.stockIncreaseLimit();
       //2.判断是否有数据
        if (CollectionUtils.isEmpty(infos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(infos);
    }

    /**
     * 沪深两市个股行情列表查询 ,以时间顺序和涨幅分页查询
     * @param page 当前页
     * @param pageSize 每页大小
     * @return
     */
    @Override
    public R<PageResult<Map>> stockPage(Integer page, Integer pageSize) {
        //1.设置分页参数
        PageHelper.startPage(page,pageSize);
        //2.通过mapper查询
        List<Map> infos= stockRtInfoMapper.stockAll();
        //3.封装到PageResult下
        //3.1 封装PageInfo对象
        PageInfo<Map> listPageInfo = new PageInfo<Map>(infos);
        //3.2 将PageInfo转PageResult
        PageResult<Map> pageResult = new PageResult<>(listPageInfo);
        //4.封装R响应对象
        return R.ok(pageResult);
    }

    /**
     * 功能描述：沪深两市涨跌停分时行情数据查询，查询T日每分钟的涨跌停数据（T：当前股票交易日）
     * 		查询每分钟的涨停和跌停的数据的同级；
     * 		如果不在股票的交易日内，那么就统计最近的股票交易下的数据
     * 	 map:
     * 	    upList:涨停数据统计
     * 	    downList:跌停数据统计
     * @return
     */
    @Override
    public R<Map> upDownCount() {
        //1.获取股票最近的有效交易日期,精确到秒
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //当前最近有效期
        Date curTime = curDateTime.toDate();
        //开盘日期
        Date openTime = DateTimeUtil.getOpenDate(curDateTime).toDate();
        //TODO 后续数据实时获取时，注释掉
        String curTimeStr="20220106142500";
        //对应开盘日期
        String openTimeStr="20220106092500";
         curTime = DateTime.parse(curTimeStr, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
         openTime = DateTime.parse(openTimeStr, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();

        //2.统计涨停的数据 约定：1代表涨停 0：跌停
        List<Map> upCount= stockRtInfoMapper.upDownCount(curTime,openTime,1);
        //3.统计跌停的数据
        List<Map> downCount= stockRtInfoMapper.upDownCount(curTime,openTime,0);
        //4.组装数据到map
        HashMap<String, List<Map>> info = new HashMap<>();
        info.put("upList",upCount);
        info.put("downList",downCount);
        //5.响应
       return R.ok(info);
    }

    /**
     * 将指定页的股票数据导出到excel表下
     * @param response
     * @param page  当前页
     * @param pageSize 每页大小
     */
    @Override
    public void stockExport(HttpServletResponse response, Integer page, Integer pageSize) {
        try {
            //1.设置响应数据的类型:excel
            response.setContentType("application/vnd.ms-excel");
            //2.设置响应数据的编码格式
            response.setCharacterEncoding("utf-8");
            //3.设置默认的文件名称
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("stockRt", "UTF-8");
            //设置默认文件名称
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //4.分页查询股票数据
            PageHelper.startPage(page,pageSize);
            List<Map> infos = this.stockRtInfoMapper.stockAll();
            Gson gson = new Gson();
            List<StockExcelDomain> excelDomains = infos.stream().map(map -> {
                StockExcelDomain domain = gson.fromJson(gson.toJsonTree(map), StockExcelDomain.class);
                return domain;
            }).collect(Collectors.toList());
            //5.导出
            EasyExcel.write(response.getOutputStream(),StockExcelDomain.class).sheet("股票数据").doWrite(excelDomains);
        } catch (IOException e) {
           log.info("股票excel数据导出异常，当前页：{}，每页大小：{}，异常信息：{}",page,pageSize,e.getMessage());
        }
    }

    /**
     * 功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     *   map结构示例：
     *      {
     *         "volList": [{"count": 3926392,"time": "202112310930"},......],
     *       "yesVolList":[{"count": 3926392,"time": "202112310930"},......]
     *      }
     * @return
     */
    @Override
    public R<Map> stockTradeVol4InnerMarket() {
        //1.获取最近的股票交易日时间，精确到分钟 T交易日
        DateTime tDate = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date tDateTime = tDate.toDate();
        //对应的开盘时间
        Date tOpenTime = DateTimeUtil.getOpenDate(tDate).toDate();
        //TODO 后续注释掉
        String tDateStr="20220103143000";
        tDateTime = DateTime.parse(tDateStr, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        String openDateStr="20220103093000";
        tOpenTime = DateTime.parse(openDateStr, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();

        //获取T-1交易日
        DateTime preTDate = DateTimeUtil.getPreviousTradingDay(tDate);
        Date preTTime = preTDate.toDate();
        Date preTOpenTime=DateTimeUtil.getOpenDate(preTDate).toDate();
        //TODO 后续注释掉
        String preTStr="20220102143000";
        preTTime = DateTime.parse(preTStr, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        String openDateStr2="20220102093000";
        preTOpenTime= DateTime.parse(openDateStr2, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();

        //2.获取T日的股票大盘交易量统计数据
        List<Map> tData=stockMarketIndexInfoMapper.stockTradeVolCount(stockInfoConfig.getInner(),tOpenTime,tDateTime);
        //3.获取T-1的数据
        List<Map> preTData=stockMarketIndexInfoMapper.stockTradeVolCount(stockInfoConfig.getInner(),preTOpenTime,preTTime);
        //4.组装数据
        HashMap<String, List<Map>> data = new HashMap<>();
        data.put("volList",tData);
        data.put("yesVolList",preTData);
        return R.ok(data);
    }

    /**
     * 功能描述：统计在当前时间下（精确到分钟），股票在各个涨跌区间的数量
     *  如果当前不在股票有效时间内，则以最近的一个有效股票交易时间作为查询时间点；
     * @return
     *  响应数据格式：
     *  {
     *     "code": 1,
     *     "data": {
     *         "time": "2021-12-31 14:58:00",
     *         "infos": [
     *             {
     *                 "count": 17,
     *                 "title": "-3~0%"
     *             },
     *             //...
     *             ]
     *     }
     */
    @Override
    public R<Map> stockUpDownScopeCount() {
        //1.获取当前时间下最近的一个股票交易时间 精确到秒
        DateTime avlDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date avlDate = avlDateTime.toDate();
        //TODO 后续删除
        String  mockDate="20220106095500";
        avlDate = DateTime.parse(mockDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        //2.查询
        List<Map> maps=stockRtInfoMapper.stockUpDownScopeCount(avlDate);
        //获取去股票涨幅区间的集合
        List<String> upDownRange = stockInfoConfig.getUpDownRange();
        //将list集合下的字符串映射成Map对象
//        List<Map> orderMap = upDownRange.stream().map(key -> {
//            Optional<Map> title = maps.stream().filter(map -> key.equals(map.get("title"))).findFirst();
//            //判断对应的map是否存在
//            Map tmp = null;
//            if (title.isPresent()) {
//                tmp = title.get();
//            } else {
//                tmp = new HashMap();
//                tmp.put("title", key);
//                tmp.put("count", 0);
//            }
//            return tmp;
//        }).collect(Collectors.toList());

        ArrayList<Map> orderMap = new ArrayList<>();
        for (String title : upDownRange) {
            Map tmp=null;
            for (Map map : maps) {
                if (title.equals(map.get("title"))) {
                    tmp=map;
                }
            }
            if (tmp==null) {
                tmp=new HashMap();
                tmp.put("title",title);
                tmp.put("count",0);
            }
            orderMap.add(tmp);
        }
        //3.组装data
        HashMap<String, Object> data = new HashMap<>();
        data.put("time",avlDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        data.put("infos",orderMap);
        //返回响应数据
        return R.ok(data);
    }


    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     *         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * @param code 股票编码
     * @return
     */
    @Override
    public R<List<Map>> stockScreenTimeSharing(String code) {
        //1.获取最近有效的股票交易时间
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //获取当前日期
        Date curDate = curDateTime.toDate();
        //获取当前日期对应的开盘日期
        Date openDate = DateTimeUtil.getOpenDate(curDateTime).toDate();

        //TODO 后续删除
        String mockDate="20220106142500";
        curDate=DateTime.parse(mockDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        String openDateStr="20220106093000";
        openDate=DateTime.parse(openDateStr, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();

        List<Map> maps= stockRtInfoMapper.stockScreenTimeSharing(code,openDate,curDate);
        //响应前端
        return R.ok(maps);
    }

    /**
     * 功能描述：单个个股日K数据查询 ，可以根据时间区间查询数日的K线数据
     * 		默认查询历史20天的数据；
     * @param code 股票编码
     * @return
     */
    @Override
    public R<List<Map>> stockCreenDkLine(String code) {
        //获取当前日期前推20天
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //当前时间节点
        Date curTime = curDateTime.toDate();
        //前推20
        Date pre20Day = curDateTime.minusDays(20).toDate();

        //TODO 后续删除
        String avlDate="20220106142500";
        curTime=DateTime.parse(avlDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        String openDate="20220101093000";
        pre20Day=DateTime.parse(openDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();

        //获取指定范围的收盘日期集合
        List<Date> closeDates=stockRtInfoMapper.getCloseDates(code,pre20Day,curTime);
        //根据收盘日期精准查询，如果不在收盘日期，则查询最新数据
        List<Map> data= stockRtInfoMapper.getStockCreenDkLineData(code,closeDates);
        return R.ok(data);
    }

    /**
     * @author SaKoRua
     * @Description //TODO 外盘指数行情数据查询，根据时间和大盘点数降序排序取前4
     * @Date 11:12 AM 2022/1/17
     * @Param
     * @return
     **/
    @Override
    public R<List<Map>> externalIndex() {
        //获取外盘Id集合
        List<String> outer = stockInfoConfig.getOuter();
        //2.获取最近最新的股票有效交易日
        DateTime lDate = DateTimeUtil.getLastDate4Stock(DateTime.now());
        String mockDate="20211226120000";//TODO 后续大盘数据实时拉去，将该行注释掉 传入的日期秒必须为O
        Date date = DateTime.parse(mockDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate();
        //3.调用mapper查询指定日期下对应的国内大盘数据
        List<Map> maps=stockMarketIndexInfoMapper.selectByIdsAndDate4outer(outer,date);
        if (CollectionUtils.isEmpty(maps)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(maps);
    }

    /**
     * @author SaKoRua
     * @Description //TODO 根据输入的个股代码，进行模糊查询，返回证券代码和证券名称
     * @Date 12:47 PM 2022/1/17
     * @Param
     * @return
     **/
    @Override
    public R<List<Map>> fuzzyQuery(String searchStr) {
        List<Map> maps = stockRtInfoMapper.fuzzyQuery(searchStr);
        return R.ok(maps);
    }


    /**
     * @author SaKoRua
     * @Description //TODO 个股主营业务查询接口
     * @Date 1:43 PM 2022/1/17
     * @Param
     * @return
     **/
    @Override
    public R<Map> stockDescription(String code) {
        Map map = stockBusinessMapper.stockDescription(code);
        return R.ok(map);
    }
}
