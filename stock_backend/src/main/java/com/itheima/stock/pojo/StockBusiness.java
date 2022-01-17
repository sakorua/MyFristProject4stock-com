package com.itheima.stock.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 主营业务表
 * @TableName stock_business
 */
@Data
public class StockBusiness implements Serializable {
    /**
     * 证券代码 股票编码
     */
    private String secCode;

    /**
     * 证券名称
     */
    private String secName;

    /**
     * 板块代码
     */
    private String sectorCode;

    /**
     * 板块名称
     */
    private String sectorName;

    /**
     * 主营业务
     */
    private String business;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}