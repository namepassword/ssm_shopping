package com.cxxy.qiu.gradudes.web.superadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/superadmin", method = { RequestMethod.GET })
public class SuperAdminController {
	// 转发至超級管理员页面
	@RequestMapping(value = "/superadminmanagement")
	public String superManagement() {
		return "superadmin/superadminmanagement";
	}

	// 转发至头条页面
	@RequestMapping(value = "/headlinemanagement")
	public String headLine() {
		return "superadmin/headlinemanagement";
	}

	// 转发至头条编辑/添加页面
	@RequestMapping(value = "/headlineoperation")
	public String headLineOperation() {
		return "superadmin/headlineoperation";
	}

	// 转发至商店类别页面
	@RequestMapping(value = "/shopcategorymanagement")
	public String shopCategoryManagement() {
		return "superadmin/shopcategorymanagement";
	}

	// 转发至商店类别页面之父类别
	@RequestMapping(value = "/parentcategorymanagement")
	public String parentCategoryManagement() {
		return "superadmin/parentcategorymanagement";
	}

	// 转发至商店类别页面之子类别
	@RequestMapping(value = "/childcategorymanagement")
	public String childCategoryManagement() {
		return "superadmin/childcategorymanagement";
	}

	// 转发至商店父类别页面之编辑/修改
	@RequestMapping(value = "/shopparentcategoryoperation")
	public String shopParentCategoryOperation() {
		return "superadmin/shopparentcategoryoperation";
	}

	// 转发至商店子类别页面之编辑/修改
	@RequestMapping(value = "/shopchildcategoryoperation")
	public String shopChildCategoryOperation() {
		return "superadmin/shopchildcategoryoperation";
	}

	// 转发至审核通过店铺管理界面
	@RequestMapping(value = "/shopmanagement")
	public String shopManagement() {
		return "superadmin/shopmanagement";
	}

	// 转发至审核通过店铺管理界面
	@RequestMapping(value = "/shopsuccessmanagement")
	public String shopSuccessManagement() {
		return "superadmin/shopsuccessmanagement";
	}

	// 转发至待审核店铺界面
	@RequestMapping(value = "/shopfailmanagement")
	public String shopFailManagement() {
		return "superadmin/shopfailmanagement";
	}
	// 转发至待审核店铺界面
		@RequestMapping(value = "/shopillegalmanagement")
		public String shopIllegalManagement() {
			return "superadmin/shopillegalmanagement";
		}
}
