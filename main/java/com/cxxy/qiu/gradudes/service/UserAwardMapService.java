package com.cxxy.qiu.gradudes.service;

import com.cxxy.qiu.gradudes.dto.UserAwardMapExecution;
import com.cxxy.qiu.gradudes.entity.UserAwardMap;

public interface UserAwardMapService {
	/*
	 * 根据传入的查询条件分页获取映射列表及总数
	 * 
	 * */
	UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardCondition,
			Integer pageIndex, Integer pageSize);
	/*
	 * 根据传入的Id获取映射器
	 * 
	 * */
	UserAwardMap getUserAwardMapById(long userAwardMapId);
	
	UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap)
			throws RuntimeException;
	
	UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap)
			throws RuntimeException;
}
