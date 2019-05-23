package com.cxxy.qiu.gradudes.service;


import com.cxxy.qiu.gradudes.dto.WechatAuthExecution;
import com.cxxy.qiu.gradudes.entity.WechatAuth;
import com.cxxy.qiu.gradudes.exceptions.WechatOperationExecution;



public interface WechatAuthService {

	/**
	 * 通过openId查找平台对应的微信账号
	 * @param openId
	 * @return
	 */
	WechatAuth getWechatAuthByOpenId(String openId);

	/**
	 * 注册本平台的微信账号
	 * @param wechatAuth
	 * @param profileImg
	 * @return
	 * @throws RuntimeException
	 */
	WechatAuthExecution register(WechatAuth wechatAuth) throws WechatOperationExecution;

}
