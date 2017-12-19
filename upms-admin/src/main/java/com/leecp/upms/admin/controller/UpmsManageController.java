package com.leecp.upms.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.leecp.upms.api.UpmsApiService;
import com.leecp.upms.api.UpmsPermissionService;
import com.leecp.upms.api.UpmsSystemService;
import com.leecp.upms.common.base.BaseController;
import com.leecp.upms.common.base.BaseResult;
import com.leecp.upms.common.constant.ResultConstant;
import com.leecp.upms.dao.model.UpmsPermission;
import com.leecp.upms.dao.model.UpmsSystem;
import com.leecp.upms.dao.model.UpmsSystemExample;
import com.leecp.upms.dao.model.UpmsUser;

/**
 * 权限控制主界面
 */
@Controller
@RequestMapping("/manage")
public class UpmsManageController extends BaseController{
	@Autowired
	private UpmsSystemService upmsSystemService;
	@Autowired
	private UpmsApiService upmsApiService;
	@Autowired 
	private UpmsPermissionService upmsPermissionService;
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(HttpServletRequest request,Model model){
		/*
		 * 
		 * 主界面数据展示：顶部导航 aside(目录)
		 * 1.取出系统的数据
		 * 2.取出该系统的目录
		 * 3.给model赋值，返回
		 * 4.jsp中取出foreach
		 */
		//查询系统
		UpmsSystemExample upmsSystemExample = new UpmsSystemExample();
		upmsSystemExample.createCriteria().andStatusEqualTo((byte)1);
		List<UpmsSystem> upmsSystem = upmsSystemService.selectByExample(upmsSystemExample);
		model.addAttribute("systems",upmsSystem);
		//获取用户名
		Subject subject = SecurityUtils.getSubject();
		String username = (String)subject.getPrincipal();
		 model.addAttribute("username", username);	
		
		return "/manage/index";
	}
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody
	public Object list(){
		//查询用户
		Subject subject = SecurityUtils.getSubject();
		String username = (String)subject.getPrincipal();
		UpmsUser upmsUser = upmsApiService.selectUpmsUserByUsername(username);
		if(null == upmsUser) return new BaseResult(ResultConstant.Fail,null);
		List<UpmsPermission> permissions = upmsApiService.selectUpmsPermissionByUpmsUserId(upmsUser.getUserId()); 
		Object data = JSONArray.toJSON(permissions);
		return new BaseResult(ResultConstant.SUCCESS,data);	
	}
}
