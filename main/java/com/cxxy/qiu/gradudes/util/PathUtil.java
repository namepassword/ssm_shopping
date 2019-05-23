package com.cxxy.qiu.gradudes.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtil {

	private static String seperator = System.getProperty("file.separator");

	private static String winPath;

	private static String linuxPath;

	private static String shopPath;

	private static String headLinePath;

	private static String shopCategoryPath;

	@Value("${shopcategory.relevant.path}")
	public void setShopCategoryPath(String shopCategoryPath) {
		PathUtil.shopCategoryPath = shopCategoryPath;
	}

	@Value("${headline.relevant.path}")
	public void setHeadLinePath(String headLinePath) {
		PathUtil.headLinePath = headLinePath;
	}

	@Value("${win.base.path}")
	public void setWinPath(String winPath) {
		PathUtil.winPath = winPath;
	}

	@Value("${linux.base.path}")
	public void setLinuxPath(String linuxPath) {
		PathUtil.linuxPath = linuxPath;
	}

	@Value("${shop.relevant.path}")
	public void setShopPath(String shopPath) {
		PathUtil.shopPath = shopPath;
	}

	// 返回项目图片的根路径
	public static String getImgBasePath() {
		String os = System.getProperty("os.name");
		String basePath ="";
		if (os.toLowerCase().startsWith("win")) {
			basePath = winPath;
		} else {
			basePath = linuxPath;
		}

		basePath = basePath.replace("/", seperator);
		return basePath;
	}

	// 依据业务需求的不同返回商店项目的子路径
	public static String getShopImagePath(long shopId) {
		String imagePath = shopPath + shopId + seperator;
		return imagePath.replace("/", seperator);
		// return imagePath;
	}

	// 依据业务需求的返回头条项目的子路径
	public static String getHeadLineImagePath() {
		String imagePath = headLinePath + seperator;
		return imagePath.replace("/", seperator);
		// return imagePath;
	}

	// 依据业务需求的返回商店类别项目的子路径
	public static String getShopCategoryImagePath() {
		String imagePath = shopCategoryPath + seperator;
		return imagePath.replace("/", seperator);
		// return imagePath;
	}
}
