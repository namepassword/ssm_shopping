package com.cxxy.qiu.gradudes.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cxxy.qiu.gradudes.dao.HeadLineDao;
import com.cxxy.qiu.gradudes.dto.HeadLineExecution;
import com.cxxy.qiu.gradudes.dto.ImageHolder;
import com.cxxy.qiu.gradudes.entity.HeadLine;
import com.cxxy.qiu.gradudes.enums.HeadLineStateEnum;
import com.cxxy.qiu.gradudes.exceptions.HeadLineOperationException;
import com.cxxy.qiu.gradudes.service.HeadLineService;
import com.cxxy.qiu.gradudes.util.ImageUtil;
import com.cxxy.qiu.gradudes.util.PathUtil;

@Service
public class HeadLineServiceImpl implements HeadLineService {

	@Autowired
	private HeadLineDao headLineDao;

	@Override
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {

		return headLineDao.queryHeadLine(headLineCondition);
	}

	@Override
	@Transactional
	public HeadLineExecution addHeadLine(HeadLine headLine, ImageHolder thumbnail) {
		// 空值判断
		if (headLine == null) {
			return new HeadLineExecution(HeadLineStateEnum.EMPTY);
		}
		try {
			// 赋予初始值
			headLine.setCreateTime(new Date());
			headLine.setLastEditTime(new Date());
			headLine.setEnableStatus(1);
			// 添加头条信息
			if (thumbnail.getImage() != null) {
				try {
					// 存储图片
					addHeadLineImg(headLine, thumbnail);
					int effectedNum = headLineDao.insertHeadLine(headLine);
					if (effectedNum <= 0) {
						throw new HeadLineOperationException("头条添加失败");
					}
				} catch (Exception e) {
					throw new HeadLineOperationException("addHeadLineImg error" + e.getMessage());
				}
			}

		} catch (Exception e) {
			throw new HeadLineOperationException("addHeadLine error" + e.getMessage());

		}
		return new HeadLineExecution(HeadLineStateEnum.SUCCESS, headLine);
	}

	private void addHeadLineImg(HeadLine headLine, ImageHolder thumbnail) {
		// 获取shop图片目录的相对值路径
		String dest = PathUtil.getHeadLineImagePath();

		String lineImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		headLine.setLineImg(lineImgAddr);
	}

	@Override
	public HeadLine getHeadLineById(long lineId) {
		return headLineDao.queryHeadLineById(lineId);
	}

	/*
	 * 修改头条信息
	 */
	@Override
	@Transactional
	public HeadLineExecution modifyHeadLine(HeadLine headLine, ImageHolder thumbnail)
			throws HeadLineOperationException {
		if (headLine == null && headLine.getLineId() ==null) {
			return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
		} 
		else if(thumbnail==null) {
			try {
				
				// 更新头条信息
				headLine.setLastEditTime(new Date());
				int effectedNum = headLineDao.updateHeadLine(headLine);
				if (effectedNum <= 0) {
					return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
				} else {
					headLine = headLineDao.queryHeadLineById(headLine.getLineId());
					return new HeadLineExecution(HeadLineStateEnum.SUCCESS, headLine);
				}
			} catch (Exception e) {
				throw new HeadLineOperationException("modifyHeadLine error:" + e.getMessage());
			}
		}
		else {
			// 1.判断是否需要处理图片
			try {
				if (thumbnail.getImage() != null && thumbnail.getImageName() != null
						&& !"".equals(thumbnail.getImageName())) {
					HeadLine tempHeadLine = headLineDao.queryHeadLineById(headLine.getLineId());
					if (tempHeadLine.getLineImg() != null) {
						ImageUtil.deleteFileOrPath(tempHeadLine.getLineImg());
					}
					addHeadLineImg(headLine, thumbnail);
				}
				// 更新头条信息
				headLine.setLastEditTime(new Date());
				int effectedNum = headLineDao.updateHeadLine(headLine);
				if (effectedNum <= 0) {
					return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
				} else {
					headLine = headLineDao.queryHeadLineById(headLine.getLineId());
					return new HeadLineExecution(HeadLineStateEnum.SUCCESS, headLine);
				}
			} catch (Exception e) {
				throw new HeadLineOperationException("modifyHeadLine error:" + e.getMessage());
			}
		}

	}

	@Override
	@Transactional
	public HeadLineExecution deleteHeadLine(long lineId) {
		if (lineId > -1) {
			try {
				HeadLine headLine = headLineDao.queryHeadLineById(lineId);
				if (headLine.getLineImg() != null) {
					ImageUtil.deleteFileOrPath(headLine.getLineImg());
				}
				int effectedNum = headLineDao.deleteHeadLine(lineId);
				if (effectedNum <= 0) {
					throw new HeadLineOperationException("头条添加失败");
				} else {
					return new HeadLineExecution(HeadLineStateEnum.SUCCESS);
				}
			} catch (Exception e) {
				throw new RuntimeException("删除头条信息失败:" + e.toString());
			}
		} else {
			return new HeadLineExecution(HeadLineStateEnum.EMPTY);
		}
	}

}
