package com.cxxy.qiu.gradudes.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cxxy.qiu.gradudes.entity.Shop;

public interface ShopDao {

	/*
	 * 分页查询店铺，可输入的条件有： 店铺名，店铺状态，店铺类别，曲区域id，owner
	 * 
	 */
	List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,
			                @Param("rowIndex")int rowIndex,@Param("pageSize") int pageSize);
	/*
	 * 返回queryShopList的总数
	 * 
	 */
	int queryShopCount(@Param("shopCondition")Shop shopCondition);
	/*
	 * 新增店铺
	 * 
	 */
	int insertShop(Shop shop);
	/*
	 * 更新店铺
	 *
	 */
	int updateShop(Shop shop);
	/*
	 *查询店铺
	 * 
	 */
	Shop queryByShopId(long shopId);
	
}
