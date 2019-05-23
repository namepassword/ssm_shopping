package com.cxxy.qiu.gradudes.web.frontend;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxxy.qiu.gradudes.dao.ShopDao;
import com.cxxy.qiu.gradudes.dto.UserAwardMapExecution;
import com.cxxy.qiu.gradudes.dto.UserProductMapExecution;
import com.cxxy.qiu.gradudes.entity.Award;
import com.cxxy.qiu.gradudes.entity.PersonInfo;
import com.cxxy.qiu.gradudes.entity.Product;
import com.cxxy.qiu.gradudes.entity.Shop;
import com.cxxy.qiu.gradudes.entity.UserAwardMap;
import com.cxxy.qiu.gradudes.entity.UserProductMap;
import com.cxxy.qiu.gradudes.enums.UserAwardMapStateEnum;
import com.cxxy.qiu.gradudes.service.AwardService;
import com.cxxy.qiu.gradudes.service.PersonInfoService;
import com.cxxy.qiu.gradudes.service.UserAwardMapService;
import com.cxxy.qiu.gradudes.service.UserProductMapService;
import com.cxxy.qiu.gradudes.util.CodeUtil;
import com.cxxy.qiu.gradudes.util.HttpServletRequestUtil;
import com.cxxy.qiu.gradudes.util.ShortNetAddressUtil;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Controller
@RequestMapping("/frontend")
public class MyAwardController {

	@Autowired
	private AwardService awardService;
	@Autowired
	private UserAwardMapService userAwardMapService;
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private ShopDao shopDao;
	@Autowired
	private UserProductMapService userProductMapService;

