package com.cxxy.qiu.gradudes.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cxxy.qiu.gradudes.entity.HeadLine;
import com.cxxy.qiu.gradudes.entity.Shop;



public interface HeadLineDao {

	
	/**
	 *根据传入的查询条件
	 *
	 */
	List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);
	
	
	/*
	 * 通过头条Id获取头条信息
	 * 
	 * */
	HeadLine queryHeadLineById(long lineId);
	
	/*
	 * 添加头条信息
	 * */
	int insertHeadLine(HeadLine headLine);
	/*
	 * 更新头条信息
	 * */
	int updateHeadLine(HeadLine headLine);
	/*
	 * 删除头条信息
	 * 
	 * */
	int deleteHeadLine(long headLineId);
}
