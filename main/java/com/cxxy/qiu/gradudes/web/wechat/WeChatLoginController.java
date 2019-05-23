package com.cxxy.qiu.gradudes.web.wechat;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cxxy.qiu.gradudes.dto.UserAccessToken;
import com.cxxy.qiu.gradudes.dto.WechatAuthExecution;
import com.cxxy.qiu.gradudes.dto.WechatUser;
import com.cxxy.qiu.gradudes.entity.PersonInfo;
import com.cxxy.qiu.gradudes.entity.WechatAuth;
import com.cxxy.qiu.gradudes.enums.WechatAuthStateEnum;
import com.cxxy.qiu.gradudes.exceptions.WechatOperationExecution;
import com.cxxy.qiu.gradudes.service.PersonInfoService;
import com.cxxy.qiu.gradudes.service.WechatAuthService;
import com.cxxy.qiu.gradudes.util.wechat.WechatUtil;

/*
 * 获取关注公众号之后的微信用户信息的接口
 * 
 * */
@Controller
@RequestMapping("/wechatlogin")
public class WeChatLoginController {
	private static Logger log = LoggerFactory
			.getLogger(WeChatLoginController.class);
	private static final String FEONTEND = "1";
	private static final String SHOPEND = "2";
	private static final String SUPEREND = "3";
	
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private WechatAuthService wechatAuthService;

	@RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("weixin login get...");
		// 获取微信公众号传输过来的code,通过code可获取哦access_token进而获取用户信息
		String code = request.getParameter("code");
		// 这个state可以用来传我们自定义的信息，方便程序调用，这里也可以不用
		String roleType = request.getParameter("state");
		log.debug("weixin login code:" + code);
		WechatUser user = null;
		String openId = null;
		WechatAuth auth = null;
		String userType=null;
		if (null != code) {
			UserAccessToken token;
			try {
				// 通过token获取accesstoken
				token = WechatUtil.getUserAccessToken(code);
				log.debug("weixin login token:" + token.toString());
				// 通过token获取openId
				String accessToken = token.getAccessToken();
				openId = token.getOpenId();
				// 通过access_token和openId获取用户昵称等信息
				user = WechatUtil.getUserInfo(accessToken, openId);
				log.debug("weixin login user:" + user.toString());
				request.getSession().setAttribute("openId", openId);
				auth = wechatAuthService.getWechatAuthByOpenId(openId);

			} catch (Exception e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		// 获取到openId后，可以通过它去数据库判断该微信账号是否在我们网站里有对应的账号
		// 没有的话这里可以自动创建，直接实现微信与网站的无缝对接
		if (auth == null) {
			PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
			auth = new WechatAuth();
			auth.setOpenId(openId);
			if (FEONTEND.equals(roleType)) {
				personInfo.setUserType(1);
			}else if(SHOPEND.equals(roleType)) {
				personInfo.setUserType(2);
			} else {
				personInfo.setUserType(3);
			}

			auth.setPersonInfo(personInfo);
			WechatAuthExecution we;
			try {
				we = wechatAuthService.register(auth);
				if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
					return null;
				} else {
					personInfo = personInfoService.getPersonInfoById(auth
							.getPersonInfo().getUserId());
					request.getSession().setAttribute("user", personInfo);
				}
			} catch (WechatOperationExecution e) {
				e.printStackTrace();
			}

		}
		//auth不为空判断显示的是哪一个功能某块
		if(auth!=null) {			
			PersonInfo personInfo = personInfoService.getPersonInfoById(auth
					.getPersonInfo().getUserId());
			request.getSession().setAttribute("user", personInfo);
			// 若用户点击的是前端展示系统页则进入前端展示系统
			//若是店家进入店家界面
			//若是超级管理员进入超级管理员页面
			 userType= personInfo.getUserType().toString();
		}
		if (userType.equals(roleType)&&SHOPEND.equals(userType)) {
			return "shop/shoplist";

		} else if(userType.equals(roleType)&&SUPEREND.equals(userType)) {
			return "superadmin/superadminmanagement";

		} else {
			return "frontend/index";
		}

	}
}
