package com.leecp.upms.common.base;

import com.leecp.upms.common.constant.ResultConstant;

/**
 * 统一返回结果类
 * 定义三个成员变量，code message data
 * @author LeeCP
 *
 */
public class BaseResult {
	//状态码，成功为1，失败为其它
	private int code;
	//消息，成功为：成功，失败为其它
	private String message;
	//返回的数据
	private Object data;
	
	public BaseResult(ResultConstant resultConstant,Object data){
		this.code = resultConstant.getCode();
		this.message = resultConstant.getMessage();
		this.data = data;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
