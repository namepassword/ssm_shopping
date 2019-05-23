package com.cxxy.qiu.gradudes.dao;

import java.util.HashSet;
/*
 * 迎合echart里的XAxis项
 * 
 * */
public class EchartXAxis {
	private String type = "category";
	//去重复
	private HashSet<String> data;

	public HashSet<String> getData() {
		return data;
	}

	public void setData(HashSet<String> data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

}
