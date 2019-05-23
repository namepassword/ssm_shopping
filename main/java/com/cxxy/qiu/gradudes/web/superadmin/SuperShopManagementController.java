package com.cxxy.qiu.gradudes.web.superadmin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxxy.qiu.gradudes.dto.ImageHolder;
import com.cxxy.qiu.gradudes.dto.ShopExecution;
import com.cxxy.qiu.gradudes.entity.Shop;
import com.cxxy.qiu.gradudes.enums.ShopStateEnum;
import com.cxxy.qiu.gradudes.service.ShopService;
import com.cxxy.qiu.gradudes.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/superadmin")
public class SuperShopManagementController {

	@Autowired
	private ShopService shopService;

	// 店铺审核通过列表
	@RequestMapping(value = "/getuseshoplist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getSuccessShopList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		try {
			Shop shopCondition = new Shop();
			shopCondition.setEnableStatus(1);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	// 店铺待审核列表
	@RequestMapping(value = "/getusefulshoplist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getFailShopList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		try {
			Shop shopCondition = new Shop();
			shopCondition.setEnableStatus(0);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	// 店铺待审核列表
	@RequestMapping(value = "/getillegalshoplist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getIllegalShopList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		try {
			Shop shopCondition = new Shop();
			shopCondition.setEnableStatus(-1);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	// 更新店铺
	@RequestMapping(value = "/modifyshopsuper", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyshop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 1.接受并转化相应的参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {

			shop = mapper.readValue(shopStr, Shop.class);

		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		// 2.修改店铺
		if (shop != null && shop.getShopId() != null && shop.getEnableStatus().equals(1)) {

			shop.setEnableStatus(-1);
			return modifyShop(modelMap, shop);
		} else if (shop != null && shop.getShopId() != null && shop.getEnableStatus().equals(0)) {
			shop.setEnableStatus(1);
			return modifyShop(modelMap, shop);
		} else if (shop != null && shop.getShopId() != null && shop.getEnableStatus().equals(-1)) {
			shop.setEnableStatus(1);
			return modifyShop(modelMap, shop);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺id");
			return modelMap;
		}
	}

	private Map<String, Object> modifyShop(Map<String, Object> modelMap, Shop shop) {
		ShopExecution se;
		try {
			se = shopService.modifyShop(shop, null);
			if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {

				modelMap.put("success", false);
				modelMap.put("errMsg", se.getStateInfo());
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}

		// 3.返回结果
		return modelMap;
	}
}
