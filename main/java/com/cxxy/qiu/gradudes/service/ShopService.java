package com.cxxy.qiu.gradudes.service;


import com.cxxy.qiu.gradudes.dto.ImageHolder;
import com.cxxy.qiu.gradudes.dto.ShopExecution;
import com.cxxy.qiu.gradudes.entity.Shop;
import com.cxxy.qiu.gradudes.exceptions.ShopOperationException;

public interface ShopService {
	/*
     *根据shopCondition分页返回相应店铺列表
     * 
     */
	ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);
	/*
     *
     * 
     */
	
	
	/*更新店铺
	 * */
	ShopExecution modifyShop(Shop shop,ImageHolder thumbnail) throws ShopOperationException;
	/*
	 *查询店铺 
	 */
	Shop getByShopId(long shopId);
    /*
     *添加店铺
     * 
     */
	ShopExecution addShop(Shop shop,ImageHolder thumbnail )throws ShopOperationException ;
	
}
