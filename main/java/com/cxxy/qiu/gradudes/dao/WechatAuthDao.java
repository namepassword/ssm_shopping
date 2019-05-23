package com.cxxy.qiu.gradudes.dao;

import com.cxxy.qiu.gradudes.entity.WechatAuth;


public interface WechatAuthDao {
	/**
	 * 通过openId查询对应平台的微信账号
	 * @param openId
	 * @return
	 */
	WechatAuth queryWechatInfoByOpenId(String openId);

	/**
	 * 添加对应版本平台的微信账号
	 * @param wechatAuth
	 * @return
	 */
	int insertWechatAuth(WechatAuth wechatAuth);

	/**
	 * 
	 * @param wechatAuthId
	 * @return
	 */
	int deleteWechatAuth(Long wechatAuthId);
}
