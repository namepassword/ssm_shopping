package com.cxxy.qiu.gradudes.config.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.cxxy.qiu.gradudes.service.ProductSellDailyService;

@Configuration
public class QuartzConfiguration {

	@Autowired
	private ProductSellDailyService productSellDailyService;
	@Autowired
	private MethodInvokingJobDetailFactoryBean jobDetailFactory;
	@Autowired
	private CronTriggerFactoryBean productSellDailyTriggerFactory;
	
	/*
	 * 创建jobDetailFactory并返回
	 * 
	 * */
	@Bean(name="jobDetailFactory")
	public MethodInvokingJobDetailFactoryBean createJobDeatil() {
		 //new出jobDetail Factory对象，此工厂主要用来制作一个jobDetail，即制作一个任务
		//由于所作的定时任务根本上讲其实就是执行一个方法，所以这个工厂比较方便使用
		MethodInvokingJobDetailFactoryBean jobDetailFactoryBean=new MethodInvokingJobDetailFactoryBean();
		//设置jobDetail的名字
		jobDetailFactoryBean.setName("product_sell_daily_job");
		//设置jobDetail组的名字
		jobDetailFactoryBean.setGroup("job_sell_daily_group");
		//对于相同的JobDetail当指定多个Trigger时，很可能第一个job完成之前，第二个奇偶比就开始了
		//制定concurrent设为false，多个job不会并发运行，第二个job将不会在第一个job完成之前开始
		jobDetailFactoryBean.setConcurrent(false);
		//指定运行任务的类
		jobDetailFactoryBean.setTargetObject(productSellDailyService);
		//指定运行任务的方法
		jobDetailFactoryBean.setTargetMethod("dailyCalculate");
		return jobDetailFactoryBean;
	}
	/*
	 * 创建CronTriggerFactoryBean并返回
	 * */
	@Bean(name="productSellDailyTriggerFactory")
	public CronTriggerFactoryBean createProductSellDailyTrigger() {
		//创建triggerFactory实例，用来创建trigger
		CronTriggerFactoryBean triggerFactory =new CronTriggerFactoryBean();
		//设置triggerFactory的名字
		triggerFactory.setName("product_sell_daily_trigger");
		//设置triggerFactory的组名
		triggerFactory.setGroup("job_product_sell_daily_group");
		//绑定jobDeatil
		triggerFactory.setJobDetail(jobDetailFactory.getObject());
		//设定cron表达式 
		triggerFactory.setCronExpression("0 0 0 * * ? *");
		return triggerFactory;
	}
	/*
	 * 创建调度工厂并返回
	 * 
	 * */
	@Bean(name="schedulerFacotry")
	public SchedulerFactoryBean createSchedulerFactory() {
		SchedulerFactoryBean schedulerFacotry =new SchedulerFactoryBean();
		schedulerFacotry.setTriggers(productSellDailyTriggerFactory.getObject());
		return schedulerFacotry;
	}
	
}
