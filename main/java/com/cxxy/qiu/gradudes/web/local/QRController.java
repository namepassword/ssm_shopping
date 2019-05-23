package com.cxxy.qiu.gradudes.web.local;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxxy.qiu.gradudes.util.CodeUtil;
import com.cxxy.qiu.gradudes.util.ShortNetAddressUtil;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Controller
@RequestMapping(value = "/local", method = { RequestMethod.GET, RequestMethod.POST })
public class QRController {

	// 微信获取用户信息的api前缀
	private static String urlPrefix;

	// 微信获取用户信息的api中间部分之店铺网站
	private static String urlMiddleLogin;
	// 微信获取用户信息的api中间部分之店家
	private static String urlMiddleShop;
	// 微信获取用户信息的api中间部分之超级管理员
	private static String urlMiddleSuper;

	// 微信获取用户信息的api后缀
	private static String urlSuffix;
	// 微信首页登陆显示页面
	private static String urlLoging;

	@Value("${wechat.middleLogin}")
	public void setUrlMiddleLogin(String urlMiddleLogin) {
		QRController.urlMiddleLogin = urlMiddleLogin;
	}

	@Value("${wechat.middleshopadmin}")
	public void setUrlMiddleShop(String urlMiddleShop) {
		QRController.urlMiddleShop = urlMiddleShop;
	}

	@Value("${wechat.middlesuperadmin}")
	public void setUrlMiddleSuper(String urlMiddleSuper) {
		QRController.urlMiddleSuper = urlMiddleSuper;
	}

	@Value("${wechat.prefix}")
	public void setUrlPrefix(String urlPrefix) {
		QRController.urlPrefix = urlPrefix;
	}

	@Value("${wechat.suffix}")
	public void setUrlSuffix(String urlSuffix) {
		QRController.urlSuffix = urlSuffix;
	}

	@Value("${wechat.login.url}")
	public void setUrlLoging(String urlLoging) {
		QRController.urlLoging = urlLoging;
	}

	@RequestMapping(value = "/generateqrcodelogin", method = RequestMethod.GET)
	@ResponseBody
	private void generateQRCodelogin(HttpServletRequest request, HttpServletResponse response) {
		// 获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
		long timpStamp = System.currentTimeMillis();

		try {
			// 将content的信息先进行base64编码以避免特殊字符串造成干扰，之后拼接目标URL
			String longUrl = urlPrefix + urlLoging + urlMiddleLogin + urlSuffix;
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

	@RequestMapping(value = "/generateqrcodeshopadmin", method = RequestMethod.GET)
	@ResponseBody
	private void generateQRCodeShopadmin(HttpServletRequest request, HttpServletResponse response) {
		// 获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
		long timpStamp = System.currentTimeMillis();

		try {
			// 将content的信息先进行base64编码以避免特殊字符串造成干扰，之后拼接目标URL
			String longUrl = urlPrefix + urlLoging + urlMiddleShop + urlSuffix;
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

	@RequestMapping(value = "/generateqrcodesuperadmin", method = RequestMethod.GET)
	@ResponseBody
	private void generateQRCodeSuperadmin(HttpServletRequest request, HttpServletResponse response) {
		// 获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
		long timpStamp = System.currentTimeMillis();

		try {
			// 将content的信息先进行base64编码以避免特殊字符串造成干扰，之后拼接目标URL
			String longUrl = urlPrefix + urlLoging + urlMiddleSuper + urlSuffix;
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
