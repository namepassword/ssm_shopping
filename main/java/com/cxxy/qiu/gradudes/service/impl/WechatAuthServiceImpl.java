package com.cxxy.qiu.gradudes.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxxy.qiu.gradudes.dao.PersonInfoDao;
import com.cxxy.qiu.gradudes.dao.WechatAuthDao;
import com.cxxy.qiu.gradudes.dto.WechatAuthExecution;
import com.cxxy.qiu.gradudes.entity.PersonInfo;
import com.cxxy.qiu.gradudes.entity.WechatAuth;
import com.cxxy.qiu.gradudes.enums.WechatAuthStateEnum;
import com.cxxy.qiu.gradudes.exceptions.WechatOperationExecution;
import com.cxxy.qiu.gradudes.service.WechatAuthService;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {
	private static Logger log = LoggerFactory
			.getLogger(WechatAuthServiceImpl.class);
	@Autowired
	private WechatAuthDao wechatAuthDao;
	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public WechatAuth getWechatAuthByOpenId(String openId) {
		// TODO Auto-generated method stub
		return wechatAuthDao.queryWechatInfoByOpenId(openId);
	}

	@Override
	@Transactional
	public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatOperationExecution {
		// 空值判断
		if (wechatAuth == null || wechatAuth.getOpenId() == null) {
			return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
		}
		try {
			// 设置创建时间
			wechatAuth.setCreateTime(new Date());
			// 如果微信账号里夹带着用户信息并且用户id为空，则认为该用户第一次使用平台（且通过微信登录）
			// 则自动创建用户信息
			if (wechatAuth.getPersonInfo() != null
					&& wechatAuth.getPersonInfo().getUserId() == null) {
				try {
					wechatAuth.getPersonInfo().setCreateTime(new Date());
					wechatAuth.getPersonInfo().setEnableStatus(1);
					PersonInfo personInfo = wechatAuth.getPersonInfo();
					int effectedNum = personInfoDao
							.insertPersonInfo(personInfo);
					wechatAuth.setPersonInfo(personInfo);
					if (effectedNum <= 0) {
						throw new WechatOperationExecution("添加用户信息失败");
					}
				} catch (Exception e) {
					log.error("insertPersonInfo error:" + e.toString());
					throw new WechatOperationExecution(
							"insertPersonInfo error: " + e.getMessage());
				}
			}

			// 创建专属于本平台的微信账号
			int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
			if (effectedNum <= 0) {
				throw new WechatOperationExecution("账号创建失败");
			} else {
				return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS,
						wechatAuth);
			}

		} catch (Exception e) {
			log.error("insertWechatAuth error:" + e.toString());
			throw new WechatOperationExecution("insertWechatAuth error: "
					+ e.getMessage());
		}

	}

}
