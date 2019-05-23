package com.cxxy.qiu.gradudes.service.impl;


import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cxxy.qiu.gradudes.dao.ProductSellDailyDao;
import com.cxxy.qiu.gradudes.entity.ProductSellDaily;
import com.cxxy.qiu.gradudes.service.ProductSellDailyService;

@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService{

	private static final Logger log=LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);
	@Autowired
	private ProductSellDailyDao  proudctSellDailyDao;
	
	@Override
	public void dailyCalculate() {
		log.info("Quartz Running");
		//统计在tb_user_product_map里面生产销量的每个店铺的各个商品的日销量
		proudctSellDailyDao.insertProductSellDaily();
		//统计余下的商品的日销量，全部为0
		proudctSellDailyDao.insertDefaultProductSellDaily();
		
	}

	@Override
	public List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime,
			Date endTime) {
		return proudctSellDailyDao.queryProductSellDailyList(productSellDailyCondition, beginTime, endTime);
	}
    
}
