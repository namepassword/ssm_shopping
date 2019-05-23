package com.cxxy.qiu.gradudes.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/local")
public class LocalController {

	/*
	 * 绑定帐号页路由
	 */

	@RequestMapping(value = "/accountbind", method = RequestMethod.GET)
	public String accountbind() {
		return "local/accountbind";
	}
	/*
	 * 修改页面路由
	 */

	@RequestMapping(value = "/changepsw", method = RequestMethod.GET)
	public String changepsw() {
		return "local/changepsw";
	}
	/*
	 * 登录页面路由
	 */

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "local/login";
	}
	/*
	 * 登录页面路由
	 */

	@RequestMapping(value = "/showqr", method = RequestMethod.GET)
	public String shopQR() {
		return "local/showqr";
	}
}
