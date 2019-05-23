package com.cxxy.qiu.gradudes.web.superadmin;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxxy.qiu.gradudes.dto.AreaExecution;
import com.cxxy.qiu.gradudes.entity.Area;
import com.cxxy.qiu.gradudes.enums.AreaStateEnum;
import com.cxxy.qiu.gradudes.service.AreaService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/superadmin")
public class AreaController {

Logger logger= LoggerFactory.getLogger(AreaController.class);	
	@Autowired
	private AreaService areaService;
	
	@RequestMapping(value="/listarea",method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> listArea(){
		
		logger.info("===start===");
		long startTime=System.currentTimeMillis();
	    //存放返回的类型
		Map<String,Object> modelMap=new HashMap<String,Object>();
		List<Area> list = new ArrayList<Area>();
		try{
			list=areaService.getAreaList();
			modelMap.put("rows",list);
			modelMap.put("total", list.size());
		}
		catch(Exception e){
			e.printStackTrace();
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		logger.error("test error!");
		long endTime=System.currentTimeMillis();
		logger.debug("costTime[{}ms]",endTime-startTime);
		logger.info("===end===");
		return modelMap;
	}
	
	@RequestMapping(value = "/addarea", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addArea(String areaStr,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		Area area = null;
		try {
			area = mapper.readValue(areaStr, Area.class);
			// decode可能有中文的地方
			area.setAreaName((area.getAreaName() == null) ? null : URLDecoder
					.decode(area.getAreaName(), "UTF-8"));
	
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (area != null && area.getAreaName() != null) {
			try {
				AreaExecution ae = areaService.addArea(area);
				if (ae.getState() == AreaStateEnum.SUCCESS.getState()) {
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
			modelMap.put("errMsg", "请输入区域信息");
		}
		return modelMap;
	}

	@RequestMapping(value = "/modifyarea", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyArea(String areaStr,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		Area area = null;
		try {
			area = mapper.readValue(areaStr, Area.class);
			area.setAreaName((area.getAreaName() == null) ? null : URLDecoder
					.decode(area.getAreaName(), "UTF-8"));
			
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (area != null && area.getAreaId() != null) {
			try {
				AreaExecution ae = areaService.modifyArea(area);
				if (ae.getState() == AreaStateEnum.SUCCESS.getState()) {
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
			modelMap.put("errMsg", "请输入区域信息");
		}
		return modelMap;
	}

	@RequestMapping(value = "/removearea", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeArea(Long areaId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (areaId != null && areaId > 0) {
			try {
				AreaExecution ae = areaService.removeArea(areaId);
				if (ae.getState() == AreaStateEnum.SUCCESS.getState()) {
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
			modelMap.put("errMsg", "请输入区域信息");
		}
		return modelMap;
	}

	@RequestMapping(value = "/removeareas", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeAreas(String areaIdListStr) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(
				ArrayList.class, Long.class);
		List<Long> areaIdList = null;
		try {
			areaIdList = mapper.readValue(areaIdListStr, javaType);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		if (areaIdList != null && areaIdList.size() > 0) {
			try {
				AreaExecution ae = areaService.removeAreaList(areaIdList);
				if (ae.getState() == AreaStateEnum.SUCCESS.getState()) {
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
			modelMap.put("errMsg", "请输入区域信息");
		}
		return modelMap;
	}
}
