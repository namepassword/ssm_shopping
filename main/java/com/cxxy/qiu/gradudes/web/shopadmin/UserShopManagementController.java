package com.cxxy.qiu.gradudes.web.shopadmin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxxy.qiu.gradudes.dto.UserProductMapExecution;
import com.cxxy.qiu.gradudes.dto.UserShopMapExecution;
import com.cxxy.qiu.gradudes.entity.PersonInfo;
import com.cxxy.qiu.gradudes.entity.Shop;
import com.cxxy.qiu.gradudes.entity.UserShopMap;
import com.cxxy.qiu.gradudes.service.UserShopMapService;
import com.cxxy.qiu.gradudes.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class UserShopManagementController {

	@Autowired
	private UserShopMapService userShopMapService;

	@RequestMapping(value = "/listusershopmapbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserShopMapByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 获取当前的店铺信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 空值校验，主要确保shopid不为空
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			UserShopMap userShopMapCondition = new UserShopMap();
			// 添加查询体条件
			userShopMapCondition.setShop(currentShop);
			String userName = HttpServletRequestUtil.getString(request, "userName");
			if(userName!=null) {
				//若传入的顾客名， 则按照顾客名模糊查询
				PersonInfo customer=new PersonInfo();
				customer.setName(userName);
				userShopMapCondition.setUser(customer);
			}
			//分页获取该店铺下的顾客积分列表
			UserShopMapExecution ue=userShopMapService.listUserShopMap(userShopMapCondition,pageIndex,pageSize);
			modelMap.put("userShopMapList", ue.getUserShopMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}

		return modelMap;

	}

}
