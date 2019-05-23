package com.cxxy.qiu.gradudes.web.superadmin;

import java.io.IOException;
import java.net.URLDecoder;
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

import com.cxxy.qiu.gradudes.dto.HeadLineExecution;
import com.cxxy.qiu.gradudes.dto.ImageHolder;
import com.cxxy.qiu.gradudes.entity.ConstantForSuperAdmin;
import com.cxxy.qiu.gradudes.entity.HeadLine;
import com.cxxy.qiu.gradudes.enums.HeadLineStateEnum;
import com.cxxy.qiu.gradudes.service.HeadLineService;
import com.cxxy.qiu.gradudes.util.CodeUtil;
import com.cxxy.qiu.gradudes.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/superadmin")
public class HeadLineController {
	@Autowired
	private HeadLineService headLineService;

	@RequestMapping(value = "/listheadlines", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	private Map<String, Object> listHeadLines(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<HeadLine> list = new ArrayList<HeadLine>();
		try {
			Integer enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
			HeadLine headLine = new HeadLine();
			if (enableStatus > -1) {
				headLine.setEnableStatus(enableStatus);
			}
			list = headLineService.getHeadLineList(headLine);
			modelMap.put(ConstantForSuperAdmin.PAGE_SIZE, list);

			modelMap.put(ConstantForSuperAdmin.TOTAL, list.size());
			modelMap.put("headLineList", list);
			modelMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		return modelMap;
	}

	@RequestMapping(value = "/addheadline", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addHeadLine(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 1.接受并转化相应的参数，包括头条信息以及图片信息
		String headLineStr = HttpServletRequestUtil.getString(request, "headLineStr");

		ObjectMapper mapper = new ObjectMapper();
		HeadLine headLine = null;
		try {

			headLine = mapper.readValue(headLineStr, HeadLine.class);

		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile headLineImg = null;
		// 文件解析器
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			headLineImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("headLineImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}

		// 添加头条
		if (headLine != null && headLineImg != null) {
			HeadLineExecution he;
			try {
				// decode可能有中文的地方
				headLine.setLineName(
						(headLine.getLineName() == null) ? null : URLDecoder.decode(headLine.getLineName(), "UTF-8"));
				ImageHolder imageHolder = new ImageHolder(headLineImg.getOriginalFilename(),
						headLineImg.getInputStream());
				he = headLineService.addHeadLine(headLine, imageHolder);
				if (he.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", he.getStateInfo());
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

	@RequestMapping(value = "/modifyheadline", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyHeadLine(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 1.接受并转化相应的参数，包括头条信息以及图片信息
		String headLineStr = HttpServletRequestUtil.getString(request, "headLineStr");

		ObjectMapper mapper = new ObjectMapper();
		HeadLine headLine = null;
		try {

			headLine = mapper.readValue(headLineStr, HeadLine.class);

		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile headLineImg = null;
		// 文件解析器
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			headLineImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("headLineImg");
		}
		// 修改头条
		if (headLine != null && headLine.getLineId() != null) {
			HeadLineExecution he;
			try {
				if (headLineImg == null) {
					he = headLineService.modifyHeadLine(headLine, null);
				} else {
					ImageHolder imageHolder = new ImageHolder(headLineImg.getOriginalFilename(),
							headLineImg.getInputStream());
					he = headLineService.modifyHeadLine(headLine, imageHolder);
				}
				if (he.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {

					modelMap.put("success", false);
					modelMap.put("errMsg", he.getStateInfo());
				}
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
			// 3.返回结果
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入头条id");
			return modelMap;
		}
	}
	@RequestMapping(value = "/getheadlinebyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getHeadLineById(@RequestParam Long lineId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//非空判断
				if (lineId > -1) {
					//获取头条信息
					HeadLine headLine = headLineService.getHeadLineById(lineId);
					
					modelMap.put("headLine", headLine);
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", "empty lineId");
				}
				return modelMap;
	}
	
	@RequestMapping(value = "/deleteheadlinebyid", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> deleteHeadLine(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 1.接受并转化相应的参数，包括头条信息以及图片信息
				String headLineStr = HttpServletRequestUtil.getString(request, "headLineStr");

				ObjectMapper mapper = new ObjectMapper();
				HeadLine headLine = null;
				try {

					headLine = mapper.readValue(headLineStr, HeadLine.class);

				} catch (Exception e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.getMessage());
					return modelMap;
				}
				HeadLineExecution he=headLineService.deleteHeadLine(headLine.getLineId());
				if (he.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", he.getStateInfo());
				}
				return modelMap;
	}
}
