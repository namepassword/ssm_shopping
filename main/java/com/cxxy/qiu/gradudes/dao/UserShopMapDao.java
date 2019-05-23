package com.cxxy.qiu.gradudes.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cxxy.qiu.gradudes.entity.UserShopMap;

public interface UserShopMapDao {

	/*
	 * 根据查询条件分页返回用户店铺积分列表
	 * 
	 * */
	List<UserShopMap> queryUserShopMapList(@Param("userShopCondition")UserShopMap userShopCondition,@Param("rowIndex")int rowIndex,
			@Param("pageSize") int pageSize);
	/*
	 * 配合queryUserShopMapList根据相同的条件返回用户店铺积分记录总数
	 * */
	int queryUserShopMapCount(@Param("userShopCondition")UserShopMap userShopCondition);
	/*
	 * 根据传入的用户Id和shopId查询该用户在某个店铺的积分信息
	 * 
	 * */
	UserShopMap queryUserShopMap(@Param("userId")long userId,@Param("shopId")long shopId);
	/*
	 * 添加一条用户店铺的积分记录
	 * 
	 * */
	int insertUserShopMap(UserShopMap userShopMap);
	/*
	 * 更新用户在某店铺的积分
	 * 
	 * */
	int updateUserShopMapPoint(UserShopMap userShopMap);
	
}
