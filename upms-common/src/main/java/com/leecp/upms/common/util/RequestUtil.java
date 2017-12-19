package com.leecp.upms.common.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 对请求进行处理
 */
public class RequestUtil {
	public static String removeParameter(HttpServletRequest request,String parameterName){
		Map<String,String[]> parameters = request.getParameterMap();
		if(null == parameters || parameters.isEmpty()) return "";
		StringBuffer queryString = new StringBuffer();
		for(Map.Entry<String, String[]> parameter : parameters.entrySet()){
			if(parameter.getKey() == parameterName) continue;
			queryString.append("&").append(parameter.getKey()).append("=").append(parameter.getValue());
		}
		return queryString.substring(1);
	}
}
