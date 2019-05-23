package com.cxxy.qiu.gradudes.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cxxy.qiu.gradudes.entity.ShopAuthMap;

public interface ShopAuthMapDao {

	/*
	 * 分页列出店铺下面的授权信息
	 */
	List<ShopAuthMap> queryShopAuthMapListByShopId(
			@Param("shopId") long shopId, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);

	/*
	 * 获取授权信息
	 */
	int queryShopAuthCountByShopId(@Param("shopId") long shopId);

	/*
	 * 新增一条店铺与店员的授权关系
	 */
	int insertShopAuthMap(ShopAuthMap shopAuthMap);
	/*
	 * 更新授权信息
	 */
   int updateShopAuthMap(ShopAuthMap shopAuthMap);
   /*
    * 对员工除权限
    * 
    * */
   int deleteShopAuthMap(long shopAuthId);
   /*
    * 通过shopAuthId查询员工授权信息
    * 
    * */
   ShopAuthMap queryShopAuthMapById(Long shopAuthId);
}
