package com.cxxy.qiu.gradudes.util.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cxxy.qiu.gradudes.dto.UserAccessToken;
import com.cxxy.qiu.gradudes.dto.WechatUser;
import com.cxxy.qiu.gradudes.entity.PersonInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//主要用来提交http 请求给微信获取用户信息
public class WechatUtil {
	
	/*
	 * 将WechatUser里的信息转换成PersonInfo的信息并返回PersonInfo
	 * 
	 * */
	public static PersonInfo getPersonInfoFromRequest(WechatUser user){
		
		PersonInfo personInfo= new PersonInfo();
		personInfo.setName(user.getNickName());
		personInfo.setGender(user.getSex()+" ");
		personInfo.setProfileImg(user.getHeadimgurl());
		personInfo.setEnableStatus(1);
		
		return personInfo;
	}
	
	private static Logger log = LoggerFactory.getLogger(WechatUtil.class);

	public static UserAccessToken getUserAccessToken(String code) {
		// 测试号信息里的appid
		String appId = "wxa7c20139d88e4abc";
		log.debug("appId:" + appId);
		// 测试号信息里的appsecret
		String appsecret = "64f2656eb5093e8975e2b87c96191bd2";
		log.debug("secret:" + appsecret);
		// 根据传入的code，拼接出访问微信定义好的接口的URL
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appsecret
				+ "&code=" + code + "&grant_type=authorization_code";
		// 向相应URL发送请求获取token json 字符串
		String tokenStr = httpsRequest(url, "GET", null);
		log.debug("userAccessToken:" + tokenStr);
		UserAccessToken token = new UserAccessToken();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// 将json字符串转换成相应对象
			token = objectMapper.readValue(tokenStr, UserAccessToken.class);
		} catch (JsonParseException e) {
			log.error("获取用户accessToken失败:" + e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("获取用户accessToken失败:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("获取用户accessToken失败:" + e.getMessage());
			e.printStackTrace();
		}
		if (token == null) {
			log.error("获取用户accessToken失败。");
			return null;
		}
		return token;
	}


	
	/*
	 * 发送https请求并获取结果
	 */
	private static String httpsRequest(String requestUrl, String requestMethod,
			String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			log.debug("https buffer:" + buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return buffer.toString();
	}



	public static WechatUser getUserInfo(String accessToken, String openId) {
		// 根据传入的accessToken以及openId拼接出访问微信定义的端口并获取用户信息的url
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId
                + "&lang=zh_CN";
        // 访问该url获取用户信息json字符串
        String userStr = httpsRequest(url, "GET", null);
        log.debug("userInfo:" + userStr);
        WechatUser user = new WechatUser();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将json字符串转换成对象
            user = objectMapper.readValue(userStr, WechatUser.class);
        } catch (JsonParseException e) {
            log.error("获取用户信息失败：" + e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            log.error("获取用户信息失败：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("获取用户信息失败：" + e.getMessage());
            e.printStackTrace();
        }
        if (user == null) {
            log.error("获取用户信息失败。");
            return null;
        }
        return user;

	}

}
