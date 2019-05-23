package com.cxxy.qiu.gradudes.service;

import java.io.IOException;
import java.util.List;


import com.cxxy.qiu.gradudes.dto.HeadLineExecution;
import com.cxxy.qiu.gradudes.dto.ImageHolder;
import com.cxxy.qiu.gradudes.entity.HeadLine;

public interface HeadLineService {

	/**
	 * 根据传入的条件返回指定的头条列表
	 * 
	 */
	List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException;

	/**
	 * 
	 * @param headLine
	 * @param thumbnail
	 * @return
	 */
	HeadLineExecution addHeadLine(HeadLine headLine, ImageHolder thumbnail);

	HeadLine getHeadLineById(long lineId);

	HeadLineExecution modifyHeadLine(HeadLine headLine, ImageHolder thumbnail);
	
	HeadLineExecution deleteHeadLine(long lineId);
}
