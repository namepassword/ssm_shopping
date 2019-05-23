package com.cxxy.qiu.gradudes.service;

import java.util.List;

import com.cxxy.qiu.gradudes.dto.ProductCategoryExecution;
import com.cxxy.qiu.gradudes.entity.ProductCategory;
import com.cxxy.qiu.gradudes.exceptions.ProductCategoryOperationExecution;

public interface ProductCategoryService {

	/**
	 * 
	 *查询指定某个店铺下的所有商品类别信息
	 * 
	 */
	List<ProductCategory> getProductCategoryList(long shopId);
	/**
	 * 
	 *添加商品类别信息
	 * 
	 */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
	throws ProductCategoryOperationExecution;
	
	/**
	 * 
	 *删除商品类别信息
	 * 
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId)
			throws ProductCategoryOperationExecution;
}
