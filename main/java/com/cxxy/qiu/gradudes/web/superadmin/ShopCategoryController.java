package com.cxxy.qiu.gradudes.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.cxxy.qiu.gradudes.dto.ImageHolder;
import com.cxxy.qiu.gradudes.dto.ShopCategoryExecution;
import com.cxxy.qiu.gradudes.entity.ShopCategory;
import com.cxxy.qiu.gradudes.enums.ShopCategoryStateEnum;
import com.cxxy.qiu.gradudes.service.ShopCategoryService;
import com.cxxy.qiu.gradudes.util.CodeUtil;
import com.cxxy.qiu.gradudes.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/superadmin")
public class ShopCategoryController {

	@Autowired
	private ShopCategoryService shopCategoryService;

	@RequestMapping(value = "/modifyshopcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShopcategory(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 1.接受并转化相应的参数，包括店铺类别信息以及图片信息
		String shopCategoryStr = HttpServletRequestUtil.getString(request, "shopCategoryStr");

		ObjectMapper mapper = new ObjectMapper();
		ShopCategory shopCategory = null;
		try {

			shopCategory = mapper.readValue(shopCategoryStr, ShopCategory.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopCategoryImg = null;
		// 文件解析器
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopCategoryImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopCategoryImg");
		}
		// 修改店铺类别
		if (shopCategory != null && shopCategory.getShopCategoryId() != null) {
			ShopCategoryExecution sce;
			try {
				if (shopCategoryImg == null) {
					sce = shopCategoryService.modifyShopCategory(shopCategory,null);
				} else {
					ImageHolder imageHolder = new ImageHolder(shopCategoryImg.getOriginalFilename(),
							shopCategoryImg.getInputStream());
					sce = shopCategoryService.modifyShopCategory(shopCategory, imageHolder);
				}
				if (sce.getState() == ShopCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {

					modelMap.put("success", false);
					modelMap.put("errMsg", sce.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			// 3.返回结果
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺类别id");
			return modelMap;
		}
	}

	@RequestMapping(value = "/getparentcategorylist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getParentCategoryInfo() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		try {
			// 获取一级店铺类别列表（既parendid为空的ShopCategory）
			shopCategoryList = shopCategoryService.getShopCategoryList(null);
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		return modelMap;
	}

	@RequestMapping(value = "/getchildcategorylist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getChildCategoryInfo() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		try {
			// 获取一级店铺类别列表（既parendid为空的ShopCategory）
			ShopCategory shopCategoryCondition = new ShopCategory();
			shopCategoryCondition.setShopCategoryDesc("ALLSECOND");
			shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
			ShopCategory parentShopCategory = new ShopCategory();
			for (ShopCategory shopCategory : shopCategoryList) {
				parentShopCategory = shopCategoryService.getShopParentCategoryById(shopCategory.getShopCategoryId());
				shopCategory.setParent(parentShopCategory);
			}
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		return modelMap;
	}

	@RequestMapping(value = "/removecategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeShopCategory(Long shopCategoryId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (shopCategoryId != null && shopCategoryId > 0) {
			try {
				ShopCategoryExecution sce = shopCategoryService.removeShopCategory(shopCategoryId);
				if (sce.getState() == ShopCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", sce.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "店铺类别信息下有子类别");
		}
		return modelMap;
	}

	@RequestMapping(value = "/addshopcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addParentCategory(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 1.接受并转化相应的参数，包括店铺类别信息以及图片信息
		String shopCategoryStr = HttpServletRequestUtil.getString(request, "shopCategoryStr");

		ObjectMapper mapper = new ObjectMapper();
		ShopCategory shopCategory = null;
		try {

			shopCategory = mapper.readValue(shopCategoryStr, ShopCategory.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopCategoryImg = null;
		// 文件解析器
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopCategoryImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopCategoryImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		// 添加店铺类别子类
		if (shopCategory != null && shopCategory.getParent() != null
				&& shopCategory.getParent().getShopCategoryId() != null) {
			ShopCategoryExecution sce;
			try {

				sce = shopCategoryService.insertShopCategory(shopCategory, null);
				if (sce.getState() == ShopCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", sce.getStateInfo());
				}

			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			// 3.返回结果
			return modelMap;
		}
		// 添加店铺类别父类
		if (shopCategory != null && shopCategoryImg != null) {
			ShopCategoryExecution sce;
			try {
				ImageHolder imageHolder = new ImageHolder(shopCategoryImg.getOriginalFilename(),
						shopCategoryImg.getInputStream());

				sce = shopCategoryService.insertShopCategory(shopCategory, imageHolder);
				if (sce.getState() == ShopCategoryStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", sce.getStateInfo());
				}

			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			// 3.返回结果
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输头条信息");
			return modelMap;
		}
	}

	@RequestMapping(value = "/getshopcategorybyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopcategoryById(@RequestParam Long shopCategoryId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 非空判断
		if (shopCategoryId > -1) {
			// 获取店铺类别信息
			ShopCategory shopCategory = shopCategoryService.getShopCategoryById(shopCategoryId);
			ShopCategory parentCategory = shopCategoryService.getShopParentCategoryById(shopCategoryId);
			if (parentCategory != null) {
				shopCategory.setParent(parentCategory);
			}
			modelMap.put("shopCategory", shopCategory);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty lineId");
		}
		return modelMap;
	}
}
