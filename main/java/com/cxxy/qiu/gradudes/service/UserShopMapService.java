package com.cxxy.qiu.gradudes.service;

import com.cxxy.qiu.gradudes.dto.UserShopMapExecution;
import com.cxxy.qiu.gradudes.entity.UserShopMap;

public interface UserShopMapService {

	/*
	 *依据传入的查询信息分页查询用户积分列表
	 * */
	UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition,
			int pageIndex, int pageSize);
	/*
	 * 根据用户Id和店铺Id返回该用户在某个店铺积分的情况
	 * */
	UserShopMap getUserShopMap(long userId,long shopId);
	
}
