package com.cxxy.qiu.gradudes.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxxy.qiu.gradudes.dao.UserAwardMapDao;
import com.cxxy.qiu.gradudes.dao.UserShopMapDao;
import com.cxxy.qiu.gradudes.dto.UserAwardMapExecution;
import com.cxxy.qiu.gradudes.entity.UserAwardMap;
import com.cxxy.qiu.gradudes.entity.UserShopMap;
import com.cxxy.qiu.gradudes.enums.UserAwardMapStateEnum;
import com.cxxy.qiu.gradudes.exceptions.UserAwardMapOperationException;
import com.cxxy.qiu.gradudes.service.UserAwardMapService;
import com.cxxy.qiu.gradudes.util.PageCalculator;

@Service
public class UserAwardMapServiceImpl implements UserAwardMapService {
	@Autowired
	private UserAwardMapDao userAwardMapDao;
	@Autowired
	private UserShopMapDao userShopMapDao;

	@Override
	public UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex,
			Integer pageSize) {
		// 空值判断
		if (userAwardCondition != null && pageIndex != -1 && pageSize != -1) {
			// 页转行
			int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
			// 根据传入的查询条件分页返回用户奖品的映射信息列表（用户领取奖品的信息列表）
			List<UserAwardMap> UserAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardCondition, beginIndex,
					pageSize);
			// 返回总数
			int count = userAwardMapDao.queryUserAwardMapCount(userAwardCondition);
			UserAwardMapExecution ue = new UserAwardMapExecution();
			ue.setUserAwardMapList(UserAwardMapList);
			ue.setCount(count);
			return ue;
		} else {

			return null;
		}
	}

	@Override
	public UserAwardMap getUserAwardMapById(long userAwardMapId) {
		return userAwardMapDao.queryUserAwardMapById(userAwardMapId);
	}

	@Override
	@Transactional
	public UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {
		// 空值判断
		if (userAwardMap != null && userAwardMap.getUser() != null && userAwardMap.getUser().getUserId() != null
				&& userAwardMap.getShop() != null && userAwardMap.getShop().getShopId() != null) {
			// 设置默认值
			userAwardMap.setCreateTime(new Date());
			userAwardMap.setUsedStatus(0);
			try {
				int effectedNum = 0;
				// 若该奖品需要消耗积分，则将tb_user_shop_map对应的用户积分抵扣
				if (userAwardMap.getPoint() != null && userAwardMap.getPoint() > 0) {
					// 根据用户Id和店铺id获取该用户在店铺的积分
					UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userAwardMap.getUser().getUserId(),
							userAwardMap.getShop().getShopId());
					// 判断该用户在店铺里是否有积分
					if (userShopMap != null) {
						// 若有积分，必须确保店铺积分大于本次要兑换的积分
						if (userShopMap.getPoint() >= userAwardMap.getPoint()) {
							// 积分抵扣
							userShopMap.setPoint(userShopMap.getPoint() - userAwardMap.getPoint());
							// 更新积分信息
							effectedNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
							if (effectedNum <= 0) {

								throw new UserAwardMapOperationException("更新积分信息失败");
							}

						} else {

							throw new UserAwardMapOperationException("积分不足无法领取");
						}

					} else {
						// 在店铺没有积分
						throw new UserAwardMapOperationException("在本店铺没有积分，无法对换奖品");
					}

				}
				// 插入礼品兑换信息
				effectedNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
				if (effectedNum <= 0) {
					throw new RuntimeException("领取奖励失败");
				}
				return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS, userAwardMap);
			} catch (Exception e) {
				throw new UserAwardMapOperationException("领取奖励失败：" + e.toString());
			}
		} else {
			return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERAWARD_INFO);
		}

	}

	@Override
	@Transactional
	public UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {
		//空值判断
		if(userAwardMap==null||userAwardMap.getUserAwardId()==null||userAwardMap.getUsedStatus()==null) {
			return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERAWARD_ID);
		}else {
			try {
				int effectNum=userAwardMapDao.updateUserAwardMap(userAwardMap);
				if(effectNum<=0) {
					return new UserAwardMapExecution(UserAwardMapStateEnum.INNER_ERROR);
				}else {
					return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS,userAwardMap);
				}
			}catch (Exception e) {
				throw new UserAwardMapOperationException("modifyUserAwardMap error:"+e.toString());
			}
		}
	
	}

}
