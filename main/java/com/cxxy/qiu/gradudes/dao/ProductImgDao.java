package com.cxxy.qiu.gradudes.dao;

import java.util.List;

import com.cxxy.qiu.gradudes.entity.ProductImg;

public interface ProductImgDao {
    /*
     *批量添加商品详情图片
     * 
     */
	int  batchInsertProductImg(List<ProductImg> productImgHolderList);
	 /*
     *删除指定商品下的所有详情图
     * 
     */
	int deleteProductImgByProductId(long productId);
	
	List<ProductImg> queryProductImgList(long productId);

}
