package com.cxxy.qiu.gradudes.service.impl;

import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxxy.qiu.gradudes.dao.PersonInfoDao;
import com.cxxy.qiu.gradudes.dao.ShopDao;
import com.cxxy.qiu.gradudes.dao.UserProductMapDao;
import com.cxxy.qiu.gradudes.dao.UserShopMapDao;
import com.cxxy.qiu.gradudes.dto.UserProductMapExecution;
import com.cxxy.qiu.gradudes.entity.PersonInfo;
import com.cxxy.qiu.gradudes.entity.Shop;
import com.cxxy.qiu.gradudes.entity.UserProductMap;
import com.cxxy.qiu.gradudes.entity.UserShopMap;
import com.cxxy.qiu.gradudes.enums.UserProductMapStateEnum;
import com.cxxy.qiu.gradudes.exceptions.UserProductMapOperationException;
import com.cxxy.qiu.gradudes.service.UserProductMapService;
import com.cxxy.qiu.gradudes.util.PageCalculator;

@Service
public class UserProductMapServiceImpl implements UserProductMapService {

	@Autowired
	private UserProductMapDao userProductMapDao;
	@Autowired
	private UserShopMapDao userShopMapDao;
	@Autowired
	private PersonInfoDao personInfoDao;
	@Autowired
	private ShopDao shopDao;

	@Override
	public UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex,
			Integer pageSize) {

		// 空值判断
		if (userProductCondition != null && pageIndex != null && pageSize != null) {
			// 页转行
			int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
			// 依据查询条件分页取出列表
			List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMapList(userProductCondition,
					beginIndex, pageSize);
			// 按照同等的查询条件获取总数
			int count = userProductMapDao.queryUserProductMapCount(userProductCondition);
			UserProductMapExecution se = new UserProductMapExecution();
			se.setUserProductMapList(userProductMapList);
			se.setCount(count);
			return se;
		} else {
			return null;
		}
	}

	/*
	 * 添加消费记录
	 */
	@Override
	@Transactional
	public UserProductMapExecution addUserProductMap(UserProductMap userProductMap)
			throws UserProductMapOperationException {
		// 空值判断，主要确保顾客Id,店铺Id以及操作员id非空
		if (userProductMap != null && userProductMap.getUser().getUserId() != null
				&& userProductMap.getShop().getShopId() != null && userProductMap.getOperator().getUserId() != null) {
			// 设定默认值
		userProductMap.setCreateTime(new Date());
			try {
				// 添加消费记录
				int effectedNum = userProductMapDao.insertUserProductMap(userProductMap);
				if (effectedNum <= 0) {
					throw new UserProductMapOperationException("添加消费记录失败");
				}
				// 若本次消费能够积分
				if (userProductMap.getPoint() != null && userProductMap.getPoint() > 0) {
					UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userProductMap.getUser().getUserId(),
							userProductMap.getShop().getShopId());
					if (userShopMap != null && userShopMap.getUserShopId() != null) {
						// 若之前消费过，即有过积分记录，则进行总积分的更新
						userShopMap.setPoint(userShopMap.getPoint() + userProductMap.getPoint());
						effectedNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
						if (effectedNum <= 0) {
							throw new UserProductMapOperationException("更新积分信息失败");
						}
					}else {
						userShopMap=compactUserShopMap4Add(userProductMap.getUser().getUserId(),userProductMap.getShop().getShopId(),
								userProductMap.getPoint());
						effectedNum=userShopMapDao.insertUserShopMap(userShopMap);
						if (effectedNum <= 0) {
							throw new UserProductMapOperationException("积分信息创建失败");
						}
					}
				}
				return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS, userProductMap);
			} catch (Exception e) {
				throw new UserProductMapOperationException("添加授权失败:" + e.toString());
			}
		}else {
			return new UserProductMapExecution(UserProductMapStateEnum.NULL_USERPRODUCT_INFO);
		}

	}

	private UserShopMap compactUserShopMap4Add(Long userId, Long shopId, Integer point) {
		UserShopMap userShopMap = null;
		if (userId != null && shopId != null) {
			userShopMap = new UserShopMap();
			PersonInfo customer=new PersonInfo();
			customer.setUserId(userId);
			Shop shop =new Shop();
			shop.setShopId(shopId);
			userShopMap.setUser(customer);
			userShopMap.setShop(shop);
			userShopMap.setCreateTime(new Date());
			userShopMap.setPoint(point);
		}
		return userShopMap;
	}

}
