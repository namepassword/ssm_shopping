package com.cxxy.qiu.gradudes.service;



import com.cxxy.qiu.gradudes.dto.AwardExecution;
import com.cxxy.qiu.gradudes.dto.ImageHolder;
import com.cxxy.qiu.gradudes.entity.Award;

public interface AwardService {
	
	/*
	 * 根据传入的条件分页返回奖品列表，并返回该查询条件下的总数
	 * */
	AwardExecution getAwardList(Award awardCondition, int pageIndex,
			int pageSize);
	/*
	 * 根据awardId查询奖品信息
	 * 
	 * */
	Award getAwardById(long awardId);
	/*
	 * 添加奖品信息，并添加图片
	 * 
	 * */
	AwardExecution addAward(Award award, ImageHolder thumbnail);
	
	/*
	 * 修改奖品信息
	 * */
	AwardExecution modifyAward(Award award, ImageHolder thumbnail);
}
