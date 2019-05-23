package com.cxxy.qiu.gradudes.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxxy.qiu.gradudes.dao.ShopCategoryDao;
import com.cxxy.qiu.gradudes.dto.ImageHolder;
import com.cxxy.qiu.gradudes.dto.ShopCategoryExecution;
import com.cxxy.qiu.gradudes.entity.ShopCategory;
import com.cxxy.qiu.gradudes.enums.ShopCategoryStateEnum;
import com.cxxy.qiu.gradudes.exceptions.ShopCategoryOperationExecution;
import com.cxxy.qiu.gradudes.service.ShopCategoryService;
import com.cxxy.qiu.gradudes.util.ImageUtil;
import com.cxxy.qiu.gradudes.util.PathUtil;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

	@Autowired
	private ShopCategoryDao shopCategoryDao;

	@Override
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
		
		
		return shopCategoryDao.queryShopCategory(shopCategoryCondition);

	}

	@Override
	@Transactional
	public ShopCategoryExecution removeShopCategory(Long shopCategoryId) {
		if (shopCategoryId > 0) {
			try {
				ShopCategory tempShopCategory = shopCategoryDao.queryShopCategoryById(shopCategoryId);
				if (tempShopCategory.getShopCategoryImg() != null) {
					ImageUtil.deleteFileOrPath(tempShopCategory.getShopCategoryImg());
				}
				int effectedNum = shopCategoryDao.deleteShopCategoryById(shopCategoryId);
				if (effectedNum > 0) {

					return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS);
				} else {
					return new ShopCategoryExecution(ShopCategoryStateEnum.INNER_ERROR);
				}
			} catch (Exception e) {
				throw new ShopCategoryOperationExecution("店铺类别信息下有子类别");
			}
		} else {
			return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
		}
	}

	@Override
	public ShopCategory getShopCategoryById(Long shopCategoryId) {

		return shopCategoryDao.queryShopCategoryById(shopCategoryId);

	}
	@Override
	public ShopCategory getShopParentCategoryById(Long shopChildCategoryId) {
		return shopCategoryDao.queryShopParentCategoryById(shopChildCategoryId);
	}

	@Override
	@Transactional
	public ShopCategoryExecution insertShopCategory(ShopCategory shopCategory,ImageHolder thumbnail) {
		
		// 空值判断
		if (shopCategory == null) {
			return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
		}
		try {
			shopCategory.setCreateTime(new Date());
			shopCategory.setLastEditTime(new Date());
			//添加店铺类别子类别
			if(thumbnail==null) {
				try {
					int effectedNum = shopCategoryDao.insertShopCategory(shopCategory);
					if (effectedNum <= 0) {
						throw new ShopCategoryOperationExecution("店铺类别父类别添加失败");
					}
				} catch (Exception e) {
					throw new ShopCategoryOperationExecution("addParentShopCategory error" + e.getMessage());
				}
			}
			// 添加店铺类别信息父类别
			else if (thumbnail.getImage() != null) {
				try {
					// 存储图片
					addShopCategoryImg(shopCategory, thumbnail);
					int effectedNum = shopCategoryDao.insertShopCategory(shopCategory);
					if (effectedNum <= 0) {
						throw new ShopCategoryOperationExecution("店铺类别父类别添加失败");
					}
				} catch (Exception e) {
					throw new ShopCategoryOperationExecution("addParentShopCategory error" + e.getMessage());
				}

			}
			
		} catch (Exception e) {
			throw new ShopCategoryOperationExecution("addShopCategory error" + e.getMessage());
		}
		return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS, shopCategory);
	}

	private void addShopCategoryImg(ShopCategory shopCategory, ImageHolder thumbnail) {
		// 获取shop图片目录的相对值路径
		String dest = PathUtil.getShopCategoryImagePath();

		String shopCategoryImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		shopCategory.setShopCategoryImg(shopCategoryImgAddr);
	}

	@Override
	@Transactional
	public ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory, ImageHolder thumbnail) {
		if (shopCategory.getShopCategoryId() == null && shopCategory.getShopCategoryId()==null) {
			return new ShopCategoryExecution(ShopCategoryStateEnum.INNER_ERROR);
		}else if(thumbnail==null){
			try {
				
				
				// 更新商店类别信息
				shopCategory.setLastEditTime(new Date());
				int effectedNum = shopCategoryDao.updateShopCategory(shopCategory);
				if (effectedNum <= 0) {
					return new ShopCategoryExecution(ShopCategoryStateEnum.INNER_ERROR);
				} else {
					shopCategory = shopCategoryDao.queryShopCategoryById(shopCategory.getShopCategoryId());
					return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS, shopCategory);
				}
			} catch (Exception e) {
				throw new ShopCategoryOperationExecution("modifyShopCategory error:" + e.getMessage());
			}
		} 
		else {
			// 1.判断是否需要处理图片
			try {
				if (thumbnail.getImage() != null && thumbnail.getImageName() != null
						&& !"".equals(thumbnail.getImageName())) {
					ShopCategory tempShopCategory = shopCategoryDao
							.queryShopCategoryById(shopCategory.getShopCategoryId());
					if (tempShopCategory.getShopCategoryImg() != null) {
						ImageUtil.deleteFileOrPath(tempShopCategory.getShopCategoryImg());
					}
					addShopCategoryImg(shopCategory, thumbnail);
				}
				
				// 更新商店类别信息
				shopCategory.setLastEditTime(new Date());
				int effectedNum = shopCategoryDao.updateShopCategory(shopCategory);
				if (effectedNum <= 0) {
					return new ShopCategoryExecution(ShopCategoryStateEnum.INNER_ERROR);
				} else {
					shopCategory = shopCategoryDao.queryShopCategoryById(shopCategory.getShopCategoryId());
					return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS, shopCategory);
				}
			} catch (Exception e) {
				throw new ShopCategoryOperationExecution("modifyShopCategory error:" + e.getMessage());
			}
		}
	}





}
