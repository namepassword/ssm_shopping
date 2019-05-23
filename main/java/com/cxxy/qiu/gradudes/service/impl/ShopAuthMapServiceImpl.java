package com.cxxy.qiu.gradudes.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxxy.qiu.gradudes.dao.ShopAuthMapDao;
import com.cxxy.qiu.gradudes.dto.ShopAuthMapExecution;
import com.cxxy.qiu.gradudes.entity.ShopAuthMap;
import com.cxxy.qiu.gradudes.enums.ShopAuthMapStateEnum;
import com.cxxy.qiu.gradudes.service.ShopAuthMapService;
import com.cxxy.qiu.gradudes.util.PageCalculator;



@Service
public class ShopAuthMapServiceImpl implements ShopAuthMapService {
	@Autowired
	private ShopAuthMapDao shopAuthMapDao;

	@Override
	public ShopAuthMapExecution listShopAuthMapByShopId(Long shopId,
			Integer pageIndex, Integer pageSize) {
		//空值判断
		if (shopId != null && pageIndex != null && pageSize != null) {
			//页转行
			int beginIndex = PageCalculator.calculateRowIndex(pageIndex,
					pageSize);
			//查询返回该店铺授权信息列表
			List<ShopAuthMap> shopAuthMapList = shopAuthMapDao
					.queryShopAuthMapListByShopId(shopId, beginIndex, pageSize);
			//返回总数
			int count = shopAuthMapDao.queryShopAuthCountByShopId(shopId);
			ShopAuthMapExecution se = new ShopAuthMapExecution();
			se.setShopAuthMapList(shopAuthMapList);
			se.setCount(count);
			return se;
		} else {
			return null;
		}

	}
	@Override
	public ShopAuthMap getShopAuthMapById(Long shopAuthId) {
		return shopAuthMapDao.queryShopAuthMapById(shopAuthId);
	}
	
	@Override
	@Transactional
	public ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap)
			throws RuntimeException {
		//空值判断
		if (shopAuthMap != null && shopAuthMap.getShop() != null&&shopAuthMap.getShop().getShopId()!=null
			&&shopAuthMap.getEmployee()!=null&&shopAuthMap.getEmployee().getUserId()!=null	) {
			shopAuthMap.setCreateTime(new Date());
			shopAuthMap.setLastEditTime(new Date());
			shopAuthMap.setEnableStatus(1);
			try {
				//添加授权信息
				int effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
				if (effectedNum <= 0) {
					throw new RuntimeException("添加授权失败");
				}
				return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS,
						shopAuthMap);
			} catch (Exception e) {
				throw new RuntimeException("添加授权失败:" + e.toString());
			}
		} else {
			return new ShopAuthMapExecution(
					ShopAuthMapStateEnum.NULL_SHOPAUTH_INFO);
		}
	}

	@Override
	@Transactional
	public ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap)
			throws RuntimeException {
		//空值判断
		if (shopAuthMap == null || shopAuthMap.getShopAuthId() == null) {
			return new ShopAuthMapExecution(
					ShopAuthMapStateEnum.NULL_SHOPAUTH_ID);
		} else {
			try {
				int effectedNum = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
				if (effectedNum <= 0) {
					return new ShopAuthMapExecution(
							ShopAuthMapStateEnum.INNER_ERROR);
				} else {// 创建成功
					return new ShopAuthMapExecution(
							ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
				}
			} catch (Exception e) {
				throw new RuntimeException("updateShopByOwner error: "
						+ e.getMessage());
			}
		}
	}

	@Override
	public ShopAuthMapExecution removeShopAuthMap(Long shopAuthMapId)
			throws RuntimeException {
		return null;
	}

	

}
