package com.leecp.upms.client.shiro.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 对请求的参数进行过滤操作
 * @author LeeCP
 *
 */
public class ParameterUtil {
	public static String removeToken(HttpServletRequest request){
		StringBuffer backurl = request.getRequestURL();
		String params = "";
		Map<String,String[]> parameterMap = request.getParameterMap();
		for(Map.Entry<String,String[]> entry : parameterMap.entrySet()){
			if(!entry.getKey().equals("upms_token") && !entry.getKey().equals("upms_username")){
				params += "&" + entry.getKey() + entry.getValue()[0];
			}
		}
		if(params.startsWith("&")){
			params = params.substring(1);
		}
		backurl.append("?").append(params);
		return backurl.toString();
	}
}
