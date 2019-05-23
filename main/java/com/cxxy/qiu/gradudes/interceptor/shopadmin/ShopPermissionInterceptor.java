package com.cxxy.qiu.gradudes.interceptor.shopadmin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cxxy.qiu.gradudes.entity.Shop;

/*
 * 店家管理系统操作验证拦截器
 * */
public class ShopPermissionInterceptor extends HandlerInterceptorAdapter{
	/*
	 * 主要做事前拦截，即用户操作发生前，改写preHandle里的逻辑，进行拦截
	 * 
	 * */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//从session中获取当前选择的店铺
		Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
		//从session中获取当前用户可操作的店铺列表
		@SuppressWarnings("unchecked")
		List<Shop> shopList=(List<Shop>) request.getSession().getAttribute("shopList");
		//非空判断
		if(currentShop!=null&&shopList!=null){
			//遍历可操作的店铺列表
			for (Shop shop : shopList) {
				//如果当前店铺在可操作的列表里返回了true，进行接下来的用户操作
				if(shop.getShopId()==currentShop.getShopId()){
					return true;
				}
			}
		}
		//若不满足拦截器的验证则返回false总之用户执行的操纵
		return false;
	}
	
}
