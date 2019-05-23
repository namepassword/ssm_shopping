package com.cxxy.qiu.gradudes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxxy.qiu.gradudes.dao.ProductCategoryDao;
import com.cxxy.qiu.gradudes.dao.ProductDao;
import com.cxxy.qiu.gradudes.dto.ProductCategoryExecution;
import com.cxxy.qiu.gradudes.entity.ProductCategory;
import com.cxxy.qiu.gradudes.enums.ProductCategoryStateEnum;
import com.cxxy.qiu.gradudes.exceptions.ProductCategoryOperationExecution;
import com.cxxy.qiu.gradudes.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{

	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Autowired
	private ProductDao productDao;
	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {

		return productCategoryDao.queryProductCategoryList(shopId);
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(
			List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationExecution {

	     if(productCategoryList!=null && productCategoryList.size()>0 ){
	    	 try{

	    		 int effectedNum=productCategoryDao.batchInsertProductCategory(productCategoryList);
	    		 if(effectedNum<=0){
	    			 throw new ProductCategoryOperationExecution("商品类别创建失败！");
	    		 }
	    		 else{
	    			return  new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
	    		 }
	    	 }catch(Exception e){
	    		 throw new ProductCategoryOperationExecution("batchAddProductCategory error:"+e.getMessage());

	    	 }
	     }else{
	    	 return  new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
	     }
	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(
			long productCategoryId, long shopId)
			throws ProductCategoryOperationExecution {
		// TODO 将此商品类别下的商品的类别id置为空
		try{
			int effectedNum=productDao.updateProductCategoryToNull(productCategoryId);
			if(effectedNum<0){
				throw new RuntimeException("商品类别更新失败");
			}
		}catch(Exception e){
			throw new RuntimeException("deleteProductCategory error:"+e.getMessage());
		}
		try{
			int effectedNum=productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if(effectedNum<0){
				throw new ProductCategoryOperationExecution("商品类别删除失败！");
			}else{
			return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		}catch (Exception e) {
			throw new ProductCategoryOperationExecution("deleteProductCategory error:"+e.getMessage());
		}

	}

}
