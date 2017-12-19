package com.leecp.upms.service;


import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * 服务启动类
 */
public class UpmsServiceApplication {

		private static Logger log = LoggerFactory.getLogger(UpmsServiceApplication.class);

		public static void main(String[] args) throws Exception {
			CountDownLatch countDownLatch = new CountDownLatch(1); 
			log.info(">>>>> upms-service 正在启动 <<<<<");
			new ClassPathXmlApplicationContext("classpath*:applicationContext-*.xml").start();
			log.info(">>>>> upms-service 启动完成 <<<<<");
			System.out.println("服务已启动");
			countDownLatch.await();
			System.out.println("服务已经停止");
		}


}
