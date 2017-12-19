package test;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class LeeRedisTest {
	private ApplicationContext context;
	@Before
	public void setUp(){
	     context = new ClassPathXmlApplicationContext("classpath:applicationContext-redis.xml");
	}
	@Test
	public void testRedis(){
		JedisPool jedisPool = (JedisPool) context.getBean("jedisPool");
		Jedis jedis = jedisPool.getResource();
//		jedis.auth("941125");
		jedis.set("user_session_id",UUID.randomUUID().toString());
		System.out.println("删除之前： " + jedis.get("user_session_id"));
		jedis.del("user_session_id");
		System.out.println("删除之后" + jedis.get("user_session_id"));
		jedis.close();
	}
}
