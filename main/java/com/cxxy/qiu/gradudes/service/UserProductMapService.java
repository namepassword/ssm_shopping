package com.cxxy.qiu.gradudes.service;

import com.cxxy.qiu.gradudes.dto.UserProductMapExecution;
import com.cxxy.qiu.gradudes.entity.UserProductMap;
import com.cxxy.qiu.gradudes.exceptions.UserProductMapOperationException;

public interface UserProductMapService {

	/*
	 * 通过传入的查询条件分页列出用户消费信息列表
	 * */
	UserProductMapExecution listUserProductMap(UserProductMap userProductCondition,Integer pageIndex,Integer pageSize);
	
	UserProductMapExecution addUserProductMap(UserProductMap userProductMap) throws UserProductMapOperationException;
	
}
