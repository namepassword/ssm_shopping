package com.cxxy.qiu.gradudes.web.shopadmin;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxxy.qiu.gradudes.dto.ShopAuthMapExecution;
import com.cxxy.qiu.gradudes.dto.UserAccessToken;
import com.cxxy.qiu.gradudes.dto.UserAwardMapExecution;
import com.cxxy.qiu.gradudes.dto.WechatInfo;
import com.cxxy.qiu.gradudes.entity.Award;
import com.cxxy.qiu.gradudes.entity.PersonInfo;
import com.cxxy.qiu.gradudes.entity.Shop;
import com.cxxy.qiu.gradudes.entity.ShopAuthMap;
import com.cxxy.qiu.gradudes.entity.UserAwardMap;
import com.cxxy.qiu.gradudes.entity.WechatAuth;
import com.cxxy.qiu.gradudes.enums.UserAwardMapStateEnum;
import com.cxxy.qiu.gradudes.service.PersonInfoService;
import com.cxxy.qiu.gradudes.service.ShopAuthMapService;
import com.cxxy.qiu.gradudes.service.UserAwardMapService;
import com.cxxy.qiu.gradudes.service.WechatAuthService;
import com.cxxy.qiu.gradudes.util.HttpServletRequestUtil;
import com.cxxy.qiu.gradudes.util.wechat.WechatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/shopadmin")
public class UserAwardManagementController {
	@Autowired
	private UserAwardMapService userAwardMapService;
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private WechatAuthService wechatAuthService;
	@Autowired
	private ShopAuthMapService shopAuthMapService;

	@RequestMapping(value = "/listuserawardmapbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserAwardMapByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取分页信息
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 获取当前的店铺信息
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		// 空值校验，主要确保shopid不为空
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			UserAwardMap userAwardMap = new UserAwardMap();
			userAwardMap.setShop(currentShop);
			// 从请求中获取奖品名
			String awardName = HttpServletRequestUtil.getString(request, "awardName");
			if (awardName != null) {
				// 如果需要按照奖品名搜索，则添加搜索条件
				Award award = new Award();
				award.setAwardName(awardName);
				userAwardMap.setAward(award);
			}
			// 分页获取该店铺下的奖品列表
			UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(userAwardMap, pageIndex, pageSize);
			modelMap.put("userAwardMapList", ue.getUserAwardMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}

		return modelMap;
	}

	/*
	 * 操作员扫顾客的奖品二维码并发派奖品，证明顾客已领取过
	 * 
	 */
	@RequestMapping(value = "/exchangeaward", method = RequestMethod.GET)
	private String exchangeAward(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 获取负责扫描二维码的店员信息
		WechatAuth auth = getOperatorInfo(request);
		if (auth != null) {

			// 通过user Info 获取店员信息
			PersonInfo operator = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
			// 设置上用户的session
			request.getSession().setAttribute("user", operator);
			// 解析微信回传过来的自定义参数state，由于之前进行了编码，这里需要解码一下
			String qrCodeInfo = new String(
					URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
			ObjectMapper mapper = new ObjectMapper();
			WechatInfo wechatInfo = null;
			try {
				// 将解码后的内容用aaa去替换之前生成二维码的时候加入的aaa前缀，转换成WechatInfo实体类
				wechatInfo = mapper.readValue(qrCodeInfo.replace("aaa", "\""), WechatInfo.class);
			} catch (Exception e) {
				return "shop/operationfail";
			}
			// 校验二维码是否已经过期
			if (!checkQRCodeInfo(wechatInfo)) {
				return "shop/operationfail";
			}
			// 获取用户奖品映射主键
			Long userAwardId = wechatInfo.getUserAwardId();
			// 获取顾客Id
			Long customerId = wechatInfo.getCustomerId();
			// 将顾客信息，操作员信息以及奖品信息封装成userAwardMap
			UserAwardMap userAwardMap = compactUserAward4Exchange(customerId, userAwardId, operator);
			
			if (userAwardMap != null) {
				try {
					/*
					 * 检测扫码人是否有权限
					 */
					if (!checkShopAuth(operator.getUserId(), userAwardMap)) {
						return "shop/operationfail";
					}
					//修改奖品的领取状态
					UserAwardMapExecution se=userAwardMapService.modifyUserAwardMap(userAwardMap);
					if(se.getState()==UserAwardMapStateEnum.SUCCESS.getState()) {
						return "shop/operationsuccess";
					}
				} catch (Exception e) {
					return "shop/operationfail";
				}
			}
		}

		 return "shop/operationfail";
	}

	/*
	 * 检测扫码人是否有权限
	 */

	private UserAwardMap compactUserAward4Exchange(Long customerId, Long userAwardId, PersonInfo operator) {
		UserAwardMap userAwardMap=null;
		
		if (customerId != null && userAwardId != null&&operator!=null) {
			userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
			userAwardMap.setUsedStatus(1);
			userAwardMap.setOperator(operator);
			}
		return userAwardMap;
	}

	private boolean checkShopAuth(Long userId, UserAwardMap userAwardMap) {
		// 获取该店铺的所有授权信息
		ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
				.listShopAuthMapByShopId(userAwardMap.getShop().getShopId(), 1, 1000);
		for (ShopAuthMap shopAuthMap : shopAuthMapExecution.getShopAuthMapList()) {
			// 看看是否给过该人员权限
			if (shopAuthMap.getEmployee().getUserId() == userId) {
				return true;
			}
		}
		return false;
	}

	/*
	 * 根据二维码携带的createTime判断是否超过10分种，超过十分钟则认为过期
	 * 
	 */
	private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
		if (wechatInfo != null && wechatInfo.getUserAwardId() != null && wechatInfo.getCreateTime() != null) {
			long nowTime = System.currentTimeMillis();
			if (nowTime - wechatInfo.getCreateTime() <= 600000) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	/*
	 * 获取扫描二维码的店员信息
	 * 
	 */
	private WechatAuth getOperatorInfo(HttpServletRequest request) {
		String code = request.getParameter("code");
		WechatAuth auth = null;
		if (null != code) {
			UserAccessToken token;
			try {
				token = WechatUtil.getUserAccessToken(code);
				String openId = token.getOpenId();
				request.getSession().setAttribute("openId", openId);
				auth = wechatAuthService.getWechatAuthByOpenId(openId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return auth;
	}
}
