package com.cxxy.qiu.gradudes.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*
 * spring-web 视图解析器的作用
 * */
@Controller
@RequestMapping(value = "/shopadmin", method = { RequestMethod.GET })
public class ShopAdminController {

	// 转发至店铺注册/编辑页面
	@RequestMapping(value = "/shopoperation")
	public String shopOperation() {
		return "shop/shopoperation";
	}

	// 转发至店铺列表页面
	@RequestMapping(value = "/shoplist")
	public String shopList() {
		return "shop/shoplist";
	}

	// 转发至店铺管理页面
	@RequestMapping(value = "/shopmanagement")
	public String shopManagement() {
		return "shop/shopmanagement";
	}

	// 转发至商品类别管理页面
	@RequestMapping(value = "/productcategorymanagement")
	public String productCategoryManage() {
		return "shop/productcategorymanage";
	}

	// 转发至商品添加/编辑页面
	@RequestMapping(value = "/productoperation")
	public String productOperation() {
		return "shop/productoperation";
	}

	// 转发至商品管理页面
	@RequestMapping(value = "/productmanagement")
	public String productManagement() {
		return "shop/productmanagement";
	}

	// 转发至 店铺授权页面
	@RequestMapping(value = "/shopauthmanagement")
	public String shopAuthManagement() {
		return "shop/shopauthmanagement";
	}

	// 转发至 店铺授权页面
	@RequestMapping(value = "/shopauthedit")
	public String shopAuthEdit() {
		return "shop/shopauthedit";
	}

	// 转发至操作失败页面
	@RequestMapping(value = "/operationfail")
	public String operationFail() {
		return "shop/operationfail";
	}

	// 转发至操作成功页面
	@RequestMapping(value = "/operationsuccess")
	public String operationSuccess() {
		return "shop/operationsuccess";
	}

	// 转发至店铺消费记录的页面
	@RequestMapping(value = "/productbuycheck")
	public String productByCheck() {
		return "shop/productbuycheck";
	}

	// 转发至店铺用户积分统计的页面
	@RequestMapping(value = "/usershopcheck")
	public String userShopCheck() {
		return "shop/usershopcheck";
	}

	// 转发至店铺用户积分兑换的页面
	@RequestMapping(value = "/awarddelivercheck")
	public String awardDeliverCheck() {
		return "shop/awarddelivercheck";
	}

	// 转发至店铺用户奖品管理的页面
	@RequestMapping(value = "/awardmanagement")
	public String awardManagement() {
		return "shop/awardmanagement";
	}
	// 转发至店铺用户奖品添加和编辑的页面
		@RequestMapping(value = "/awardoperation")
		public String awardEdit() {
			return "shop/awardoperation";
		}
		@RequestMapping(value = "/shopawarddetail")
		public String shopAwardDetail() {
			return "shop/shopawarddetail";
		}
}
