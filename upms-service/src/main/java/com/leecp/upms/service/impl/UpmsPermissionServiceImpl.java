package com.leecp.upms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leecp.upms.api.UpmsApiService;
import com.leecp.upms.api.UpmsPermissionService;
import com.leecp.upms.common.base.BaseServiceImpl;
import com.leecp.upms.dao.mapper.UpmsPermissionMapper;
import com.leecp.upms.dao.mapper.UpmsSystemMapper;
import com.leecp.upms.dao.mapper.UpmsUserPermissionMapper;
import com.leecp.upms.dao.model.UpmsPermission;
import com.leecp.upms.dao.model.UpmsPermissionExample;
import com.leecp.upms.dao.model.UpmsRolePermission;
import com.leecp.upms.dao.model.UpmsSystem;
import com.leecp.upms.dao.model.UpmsSystemExample;
import com.leecp.upms.dao.model.UpmsUserPermission;
import com.leecp.upms.dao.model.UpmsUserPermissionExample;
@Service
@Transactional
public class UpmsPermissionServiceImpl extends
		BaseServiceImpl<UpmsPermissionMapper, UpmsPermission, UpmsPermissionExample> implements
		UpmsPermissionService {
	private static final Logger log = LoggerFactory.getLogger(UpmsPermissionServiceImpl.class);
	@Autowired
	private UpmsApiService upmsApiService;
	
	@Autowired
	private UpmsSystemMapper upmsSystemMapper;
	
	@Autowired
	private UpmsPermissionMapper upmsPermissionMapper;
	
	@Autowired
	private UpmsUserPermissionMapper upmsUserPermissionMapper;

	public JSONArray selectUpmsPermissionTreeByUpmsUserId(Integer upmsUserId,byte type) {
		//根据系统来划分目录
		//1.获取系统，作为根目录
		//2.获取菜单项
		//3.获取操作项
		//细节问题：nockeck/checked,opend,id,label
		
		/*
		 * 1. 判断type类型
		 *     若是1，则直接查询全部，UpmsApiService
		 *     若是-1，则使用UpmsUserPermissionService来查询减权限
		 * 2. 进行封装List<Integer> permissionIds
		 * 3. 进行比较操作
		 */
		List<Integer> permissionIds = new ArrayList<Integer>();
		if((byte)1 == type){
			//查询用户已拥有的人权限
			List<UpmsPermission> upmsPermissions = upmsApiService.selectUpmsPermissionByUpmsUserId(upmsUserId);
			for(UpmsPermission permission: upmsPermissions){
				permissionIds.add(permission.getPermissionId());
			}
		}else{//type == -1
			UpmsUserPermissionExample upmsUserPermissionExample = new UpmsUserPermissionExample();
			upmsUserPermissionExample.createCriteria().andTypeEqualTo(type).andUserIdEqualTo(upmsUserId);
			List<UpmsUserPermission> upmsUserPermissions = upmsUserPermissionMapper.selectByExample(upmsUserPermissionExample);
			for(UpmsUserPermission permission: upmsUserPermissions){
				permissionIds.add(permission.getPermissionId());
			}
			
		}
		
		
		//获取系统
		UpmsSystemExample upmsSystemExample = new UpmsSystemExample();
		upmsSystemExample.createCriteria().andStatusEqualTo((byte)1);
		List<UpmsSystem> upmsSystems = upmsSystemMapper.selectByExample(upmsSystemExample);
		if(null == upmsSystems ||upmsSystems.size() == 0) return null;
		//system目录
		JSONArray systems = new JSONArray();
	    for(UpmsSystem upmsSystem : upmsSystems){
	    	//获取当前系统权限
	    	UpmsPermissionExample upmsPermissionExample = new UpmsPermissionExample();
	    	upmsPermissionExample.createCriteria().andSystemIdEqualTo(upmsSystem.getSystemId()).andStatusEqualTo((byte)1);
	    	List<UpmsPermission> upmsPermissions = upmsPermissionMapper.selectByExample(upmsPermissionExample); 
	    	//若是当前系统没有任务权限，直接返回，不添加该系统
	    	if(null == upmsPermissions || upmsPermissions.size() == 0) continue;
	    	
	    	//添加系统目录
	    	JSONObject system = new JSONObject();
	    	system.put("id", upmsSystem.getSystemId());
	    	system.put("name", upmsSystem.getLabel());
	    	system.put("nocheck", true);
	    	system.put("open", true);
	    	systems.add(system);
	    	
	    	//添加菜单目录
	    	JSONArray menus = new JSONArray();
	    	for(UpmsPermission upmsPermission : upmsPermissions){
	    		if(1 != upmsPermission.getType() || 0 != upmsPermission.getPid()) continue;
	    		JSONObject menu = new JSONObject();
	    		menu.put("id", upmsPermission.getPermissionId());
	    		menu.put("name", upmsPermission.getLabel());
	    		menu.put("open",true);
	    			//遍历验证此用户是否拥有该权限
		    		for(Integer permissionId : permissionIds){
		    			if(permissionId.intValue() == upmsPermission.getPermissionId().intValue()){
		    				menu.put("checked", true);
		    				break;
		    			}
		    		}
	    		menus.add(menu);
	    		//添加菜单项
	    		JSONArray items = new JSONArray();
		    	for(UpmsPermission upmsPermission2 : upmsPermissions){
		    		if(2 != upmsPermission2.getType() || upmsPermission.getPermissionId() != upmsPermission2.getPid()) continue;
		    		JSONObject item = new JSONObject();
		    		item.put("id", upmsPermission2.getPermissionId());
		    		item.put("name", upmsPermission2.getLabel());
		    		item.put("open",true);
		    			//遍历验证此用户是否拥有该权限
			    		for(Integer permissionId : permissionIds){
			    			if(permissionId.intValue() == upmsPermission2.getPermissionId().intValue()){
			    				item.put("checked", true);
			    				break;
			    			}
			    		}
		    		items.add(item);
		    		//操作按钮
		    		JSONArray buttons = new JSONArray();
		    		for(UpmsPermission upmsPermission3 : upmsPermissions){
			    		if(3 != upmsPermission3.getType() || upmsPermission2.getPermissionId() != upmsPermission3.getPid()) continue;
			    		JSONObject button = new JSONObject();
			    		button.put("id", upmsPermission3.getPermissionId());
			    		button.put("name", upmsPermission3.getLabel());
			    			//遍历验证此用户是否拥有该权限
				    		for(Integer permissionId : permissionIds){
				    			if(permissionId.intValue() == upmsPermission3.getPermissionId().intValue()){
				    				button.put("checked", true);
				    				break;
				    			}
				    		}
			    		
			    		buttons.add(button);
			    	}
		    		if(buttons.size() > 0) item.put("children",buttons);
		    	}
		    	if(items.size() > 0) menu.put("children",items);
		    	
	    	}
	    	if(menus.size() > 0 )system.put("children", menus);
	    }
		return systems;
	}

	public JSONArray selectUpmsPermissionTreeByUpmsRoleId(Integer upmsRoleId) {
		List<UpmsRolePermission> upmsRolePermissions = upmsApiService.selectUpmsRolePermissionByUpmsRoleId(upmsRoleId);
		//获取系统
		UpmsSystemExample upmsSystemExample = new UpmsSystemExample();
		upmsSystemExample.createCriteria().andStatusEqualTo((byte)1);
		List<UpmsSystem> upmsSystems = upmsSystemMapper.selectByExample(upmsSystemExample);
		if(null == upmsSystems ||upmsSystems.size() == 0) return null;
		//system目录
		JSONArray systems = new JSONArray();
	    for(UpmsSystem upmsSystem : upmsSystems){
	    	//获取当前系统权限
	    	UpmsPermissionExample upmsPermissionExample = new UpmsPermissionExample();
	    	upmsPermissionExample.createCriteria().andSystemIdEqualTo(upmsSystem.getSystemId()).andStatusEqualTo((byte)1);
	    	List<UpmsPermission> upmsPermissions = upmsPermissionMapper.selectByExample(upmsPermissionExample); 
	    	//若是当前系统没有任何权限，直接返回，不添加该系统
	    	if(null == upmsPermissions || upmsPermissions.size() == 0) continue;
	    	
	    	//添加系统目录
	    	JSONObject system = new JSONObject();
	    	system.put("id", upmsSystem.getSystemId());
	    	system.put("name", upmsSystem.getLabel());
	    	system.put("nocheck", true);
	    	system.put("open", true);
	    	systems.add(system);
	    	
	    	//添加菜单目录
	    	JSONArray menus = new JSONArray();
	    	for(UpmsPermission upmsPermission : upmsPermissions){
	    		if(1 != upmsPermission.getType() || 0 != upmsPermission.getPid()) continue;
	    		JSONObject menu = new JSONObject();
	    		menu.put("id", upmsPermission.getPermissionId());
	    		menu.put("name", upmsPermission.getLabel());
	    		menu.put("open",true);
	    		//遍历验证此用户是否拥有该权限
	    		for(UpmsRolePermission upmsRolePermission : upmsRolePermissions){
	    			if(upmsRolePermission.getPermissionId().intValue() == upmsPermission.getPermissionId().intValue()){
	    				menu.put("checked", true);
	    			}
	    		}
	    		menus.add(menu);
	    		//添加菜单项
	    		JSONArray items = new JSONArray();
		    	for(UpmsPermission upmsPermission2 : upmsPermissions){
		    		if(2 != upmsPermission2.getType() || upmsPermission.getPermissionId() != upmsPermission2.getPid()) continue;
		    		JSONObject item = new JSONObject();
		    		item.put("id", upmsPermission2.getPermissionId());
		    		item.put("name", upmsPermission2.getLabel());
		    		item.put("open",true);
		    		//遍历验证此用户是否拥有该权限
		    		for(UpmsRolePermission upmsRolePermission : upmsRolePermissions){
		    			if(upmsRolePermission.getPermissionId().intValue() == upmsPermission2.getPermissionId().intValue()){
		    				item.put("checked", true);
		    			}
		    		}
		    		items.add(item);
		    		//操作按钮
		    		JSONArray buttons = new JSONArray();
		    		for(UpmsPermission upmsPermission3 : upmsPermissions){
			    		if(3 != upmsPermission3.getType() || upmsPermission2.getPermissionId() != upmsPermission3.getPid()) continue;
			    		JSONObject button = new JSONObject();
			    		button.put("id", upmsPermission3.getPermissionId());
			    		button.put("name", upmsPermission3.getLabel());
			    		button.put("open",true);
			    		//遍历验证此用户是否拥有该权限
			    		for(UpmsRolePermission upmsRolePermission : upmsRolePermissions){
			    			if(upmsRolePermission.getPermissionId().intValue() == upmsPermission3.getPermissionId().intValue()){
			    				button.put("checked", true);
			    			}
			    		}
			    		
			    		buttons.add(button);
			    	}
		    		if(buttons.size() > 0) item.put("children",buttons);
		    	}
		    	if(items.size() > 0) menu.put("children",items);
	    	}
	    	if(menus.size() > 0) system.put("children", menus);
	    }
		return systems;
	}

}
