package com.cxxy.qiu.gradudes.service;

import java.util.List;

import com.cxxy.qiu.gradudes.dto.ImageHolder;
import com.cxxy.qiu.gradudes.dto.ProductExecution;
import com.cxxy.qiu.gradudes.entity.Product;
import com.cxxy.qiu.gradudes.exceptions.ProductOperationException;

public interface ProductService {

	/*
	 * 添加商品信息以及图片处理
	 * 
	 */
	ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList ) throws ProductOperationException;
	/*
	 * 通过商品Id查询唯一的商品信息
	 * 
	 */
	Product getProductById(long productId);
	
	/*
	 * 更新信息以及图片处理
	 * 
	 */
	ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList ) throws ProductOperationException;
	 /*
     *查询商品列表并分页，可输入的条件有：商品名，商品装态，店铺Id,店铺类别
     * 
     */
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

}
