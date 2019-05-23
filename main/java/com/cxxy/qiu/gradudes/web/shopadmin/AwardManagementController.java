package com.cxxy.qiu.gradudes.web.shopadmin;

import java.io.IOException;
import java.util.HashMap;
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

import com.cxxy.qiu.gradudes.dto.AwardExecution;
import com.cxxy.qiu.gradudes.dto.ImageHolder;
import com.cxxy.qiu.gradudes.entity.Award;
import com.cxxy.qiu.gradudes.entity.Shop;
import com.cxxy.qiu.gradudes.entity.UserAwardMap;
import com.cxxy.qiu.gradudes.enums.AwardStateEnum;
import com.cxxy.qiu.gradudes.service.AwardService;
import com.cxxy.qiu.gradudes.util.CodeUtil;
import com.cxxy.qiu.gradudes.util.HttpServletRequestUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/shopadmin")
public class AwardManagementController {
	@Autowired
	private AwardService awardService;

	@RequestMapping(value = "/listawardsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listAwardsByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取前台的页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 获前台传过来的每页要求返回的商品总数上线
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 从当前session中获取店铺信息，主要是回去shopId
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 空值判断
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			// 从请求中获取奖品名,有则模糊查询
			String awardName = HttpServletRequestUtil.getString(request, "awardName");
			// 拼接查询条件
			Award awardCondition = compactAwardCondition4Search(currentShop.getShopId(), awardName);
			// 根据查询条件分页获取奖品列表即总数
			AwardExecution ae = awardService.getAwardList(awardCondition, pageIndex, pageSize);
			modelMap.put("awardList", ae.getAwardList());
			modelMap.put("count", ae.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}

	/*
	 * 通过商品id获取奖品信息
	 */
	@RequestMapping(value = "/getawardbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getAwardbyId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从request中获取前端传过来的awardId
		long awardId = HttpServletRequestUtil.getLong(request, "awardId");
		// 空值判断
		if (awardId > -1) {
			Award award = awardService.getAwardById(awardId);
			modelMap.put("award", award);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty awardId");
		}
		return modelMap;
	}

	/*
	 * 添加奖品
	 * 
	 * */
	@RequestMapping(value = "/addaward", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addAward(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//验证码校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		//接受前台参数的变量的初始化，包括奖品，缩略图
		ObjectMapper mapper = new ObjectMapper();
		Award award = null;
		String awardStr = HttpServletRequestUtil.getString(request, "awardStr");
		ImageHolder thumbnail = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 请求都是带multi字样，因此没法过滤，只能用拦截器外部非图片流的处理
		// 里边有缩略图的空值判断
		try {
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			// 尝试获取前端传过来的表单string流并将其转换成Award实体类
			award = mapper.readValue(awardStr, Award.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (award != null && thumbnail != null) {
			
			try {
				// 从session中获取当前店铺的Id并赋值给product，减少对前端数据 的依赖
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				award.setShopId(currentShop.getShopId());
				//添加award
				AwardExecution ae = awardService.addAward(award, thumbnail);
				if (ae.getState() == AwardStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入奖品信息");
		}
		return modelMap;
	}

	/*
	 * 修改奖品信息
	 */
	@RequestMapping(value = "/modifyaward", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyAward(HttpServletRequest request) {
		boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 根据传入的状态值决定是否跳过验证码校验
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 接收前台参数的变量的初始化，包括商品，缩略图
		ObjectMapper mapper = new ObjectMapper();
		Award award = null;
		ImageHolder thumbnail = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 请求都是带multi字样，因此没法过滤，只能用拦截器外部非图片流的处理
		// 里边有缩略图的空值判断
		try {
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			String awardStr = HttpServletRequestUtil.getString(request, "awardStr");
			// 尝试获取前端传过来的表单string流并将其转换成Award实体类
			award = mapper.readValue(awardStr, Award.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (award != null) {
			try {
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				award.setShopId(currentShop.getShopId());
				AwardExecution pe = awardService.modifyAward(award, thumbnail);
				if (pe.getState() == AwardStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入奖品信息");
		}
		return modelMap;
	}

	private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail) throws IOException {
		MultipartHttpServletRequest multipartRequest;

		multipartRequest = (MultipartHttpServletRequest) request;
		// 取出缩略图并构建ImageHolder对象
		CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");

		thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());

		return thumbnail;

	}

	private Award compactAwardCondition4Search(Long shopId, String awardName) {
		Award awardCondition = new Award();
		awardCondition.setShopId(shopId);
		if (awardName != null) {
			awardCondition.setAwardName(awardName);
		}
		return awardCondition;
	}
	@RequestMapping(value = "/getawardbyawardid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getAwardByAwardid(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从前台传递过来的userAwardid
		long awardId = HttpServletRequestUtil.getLong(request, "awardId");
		// 空值判断
		if (awardId > -1) {
			// 根据奖品Id获取奖品信息
			Award award = awardService.getAwardById(awardId);
			// 将奖品信息和领取状态返回给前台
			modelMap.put("award", award);
			
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty awardId");
		}
		return modelMap;
	}
}
