package com.cxxy.qiu.gradudes.service;


import com.cxxy.qiu.gradudes.dto.LocalAuthExecution;
import com.cxxy.qiu.gradudes.entity.LocalAuth;
import com.cxxy.qiu.gradudes.exceptions.LocalAuthOperationException;



public interface LocalAuthService {
	/**
	 * 通过账号和密码获取平台帐号信息
	 * @param userName
	 * @return
	 */
	LocalAuth getLocalAuthByUserNameAndPwd(String userName, String password);

	/**
	 * 通过userId获取平台帐号信息
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);
/*
	*//**
	 * 
	 * @param localAuth
	 * @param profileImg
	 * @return
	 * @throws RuntimeException
	 *//*
	LocalAuthExecution register(LocalAuth localAuth,
			CommonsMultipartFile profileImg) throws LocalAuthOperationException;
*/
	/**
	 * 绑定微信，生成平台专属帐号
	 * @param localAuth
	 * @return
	 * @throws RuntimeException
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth)
			throws LocalAuthOperationException;

	/**
	 * 修改平台帐号的登录密码
	 * @param localAuthId
	 * @param userName
	 * @param password
	 * @param newPassword
	 * @param lastEditTime
	 * @return
	 */
	LocalAuthExecution modifyLocalAuth(Long userId, String userName,
			String password, String newPassword);
}
