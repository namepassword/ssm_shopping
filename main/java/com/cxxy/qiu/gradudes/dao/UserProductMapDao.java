package com.cxxy.qiu.gradudes.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cxxy.qiu.gradudes.entity.UserProductMap;

public interface UserProductMapDao {
 /*
  * 根据查询条件分页返回用户购买商品的记录列表
  * 
  * */
	List<UserProductMap> queryUserProductMapList(@Param("userProductCondition")UserProductMap userProductCondition,@Param("rowIndex")int rowIndex,
			@Param("pageSize") int pageSize);
	/*
	  * 配合queryUserProductMapList根据相同的查询条件返回用户购买商品的记录总数
	  * 
	  * */
	int queryUserProductMapCount(@Param("userProductCondition")UserProductMap userProductCondition);
	
	/*
	  * 添加一条用户购买商品的记录
	  * 
	  * */
	int insertUserProductMap(UserProductMap userProductMap);
	
}

