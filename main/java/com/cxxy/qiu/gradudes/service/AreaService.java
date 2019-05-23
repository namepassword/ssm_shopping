package com.cxxy.qiu.gradudes.service;

import java.util.List;

import com.cxxy.qiu.gradudes.dto.AreaExecution;
import com.cxxy.qiu.gradudes.entity.Area;

public interface AreaService {

	List<Area> getAreaList();
	AreaExecution addArea(Area area);
	AreaExecution modifyArea(Area area);
	AreaExecution removeArea(long areaId);
	AreaExecution removeAreaList(List<Long> areaIdList);
}
