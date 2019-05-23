package com.cxxy.qiu.gradudes.config.redis;

import org.apache.commons.collections.set.CompositeSet.SetMutator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cxxy.qiu.gradudes.cache.JedisPoolWriper;
import com.cxxy.qiu.gradudes.cache.JedisUtil;

import redis.clients.jedis.JedisPoolConfig;

/*
 * spring-redis.xml里面的配置
 * 
 * */
@Configuration
public class RedisConfiguration {
     @Value("${redis.hostname}")
	private String hostname;
     @Value("${redis.port}")
	private int port;
     @Value("${redis.pool.maxActive}")
	private int maxToata;
     @Value("${redis.pool.maxIdle}")
	private int maxIdle;
     @Value("${redis.pool.maxWait}")
	private long maxWaitMills;
     @Value("${redis.pool.testOnBorrow}")
	private boolean testOnBorrow;
	
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	@Autowired
	private JedisPoolWriper jedisWriperPool;
	@Autowired
	private  JedisUtil jedisUtil;
	
	/*
	 * 创建redis连接池的设置
	 * */
	@Bean(name="jedisPoolConfig")
	public JedisPoolConfig createJedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig =new JedisPoolConfig();
		//控制一个pool可分配多个jedis实例
		jedisPoolConfig.setMaxTotal(maxToata);
		//连接池中最多可空闲maxIdle个连接，这里取值20
		//表示即使没有数据库连接时依然可以保持20空闲的练级
		//而不被清除随时处于待命状态
		jedisPoolConfig.setMaxIdle(maxIdle);
		//最大等待时间：当没有可用连接时，
		//连接池等待连接被归还的最大时间，超过时间则抛出异常
		jedisPoolConfig.setMaxWaitMillis(maxWaitMills);
		//在获取连接的时候检查有效性
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		return jedisPoolConfig;
		
	}
	/*
	 * 创建Redis连接池，并作相关配置
	 * 
	 * */
	@Bean(name="jedisWritePool")
	public JedisPoolWriper createJedisPoolWriper() {
		JedisPoolWriper jedisPoolWriper=new JedisPoolWriper(jedisPoolConfig,hostname,port);
		return jedisPoolWriper;
	}
	/*
	 * 创建Redis工具类,封装好Redis的连接进行相关的操作
	 * 
	 * */
	@Bean(name="jedisUtil")
	public JedisUtil createJedisUtil() {
		JedisUtil jedisUtil=new JedisUtil();
		jedisUtil.setJedisPool(jedisWriperPool);
		return jedisUtil;
	}
	/*
	 * Redis的key操作
	 * */
	@Bean(name="jedisKeys")
	public JedisUtil.Keys createJedisKeys(){
		JedisUtil.Keys jedisKeys=jedisUtil.new Keys();
		return jedisKeys;
	}
	/*
	 * Redis的String操作
	 * */
	@Bean(name="jedisStrings")
	public JedisUtil.Strings createJedisStrings(){
		JedisUtil.Strings jedisStrings=jedisUtil.new Strings();
		return jedisStrings;
	}
}
