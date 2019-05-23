
package com.cxxy.qiu.gradudes.config.web;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.cxxy.qiu.gradudes.interceptor.shopadmin.ShopLoginInterceptor;
import com.cxxy.qiu.gradudes.interceptor.shopadmin.ShopPermissionInterceptor;
import com.cxxy.qiu.gradudes.interceptor.superadmin.SuperLoginInterceptor;
import com.google.code.kaptcha.servlet.KaptchaServlet;

/*开启mvc自动注入spring容器，webMvcConfigurationAdapter配置试图解析器
*当一个类实现这个接口之后，这个类就方便获得applicationContext中的所有bean
*/

@Configuration
//等价于<mvc:annotation-driven/>
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware {

	// spring容器
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	// 静态资源配置
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

	registry.addResourceHandler("/upload/**").addResourceLocations("file:/usr/project/image/upload/");
	 //registry.addResourceHandler("/upload/**").addResourceLocations("file:D:/project/image/upload/");
	}

	// 定义默认的请求处理器
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	// 创建viewResolver

	@Bean(name = "viewResolver")
	public ViewResolver createViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		// 设置Spring容器
		viewResolver.setApplicationContext(this.applicationContext);
		// 取消缓存
		viewResolver.setCache(false);
		// 设置解析器
		viewResolver.setPrefix("/WEB-INF/html/");
		// 设置试图解析的后缀
		viewResolver.setSuffix(".html");
		return viewResolver;
	}

	// 文件上传解析器

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver createMultipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding("utf-8");
		// 1024*1024*20=20M
		multipartResolver.setMaxUploadSize(20971520);
		multipartResolver.setMaxInMemorySize(20971520);
		return multipartResolver;
	}

	@Value("${kaptcha.border}")
	private String border;
	@Value("${kaptcha.textproducer.font.color}")
	private String fcolor;
	@Value("${kaptcha.image.width}")
	private String width;
	@Value("${kaptcha.image.height}")
	private String height;
	@Value("${kaptcha.textproducer.char.string}")
	private String cString;
	@Value("${kaptcha.textproducer.font.size}")
	private String fsize;
	@Value("${kaptcha.noise.color}")
	private String nColor;
	@Value("${kaptcha.textproducer.char.length}")
	private String clength;
	@Value("${kaptcha.textproducer.font.names}")
	private String fnames;

	/* 验证码 */
	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(), "/Kaptcha");
		servlet.addInitParameter("kaptcha.border", border);// 无边框
		servlet.addInitParameter("kaptcha.textproducer.font.color", fcolor);// 字体颜色
		servlet.addInitParameter("kaptcha.image.width", width);// 图片宽度
		servlet.addInitParameter("kaptcha.image.height", height);// 图片高度
		servlet.addInitParameter("kaptcha.textproducer.char.string", cString);// 使用哪些字
		servlet.addInitParameter("kaptcha.textproducer.font.size", fsize);// 字体大小
		servlet.addInitParameter("kaptcha.noise.color", nColor);// 干扰线的颜色
		servlet.addInitParameter("kaptcha.textproducer.char.length", clength);// 字符个数
		servlet.addInitParameter("kaptcha.textproducer.font.names", fnames);// 字体

		return servlet;
	}

	/* 拦截器 */
	public void addInterceptors(InterceptorRegistry registry) {
		String interceptPath = "/shopadmin/**";
		String interceptSuperPath = "/superadmin/**";
		
		// 注册拦截器
		InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInterceptor());
		InterceptorRegistration loginSL = registry.addInterceptor(new SuperLoginInterceptor());
		
		// 配置拦截器路径
		loginIR.addPathPatterns(interceptPath);

		loginSL.addPathPatterns(interceptSuperPath);

		
		// 还可以注册其他的拦截器
		InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInterceptor());
		// 配置拦截器路径
		permissionIR.addPathPatterns(interceptPath);
		// 配置不拦截的路径
		/** shoplist page **/
		permissionIR.excludePathPatterns("/shopadmin/shoplist");
		permissionIR.excludePathPatterns("/shopadmin/getshoplist");
		/** shopregister page **/
		permissionIR.excludePathPatterns("/shopadmin/getshopinitinfo");
		permissionIR.excludePathPatterns("/shopadmin/registershop");
		permissionIR.excludePathPatterns("/shopadmin/shopoperation");
		/** shopmanage page **/
		permissionIR.excludePathPatterns("/shopadmin/shopmanagement");
		permissionIR.excludePathPatterns("/shopadmin/getshopmanagementinfo");
		/** shopauthmanagement page **/
		permissionIR.excludePathPatterns("/shopadmin/addshopauthmap");


		loginIR.excludePathPatterns("/shopadmin/exchangeaward");
		loginIR.excludePathPatterns("/shopadmin/adduserproductmap");
		loginIR.excludePathPatterns("/shopadmin/addshopauthmap");
		permissionIR.excludePathPatterns("/shopadmin/exchangeaward");
		permissionIR.excludePathPatterns("/shopadmin/adduserproductmap");
		permissionIR.excludePathPatterns("/shopadmin/addshopauthmap");
	}

}
