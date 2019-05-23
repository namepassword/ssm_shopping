package com.cxxy.qiu.gradudes.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {

	// 首页路由
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	private String index() {
		return "frontend/index";
	}

	// 店铺列表路由
	@RequestMapping(value = "/shoplist", method = RequestMethod.GET)
	private String shopList() {
		return "frontend/shoplist";
	}

	// 商店详情路由
	@RequestMapping(value = "/shopdetail", method = RequestMethod.GET)
	private String shopDetail() {
		return "frontend/shopdetail";
	}

	// 店铺奖品列表路由
	@RequestMapping(value = "/awardlist", method = RequestMethod.GET)
	private String awardList() {
		return "frontend/awardlist";
	}

	// 奖品兑换路由
	@RequestMapping(value = "/pointrecord", method = RequestMethod.GET)
	private String pointRecord() {
		return "frontend/pointrecord";
	}

	// 类别详情路由
	@RequestMapping(value = "/productdetail", method = RequestMethod.GET)
	private String ProductDetail() {
		return "frontend/productdetail";
	}

	// 奖品详情页的路由
	@RequestMapping(value = "/myawarddetail", method = RequestMethod.GET)
	private String showMyAward() {
		return "frontend/myawarddetail";
	}

	// 我的积分路由的路由
	@RequestMapping(value = "/myrecord", method = RequestMethod.GET)
	private String showMyRecord() {
		return "frontend/myrecord";
	}
	// 我的积分路由的路由
		@RequestMapping(value = "/mypoint", method = RequestMethod.GET)
		private String showMyPoint() {
			return "frontend/mypoint";
		}
}
