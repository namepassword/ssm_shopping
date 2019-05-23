package com.cxxy.qiu.gradudes.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cxxy.qiu.gradudes.entity.Product;

public interface ProductDao {
    /*
     *插入商品 
     * 
     */
	int insertProduct(Product product);
	 /*
     *通过ProdductId查询唯一的商品信息 
     * 
     */
	Product queryProductById(long productId);
	 /*
     *更新商品信息
     * 
     */
	int updateProduct(Product prodcut);
	 /*
     *查询商品列表并分页，可输入的条件有：商品名，商品装态，店铺Id,店铺类别
     * 
     */
	List<Product> queryProductList(
			@Param("productCondition") Product productCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	/**
	 * 查询对应的商品总数
	 * 
	 */
	int queryProductCount(@Param("productCondition") Product productCondition);
	/**
	 * 删除商品类别之前，将商品类别ID置为空
	 * 
	 * @param productCategoryId
	 * @return
	 */
	int updateProductCategoryToNull(long productCategoryId);


}
