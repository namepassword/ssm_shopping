package com.cxxy.qiu.gradudes.dto;

/*
 * 用来接收平台二维码信息
 * 
 * */
public class WechatInfo {
private Long customerId;
private Long productId;
private Long userAwardId;
private Long createTime;
private Long shopId;
public Long getCustomerId() {
	return customerId;
}
public void setCustomerId(Long customerId) {
	this.customerId = customerId;
}
public Long getProductId() {
	return productId;
}
public void setProductId(Long productId) {
	this.productId = productId;
}
public Long getUserAwardId() {
	return userAwardId;
}
public void setUserAwardId(Long userAwardId) {
	this.userAwardId = userAwardId;
}
public Long getCreateTime() {
	return createTime;
}
public void setCreateTime(Long createTime) {
	this.createTime = createTime;
}
public Long getShopId() {
	return shopId;
}
public void setShopId(Long shopId) {
	this.shopId = shopId;
}
@Override
public String toString() {
	return "WechatInfo [customerId=" + customerId + ", productId=" + productId + ", userAwardId=" + userAwardId
			+ ", createTime=" + createTime + ", shopId=" + shopId + "]";
}


}
