package com.leecp.upms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leecp.upms.api.UpmsRolePermissionService;
import com.leecp.upms.common.base.BaseServiceImpl;
import com.leecp.upms.dao.mapper.UpmsRolePermissionMapper;
import com.leecp.upms.dao.model.UpmsRolePermission;
import com.leecp.upms.dao.model.UpmsRolePermissionExample;
@Service
@Transactional
public class UpmsRolePermissionServiceImpl extends BaseServiceImpl<UpmsRolePermissionMapper,UpmsRolePermission,UpmsRolePermissionExample> implements UpmsRolePermissionService{
	
	@Autowired
	private UpmsRolePermissionMapper upmsRolePermissionMapper;
	
	public int distributePermission(int roleId, String rolePermissions) {
		/*
		 * 1.判断rolePermissions null?
		 * 2.用JSONArray来解析
		 * 3.foreach,cheked=true,insert false delete
		 * 4.返回结果
		 * 
		 */
		if(null == rolePermissions) return 0;
		JSONArray permissions = (JSONArray) JSONArray.parse(rolePermissions);
		int result =0;
		List<Integer> deleteIds = new ArrayList<Integer>();
		for(Object obj : permissions){
			JSONObject permission = (JSONObject)obj;
			if(permission.getBoolean("checked")){
				UpmsRolePermission upmsRolePermission = new UpmsRolePermission();
				upmsRolePermission.setRoleId(roleId);
				upmsRolePermission.setPermissionId(permission.getInteger("id"));
				result += upmsRolePermissionMapper.insertSelective(upmsRolePermission);
			}else{//删除权限
				deleteIds.add(permission.getInteger("id"));
			}
			
			if(deleteIds.size() > 0){
				UpmsRolePermissionExample example = new UpmsRolePermissionExample();
				example.createCriteria().andPermissionIdIn(deleteIds).andRoleIdEqualTo(roleId);
				result += upmsRolePermissionMapper.deleteByExample(example);
			}
		}
		return result;
	}
	
}
