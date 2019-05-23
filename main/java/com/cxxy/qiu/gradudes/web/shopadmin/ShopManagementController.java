package com.cxxy.qiu.gradudes.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.cxxy.qiu.gradudes.dto.ImageHolder;
import com.cxxy.qiu.gradudes.dto.ShopExecution;
import com.cxxy.qiu.gradudes.entity.Area;
import com.cxxy.qiu.gradudes.entity.PersonInfo;
import com.cxxy.qiu.gradudes.entity.Shop;
import com.cxxy.qiu.gradudes.entity.ShopCategory;
import com.cxxy.qiu.gradudes.enums.ShopStateEnum;
import com.cxxy.qiu.gradudes.service.AreaService;
import com.cxxy.qiu.gradudes.service.ShopCategoryService;
import com.cxxy.qiu.gradudes.service.ShopService;
import com.cxxy.qiu.gradudes.util.CodeUtil;
import com.cxxy.qiu.gradudes.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	

	//是否有权登录操作
	@RequestMapping(value="/getshopmanagementinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId<=0){
			Object currentShopObj=request.getSession().getAttribute("currentShop");
	       if(currentShopObj==null){
	    	   modelMap.put("redirect",true);
	    	   modelMap.put("url","/gradudes/shopadmin/shoplist");
	       }else{
	    	   Shop currentShop=(Shop) currentShopObj;
	    	   modelMap.put("redirect",false);
	    	   modelMap.put("shopId",currentShop.getShopId());
	       }
		}else{
			 Shop currentShop= new Shop();
			 currentShop.setShopId(shopId);
			 request.getSession().setAttribute("currentShop", currentShop);
			 modelMap.put("redirect",false);
		}
		return modelMap;
	}
	
	//店铺列表
	@RequestMapping(value="/getshoplist",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user= new PersonInfo();
		//user.setUserId(1L);
//		user.setName("test");
//		request.getSession().setAttribute("user",user);
		user=(PersonInfo) request.getSession().getAttribute("user");
		
		//List<Shop> shopList= new ArrayList<Shop>();
		try{
			Shop shopCondition=new Shop();
			shopCondition.setOwner(user);
			ShopExecution se =shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("shopList",se.getShopList());
			//列出店铺成功之后，将店铺放入session中作为权限验证依据，即该帐号只能操作它自己的店铺
			request.getSession().setAttribute("shopList",se.getShopList());	
			modelMap.put("user",user);
			modelMap.put("success",true);
		}catch(Exception e){
			modelMap.put("success",false);
			modelMap.put("errMsg",e.getMessage());
		}
		return modelMap;
	}
	
	
   //更新店铺
	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyshop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
	
		// 1.接受并转化相应的参数，包括店铺信息以及图片信息	
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
		  
			shop = mapper.readValue(shopStr,Shop.class);
			
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		// 文件解析器
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest
					.getFile("shopImg");
		} 
		
		// 2.修改店铺
		if (shop != null && shop.getShopId()!=null) {

			ShopExecution se;
			try {
				if(shopImg==null){
					se = shopService.modifyShop(shop,null);
				}else{
					ImageHolder imageHolder= new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
					se = shopService.modifyShop(shop,imageHolder);
				}
				
				if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {

					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg",e.getMessage());
			}
			
			// 3.返回结果
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺id");
			return modelMap;
		}

	}
	
	//依据店铺id查询并返回给前台
	@RequestMapping(value="/getshopbyid",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId=HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId>-1){
			try{
			Shop shop=shopService.getByShopId(shopId);
			List<Area>areaList=areaService.getAreaList();
			modelMap.put("shop", shop);
			modelMap.put("areaList",areaList);
			modelMap.put("success", true);
			}catch(Exception e){
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
			
		}
		return modelMap;
	}
	
	//查询到的区域和店铺分类返回给前台
	@RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo() {

		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try {
			shopCategoryList = shopCategoryService
					.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		
		// 1.接受并转化相应的参数，包括店铺信息以及图片信息	
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
		
			shop = mapper.readValue(shopStr,Shop.class);
			
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		// 文件解析器
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest
					.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		
		// 2.注册店铺
		if (shop != null && shopImg != null) {

			
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
		
			ShopExecution se;
			try {
				ImageHolder imageHolder= new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
				se = shopService.addShop(shop,imageHolder );
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					//该用户可以操作的店铺列表
					@SuppressWarnings("unchecked")
					List<Shop> shopList=(List<Shop>) request.getSession().getAttribute("shopList");
					if(shopList==null||shopList.size()==0){
						shopList=new ArrayList<Shop>();		
					}
					shopList.add(se.getShop());
					//将shopList设置到session方便调用
					request.getSession().setAttribute("shopList",shopList);								
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg",e.getMessage());
			}
			
			// 3.返回结果
			return modelMap;
		} else {
			System.out.println(shop);
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}

	}
}
