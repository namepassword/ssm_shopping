package com.cxxy.qiu.gradudes.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.cxxy.qiu.gradudes.entity.LocalAuth;



public interface LocalAuthDao {

	/*
	 * 通过账号和密码查询对应信息，登录用
	 * */
	
	LocalAuth queryLocalByUserNameAndPwd(@Param("userName") String userName,
			@Param("password") String password);

	/*
	 * 通过用户ID查询对应的localauth
	 * */
	LocalAuth queryLocalByUserId(@Param("userId") long userId);

	/*
	 * 添加平台账号
	 * */
	int insertLocalAuth(LocalAuth localAuth);
	/*
	 *通过userId,usersname,password更改密码 
	 * */
	
	int updateLocalAuth(@Param("userId") Long userId,
			@Param("userName") String userName,
			@Param("password") String password,
			@Param("newPassword") String newPassword,
			@Param("lastEditTime") Date lastEditTime);
}
