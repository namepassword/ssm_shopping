package com.cxxy.qiu.gradudes.entity;

import java.util.Date;

public class WechatAuth {
    //id
	private Long wechatAuthId;
	//openID 微信账号和公众号绑定的标识
	private String openId;
	//创建时间
	private Date createTime;
	//用户信息实体类
	private PersonInfo personInfo;
	
	public Long getWechatAuthId() {
		return wechatAuthId;
	}
	public void setWechatAuthId(Long wechatAuthId) {
		this.wechatAuthId = wechatAuthId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public PersonInfo getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
	@Override
	public String toString() {
		return "WechatAuth [wechatAuthId=" + wechatAuthId + ", openId=" + openId + ", createTime=" + createTime
				+ ", personInfo=" + personInfo + "]";
	}
	
	
	
}
