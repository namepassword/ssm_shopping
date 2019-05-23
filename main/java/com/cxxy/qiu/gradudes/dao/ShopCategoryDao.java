package com.cxxy.qiu.gradudes.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cxxy.qiu.gradudes.entity.ShopCategory;

public interface ShopCategoryDao {

	/*
	 * 查询店铺类别
	 * */
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
    /*
             * 新增店铺类别
     * */
	int insertShopCategory(ShopCategory shopCategory);
	/*
	 * 更新店铺类别信息
	 * 
	 * */
	int updateShopCategory(ShopCategory shopCategory);
	/*
	 * 删除店铺类别
	 * */
	int deleteShopCategoryById(Long shopCategoryId);
	
	/*
	 * 依据店铺类别Id获取店铺信息
	 * 
	 * */
	ShopCategory queryShopCategoryById(Long shopCategoryId);
	/*
	 * 查询父类类别信息
	 * */
	ShopCategory queryShopParentCategoryById(Long shopChildCategoryId);
	
	
}
