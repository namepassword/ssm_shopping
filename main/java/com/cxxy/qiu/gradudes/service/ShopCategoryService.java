package com.cxxy.qiu.gradudes.service;

import java.util.List;

import com.cxxy.qiu.gradudes.dto.ImageHolder;
import com.cxxy.qiu.gradudes.dto.ShopCategoryExecution;
import com.cxxy.qiu.gradudes.entity.ShopCategory;

public interface ShopCategoryService {

	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);

	ShopCategory getShopCategoryById(Long shopCategoryId);

	ShopCategory getShopParentCategoryById(Long shopChildCategoryId);
	
	ShopCategoryExecution removeShopCategory(Long shopCategoryId);

	ShopCategoryExecution insertShopCategory(ShopCategory shopCategory, ImageHolder thumbnail);

	ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory, ImageHolder thumbnail);

}
