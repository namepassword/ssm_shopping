package com.cxxy.qiu.gradudes.dao;



import com.cxxy.qiu.gradudes.entity.PersonInfo;



public interface PersonInfoDao {

	/**
	 * 通过用户Id查询用户
	 * @param userId
	 * @return
	 */
	PersonInfo queryPersonInfoById(long userId);

	/**
	 * 添加用户
	 * @param wechatAuth
	 * @return
	 */
	int insertPersonInfo(PersonInfo personInfo);

}