	/*
	 * 列出某个顾客的商品消费记录
	 * 
	 */
	@RequestMapping(value = "/listuserproductmapsbycustomer", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listuserProductMapsByCustomer(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 取出分页
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		//从session中获取顾客信息
		PersonInfo user=(PersonInfo) request.getSession().getAttribute("user");
		//空值判断
		if((pageIndex > -1) && (pageSize > -1)&&(user!=null)&&(user.getUserId()!=-1)) {
			UserProductMap userProductMapCondition=new UserProductMap();
			userProductMapCondition.setUser(user);
			long shopId = HttpServletRequestUtil.getLong(request, "shopId");
			if(shopId>-1) {
				Shop shop =new Shop();
				shop.setShopId(shopId);
				userProductMapCondition.setShop(shop);
			}
			String productName=HttpServletRequestUtil.getString(request, "productName");
			if(productName!=null) {
				Product product=new Product();
				product.setProductName(productName);
				userProductMapCondition.setProduct(product);
			}
			UserProductMapExecution ue=userProductMapService.listUserProductMap(userProductMapCondition,pageIndex,pageSize);
			modelMap.put("userProductMapList", ue.getUserProductMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/getawardbyuserawardid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getAwardByuserawardid(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从前台传递过来的userAwardid
		long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
		// 空值判断
		if (userAwardId > -1) {
			// 根据id获取顾客奖品映射信息，进而获取奖品Id
			UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
			// 根据奖品Id获取奖品信息
			Award award = awardService.getAwardById(userAwardMap.getAward().getAwardId());
			// 将奖品信息和领取状态返回给前台
			modelMap.put("award", award);
			modelMap.put("usedStatus", userAwardMap.getUsedStatus());
			modelMap.put("userAwardMap", userAwardMap);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty awardId");
		}
		return modelMap;
	}

	/*
	 * 
	 * 获取顾客的兑换列表
	 * 
	 */
	@RequestMapping(value = "/listuserawardmapsbycustomer", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserAwardMapsByCustomer(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 取出分页
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// 获取用户信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 空值判断
		if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != null)) {
			UserAwardMap userAwardMapCondition = new UserAwardMap();
			userAwardMapCondition.setUser(user);
			long shopId = HttpServletRequestUtil.getLong(request, "shopId");
			if (shopId > -1) {
				// 若店铺id为非空，则将其添加进查询条件，即查询该用户在某个店铺的兑换信息
				Shop shop = new Shop();
				shop.setShopId(shopId);
				userAwardMapCondition.setShop(shop);
			}
			String awardName = HttpServletRequestUtil.getString(request, "awardName");

			if (awardName != null) {
				// 若奖品名为非空，则将其添加进查询条件里面进行模糊查询
				Award award = new Award();
				award.setAwardName(awardName);

				userAwardMapCondition.setAward(award);
			}
			// 根据传入的查询条件分页获取用户奖品映射信息
			UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(userAwardMapCondition, pageIndex, pageSize);
			modelMap.put("userAwardMapList", ue.getUserAwardMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or userId");
		}

		return modelMap;
	}

	@RequestMapping(value = "/adduserawardmap", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addUserAwardMap(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 从session中获取用户信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 从前端请求中获取奖品Id
		Long awardId = HttpServletRequestUtil.getLong(request, "awardId");
		// 封装成用户奖品映射对象
		UserAwardMap userAwardMap = compactUserAwardMap4Add(user, awardId);
		
		// 空值判断
		if (userAwardMap != null) {
			try {
				// 添加兑换信息
				UserAwardMapExecution se = userAwardMapService.addUserAwardMap(userAwardMap);
				if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}

		return modelMap;
	}

	// 微信获取用户信息的api前缀
	private static String urlPrefix;
	// 微信获取用户信息的api中间部分
	private static String urlMiddle;
	// 微信获取用户信息的api后缀
	private static String urlSuffix;
	// 微信回传给响应添加用户奖品映射信息的url
	private static String exchangeUrl;

	@Value("${wechat.prefix}")
	public void setUrlPrefix(String urlPrefix) {
		MyAwardController.urlPrefix = urlPrefix;
	}

	@Value("${wechat.middle}")
	public void setUrlMiddle(String urlMiddle) {
		MyAwardController.urlMiddle = urlMiddle;
	}

	@Value("${wechat.suffix}")
	public void setUrlSuffix(String urlSuffix) {
		MyAwardController.urlSuffix = urlSuffix;
	}

	@Value("${wechat.exchange.url}")
	public void setExchangeUrl(String exchangeUrl) {
		MyAwardController.exchangeUrl = exchangeUrl;
	}

	/*
	 * 生成奖品的领取二维码，供操作员扫描，证明已领取，微信扫一扫就能链接到对应的url中
	 * 
	 */
	@RequestMapping(value = "/generateqrcode4award", method = RequestMethod.GET)
	@ResponseBody
	private void generateQRCode4Award(HttpServletRequest request, HttpServletResponse response) {
		// 获取前台传递过来的商品Id
		long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
		// 根据I获取顾客奖品映射实体类对象
		UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
		// 从session中获取当前顾客信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 空值判断
		if (userAwardMap != null && user != null && user.getUserId() != null
				&& userAwardMap.getUser().getUserId() == user.getUserId()) {
			// 获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
			long timpStamp = System.currentTimeMillis();
			// 将顾客奖品Id，顾客id和timestamp传入content赋值到state中
			// 加上aaa是为了一会儿在添加信息的方法里替换这些信息
			String content = "{aaauserAwardIdaaa:" + userAwardId + ",aaacustomerIdaaa:" + user.getUserId()
					+ ",aaacreateTimeaaa:" + timpStamp + "}";
			try {

				// 将content的信息先进行base64编码以避免特殊字符串造成干扰，之后拼接目标URL
				String longUrl = urlPrefix + exchangeUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
				// 将目标URL转换成短Url
				String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
				// 调用二维码生成的工具类方法，传入短的URL,生成二维码
				BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(shortUrl, response);
				// 将二维码以图片流的形式输出到前端
				MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private UserAwardMap compactUserAwardMap4Add(PersonInfo user, Long awardId) {
		UserAwardMap userAwardMap = null;
		if (user != null && user.getUserId() != null && awardId != -1) {
			userAwardMap = new UserAwardMap();
			PersonInfo personInfo = personInfoService.getPersonInfoById(user.getUserId());
			Award award = awardService.getAwardById(awardId);
			Shop shop = shopDao.queryByShopId(award.getShopId());
			userAwardMap.setAward(award);
			userAwardMap.setCreateTime(new Date());
			userAwardMap.setPoint(award.getPoint());
			userAwardMap.setShop(shop);
			PersonInfo operator=personInfoService.getPersonInfoById(shop.getOwner().getUserId());
			
			userAwardMap.setUsedStatus(0);
			userAwardMap.setUser(personInfo);
			userAwardMap.setOperator(operator);
		}
		return userAwardMap;
	}
}
