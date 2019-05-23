package com.cxxy.qiu.gradudes.service;

import java.util.Date;
import java.util.List;

import com.cxxy.qiu.gradudes.entity.ProductSellDaily;

public interface ProductSellDailyService {

	/*
	 * 每日定时对所有商品销量进行统计
	 * 
	 * */
	void dailyCalculate();
	/*
	 * 根据查询条件返回商品日销售的统计列表
	 * 
	 * */
	List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition,Date beginTime,Date endTime);
}
