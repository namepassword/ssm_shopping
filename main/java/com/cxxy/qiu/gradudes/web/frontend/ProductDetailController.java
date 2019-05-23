package com.cxxy.qiu.gradudes.web.frontend;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxxy.qiu.gradudes.entity.PersonInfo;
import com.cxxy.qiu.gradudes.entity.Product;
import com.cxxy.qiu.gradudes.service.ProductService;
import com.cxxy.qiu.gradudes.util.CodeUtil;
import com.cxxy.qiu.gradudes.util.HttpServletRequestUtil;
import com.cxxy.qiu.gradudes.util.ShortNetAddressUtil;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Controller
@RequestMapping("/frontend")
public class ProductDetailController {
	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/listproductdetailpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductDetailPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 前台传过来的productId
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		Product product = null;
		// 空值判断
		if (productId != -1) {
			product = productService.getProductById(productId);
			PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
			if (user == null) {
				modelMap.put("needQRCode", false);
			} else {
				modelMap.put("needQRCode", true);
			}
			modelMap.put("product", product);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId");
		}
		return modelMap;
	}

	// 微信获取用户信息的api前缀
	private static String urlPrefix;
	// 微信获取用户信息的api中间部分
	private static String urlMiddle;
	// 微信获取用户信息的api后缀
	private static String urlSuffix;
	// 微信回传给响应添加授权信息的url
	private static String productmapUrl;

	@Value("${wechat.prefix}")
	public void setUrlPrefix(String urlPrefix) {
		ProductDetailController.urlPrefix = urlPrefix;
	}

	@Value("${wechat.middle}")
	public void setUrlMiddle(String urlMiddle) {
		ProductDetailController.urlMiddle = urlMiddle;
	}

	@Value("${wechat.suffix}")
	public void setUrlSuffix(String urlSuffix) {
		ProductDetailController.urlSuffix = urlSuffix;
	}

	@Value("${wechat.productmap.url}")
	public void setProductmapUrl(String productmapUrl) {
		ProductDetailController.productmapUrl = productmapUrl;
	}

	/*
	 * 生成商品的消费凭证二维码，供操作员扫描，证明已消费，微信扫一扫就能实现
	 * 
	 */

	@RequestMapping(value = "/generateqrcode4product", method = RequestMethod.GET)
	@ResponseBody
	private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {
		// 获取前台传递过来的商品Id
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		// 从session中获取当前顾客信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 空值判断
		if (productId != -1 && user != null && user.getUserId() != null) {
			// 获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
			long timpStamp = System.currentTimeMillis();
			// 将商品Id，顾客id和timestamp传入content赋值到state中
			// 加上aaa是为了一会儿在添加信息的方法里替换这些信息
			String content = "{aaaproductIdaaa:" + productId + ",aaacustomerIdaaa:" + user.getUserId()
					+ ",aaacreateTimeaaa:" + timpStamp + "}";
			try {
				// 将content的信息先进行base64编码以避免特殊字符串造成干扰，之后拼接目标URL
				String longUrl = urlPrefix + productmapUrl + urlMiddle + URLEncoder.encode(content, "UTF-8")
						+ urlSuffix;
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
}
