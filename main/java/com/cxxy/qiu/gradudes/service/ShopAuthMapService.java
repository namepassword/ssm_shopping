package com.cxxy.qiu.gradudes.service;

import com.cxxy.qiu.gradudes.dto.ShopAuthMapExecution;
import com.cxxy.qiu.gradudes.entity.ShopAuthMap;



public interface ShopAuthMapService {

	/*
	 * 根据店铺Id分页显示该店铺的授权信息
	 * */
	ShopAuthMapExecution listShopAuthMapByShopId(Long shopId,
			Integer pageIndex, Integer pageSize);

	/*
	 * 添加授权信息
	 * 
	 * */
	ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap)
			throws RuntimeException;
	/*
	 * 更新授权信息，包括职位等
	 * 
	 * */
	
	ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws RuntimeException;

	//对员工移权
	ShopAuthMapExecution removeShopAuthMap(Long shopAuthMapId)
			throws RuntimeException;

	/*
	 * 根据shopAuthId返回对应的授权信息
	 * 
	 * */
	ShopAuthMap getShopAuthMapById(Long shopAuthId);

}
