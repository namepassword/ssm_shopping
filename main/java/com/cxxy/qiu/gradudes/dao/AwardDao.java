package com.cxxy.qiu.gradudes.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cxxy.qiu.gradudes.entity.Award;


public interface AwardDao {

	
	/*
	 * 依据传入的查询条件分页显示奖品信息列表
	 * 
	 * */
	List<Award> queryAwardList(@Param("awardCondition") Award awardCondition, @Param("rowIndex")int rowIndex,
			@Param("pageSize") int pageSize);
	/*
	 * 配合queryAwardList返回相同查询条件下的奖品数
	 * 
	 * */
	int queryAwardCount(@Param("awardCondition") Award awardCondition);
	/*
	 * 通过AwardId查询奖品信息
	 * 
	 */
	Award queryAwardByAwardId(long awardId);
	/*
	 * 添加奖品信息
	 * 
	 */
	int insertAward(Award award);
	/*
	 *更新奖品信息
	 * 
	 */
	int updateAward(Award award);
	/*
	 * 删除奖品信息
	 * 
	 */
	int deleteAward(@Param("awardId") long awardId,@Param("shopId") long shopId);
}
