package com.cxxy.qiu.gradudes.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cxxy.qiu.gradudes.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {

	private static Logger log = LoggerFactory.getLogger(ImageUtil.class);
	//private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static String basePath = PathUtil.getImgBasePath();
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r = new Random();

	public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
		log.info("Java_jar:" + basePath);
		String realFileName = getRandomFileName();
		String extension = getFileExtension(thumbnail.getImageName());
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);

		try {

			Thumbnails.of(thumbnail.getImage()).size(200, 200) // "D:/project/gradudesImg/watermark.jpg"
																// basePath+"/watermark.jpg"
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
					.outputQuality(0.8f).toFile(dest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return relativeAddr;
	}

	/**
	 * 创建目标路径所涉及到的目录，即/home/work/qiu/xx.jpg 那么home work qiu 这三个文件夹都得自动创建
	 */
	private static void makeDirPath(String targetAddr) {

		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	/**
	 * 获取输入文件流的扩展名
	 */

	private static String getFileExtension(String fileName) {

		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 
	 * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
	 */
	public static String getRandomFileName() {
		// 获取随机的五位数
		int rannum = r.nextInt(89999) + 10000;
		String nowTimeStr = sDateFormat.format(new Date());
		return nowTimeStr + rannum;
	}

	/**
	 * storePath是文件的路径还是目录的路径 如果storePath是文件路径则删除该文件 如果storePath是目录路径则是删除该目录下的所有文件
	 */
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
		if (fileOrPath.exists()) {
			if (fileOrPath.isDirectory()) {
				File files[] = fileOrPath.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}

	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		// 获取不重复的随机名
		String realFileName = getRandomFileName();
		// 获取文件的扩展名如.jpg .png
		String extension = getFileExtension(thumbnail.getImageName());
		// 如果目标路径不存在，则自动创建
		makeDirPath(targetAddr);
		// 获取文件存储的相对路径（带文件名）
		String relativeAddr = targetAddr + realFileName + extension;
		// 获取文件要保存到的目标路径
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		// 调用Thumbnails生成有水印的图片
		try {

			Thumbnails.of(thumbnail.getImage()).size(337, 640)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
					.outputQuality(0.8f).toFile(dest);

		} catch (Exception e) {

			throw new RuntimeException("创建缩略图片失败：" + e.getMessage());
		}
		// 返回图片相对路径地址
		return relativeAddr;
	}
}
