package com.cxxy.qiu.gradudes.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cxxy.qiu.gradudes.entity.ProductCategory;

public interface ProductCategoryDao {
/*
 *刪除指定商品类别
 * 
 */
	int deleteProductCategory(@Param("productCategoryId") long productCategoryId,@Param("shopId") long shopId);
	
	/**
	 * 通过shop id查询店铺商品类别
	 * 
	 */
	List<ProductCategory> queryProductCategoryList(long shopId);
	
	/**
	 *批量新增商品类别
	 */
	int batchInsertProductCategory(List<ProductCategory>productCategoryList);
}
