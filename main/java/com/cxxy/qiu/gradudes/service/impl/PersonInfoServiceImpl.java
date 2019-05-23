package com.cxxy.qiu.gradudes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cxxy.qiu.gradudes.dao.PersonInfoDao;
import com.cxxy.qiu.gradudes.entity.PersonInfo;
import com.cxxy.qiu.gradudes.service.PersonInfoService;

@Service
public class PersonInfoServiceImpl implements PersonInfoService{

	@Autowired
	private PersonInfoDao personInfoDao;
	
	@Override
	public PersonInfo getPersonInfoById(Long userId) {
		return personInfoDao.queryPersonInfoById(userId);
	}

}
