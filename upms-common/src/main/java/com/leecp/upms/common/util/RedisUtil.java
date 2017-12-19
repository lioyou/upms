package com.leecp.upms.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Redis CRUD工具类
 * @author LeeCP
 *
 */
public class RedisUtil {
	private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);
	private static final String PASSWORD = PropertiesFileUtil.getInstance("redis").get("master.password");
	@Autowired
	private static JedisPool jedisPool;
	
	/**
	 * 为了给JedisPool 进行注入才设置的Getter与Setter方法
	 * 同时，不能使用Static修饰符，否则注入失败
	 */
	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public  void setJedisPool(JedisPool jedisPool) {
		RedisUtil.jedisPool = jedisPool;
	}

	/**
	 * 获取Jedis连接实例
	 * @return jedis实例
	 */
	public synchronized static Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (Exception e) {
			log.error("无法获取连接资源对象Jedis" + e);
		}
		return jedis;
	}
	
	/**
	 * 设置 String
	 * @param key
	 * @param value
	 */
	public synchronized static void set(String key, String value) {
		try {
			value = StringUtils.isBlank(value) ? "" : value;
			Jedis jedis = getJedis();
			jedis.set(key, value);
			jedis.close();
		} catch (Exception e) {
			log.error("Set key error : " + e);
		}
	}

	/**
	 * 设置 byte[]
	 * @param key
	 * @param value
	 */
	public synchronized static void set(byte[] key, byte[] value) {
		try {
			Jedis jedis = getJedis();
			jedis.set(key, value);
			jedis.close();
		} catch (Exception e) {
			log.error("Set key error : " + e);
		}
	}

	/**
	 * 设置 String 过期时间
	 * @param key
	 * @param value
	 * @param seconds 以秒为单位
	 */
	public synchronized static void set(String key, String value, int seconds) {
		try {
			value = StringUtils.isBlank(value) ? "" : value;
			Jedis jedis = getJedis();
			jedis.setex(key, seconds, value);
			jedis.close();
		} catch (Exception e) {
			log.error("Set keyex error : " + e);
		}
	}

	/**
	 * 设置 byte[] 过期时间
	 * @param key
	 * @param value
	 * @param seconds 以秒为单位
	 */
	public synchronized static void set(byte[] key, byte[] value, int seconds) {
		try {
			Jedis jedis = getJedis();
			jedis.set(key, value);
			jedis.expire(key, seconds);
			jedis.close();
		} catch (Exception e) {
			log.error("Set key error : " + e);
		}
	}

	/**
	 * 获取String值
	 * @param key
	 * @return value
	 */
	public synchronized static String get(String key) {
		Jedis jedis = getJedis();
		String value = jedis.get(key);
		jedis.close();
		return value;
	}

	/**
	 * 获取byte[]值
	 * @param key
	 * @return value
	 */
	public synchronized static byte[] get(byte[] key) {
		Jedis jedis = getJedis();
		byte[] value = jedis.get(key);
		jedis.close();
		return value;
	}

	/**
	 * 删除值
	 * @param key
	 */
	public synchronized static void remove(String key) {
		try {
			Jedis jedis = getJedis();
			jedis.del(key);
			jedis.close();
		} catch (Exception e) {
			log.error("Remove keyex error : " + e);
		}
	}

	/**
	 * 删除值
	 * @param key
	 */
	public synchronized static void remove(byte[] key) {
		try {
			Jedis jedis = getJedis();
			jedis.del(key);
			jedis.close();
		} catch (Exception e) {
			log.error("Remove keyex error : " + e);
		}
	}

	/**
	 * lpush
	 * @param key
	 * @param key
	 */
	public synchronized static void lpush(String key, String... strings) {
		try {
			Jedis jedis = RedisUtil.getJedis();
			jedis.lpush(key, strings);
			jedis.close();
		} catch (Exception e) {
			log.error("lpush error : " + e);
		}
	}

	/**
	 * lrem
	 * @param key
	 * @param count 删除list中count个值为value的数
	 * @param value
	 */
	public synchronized static void lrem(String key, long count, String value) {
		try {
			Jedis jedis = RedisUtil.getJedis();
			jedis.lrem(key, count, value);
			jedis.close();
		} catch (Exception e) {
			log.error("lrem error : " + e);
		}
	}

	/**
	 * sadd
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public synchronized static void sadd(String key, String value, int seconds) {
		try {
			Jedis jedis = RedisUtil.getJedis();
			jedis.sadd(key, value);
			jedis.expire(key, seconds);
			jedis.close();
		} catch (Exception e) {
			log.error("sadd error : " + e);
		}
	}
	
	/**
	 * 删除set中的值
	 * @param key
	 * @param value
	 */
	public synchronized static void srem(String key,String value){
		try {
			Jedis jedis = RedisUtil.getJedis();
			jedis.srem(key, value);
			jedis.close();
		} catch (Exception e) {
			log.error("srem error : " + e);
		}
	}

	/**
	 * incr
	 * @param key
	 * @return value
	 */
	public synchronized static Long incr(String key) {
		Jedis jedis = getJedis();
		long value = jedis.incr(key);
		jedis.close();
		return value;
	}

	/**
	 * decr
	 * @param key
	 * @return value
	 */
	public synchronized static Long decr(String key) {
		Jedis jedis = getJedis();
		long value = jedis.decr(key);
		jedis.close();
		return value;
	}
}
