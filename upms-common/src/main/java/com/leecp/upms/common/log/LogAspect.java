package com.leecp.upms.common.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.rpc.RpcContext;

/**
 * 使用AOP记录Service操作日志
 * @author LeeCP
 *
 */
@Service
@Aspect
public class LogAspect {
	private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
	//使用环绕对日志进行记录
	@Around("execution(* com.leecp.upms.service.impl.*.*(..))")
	public Object log(ProceedingJoinPoint proceedingJoinPoint){
		Object result = null;
		try {
			//调用目标方法，注意，一定要将结果返回，不然，目标方法将无返回值
			result = proceedingJoinPoint.proceed();
			// 是否是消费端
			boolean consumerSide = RpcContext.getContext().isConsumerSide();
			// 获取最后一次提供方或调用方IP
			String ip = RpcContext.getContext().getRemoteHost();
			// 服务url
			String rpcUrl = RpcContext.getContext().getUrl().getParameter("application");
			log.info("consumerSide={}, ip={}, url={}", consumerSide, ip, rpcUrl);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
