package com.cxxy.qiu.gradudes.web.superadmin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxxy.qiu.gradudes.entity.PersonInfo;

@Controller
@RequestMapping(value = "/superadmin", method = { RequestMethod.GET })
public class SuperAdminManagement {

	@RequestMapping(value = "/getsuperadminname", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getSuperAdminname(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = new PersonInfo();
		try {
			user = (PersonInfo) request.getSession().getAttribute("user");
			modelMap.put("user", user);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}
}
