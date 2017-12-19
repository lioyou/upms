package com.leecp.upms.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leecp.upms.api.UpmsUserPermissionService;
import com.leecp.upms.common.base.BaseServiceImpl;
import com.leecp.upms.dao.mapper.UpmsUserMapper;
import com.leecp.upms.dao.mapper.UpmsUserPermissionMapper;
import com.leecp.upms.dao.model.UpmsUserExample;
import com.leecp.upms.dao.model.UpmsUserPermission;
import com.leecp.upms.dao.model.UpmsUserPermissionExample;
@Service
@Transactional
public class UpmsUserPermissionServiceImpl extends BaseServiceImpl<UpmsUserPermissionMapper,UpmsUserPermission,UpmsUserPermissionExample> implements UpmsUserPermissionService {
	private static final Logger log = LoggerFactory.getLogger(UpmsUserPermissionServiceImpl.class);
	
	@Autowired
	private UpmsUserMapper upmsUserMapper; 
	@Autowired
	private UpmsUserPermissionMapper upmsUserPermissionMapper; 
	
	
	public int distribute(JSONArray data, Integer userId) {
		/*
		 * 1.查询用户
		 * 2.添加，判断check,true，则添加，false则删除
		 * 3.返回结果
		 */
		if(userId == null) return 0;
		UpmsUserExample upmsUserExample = new UpmsUserExample();
		upmsUserExample.createCriteria().andUserIdEqualTo(userId);
		int user = upmsUserMapper.countByExample(upmsUserExample);
		if(user <= 0) return 0;
		if(null == data || data.size() == 0) return 0;
		int result = 0;
		for(Object obj : data){
			JSONObject userPermission = (JSONObject)obj;
			if(userPermission.getBoolean("checked")){
				UpmsUserPermission upmsUserPermission = new UpmsUserPermission();
				upmsUserPermission.setUserId(userId);
				upmsUserPermission.setPermissionId(userPermission.getInteger("id"));
				upmsUserPermission.setType(userPermission.getByte("type"));
				result += upmsUserPermissionMapper.insertSelective(upmsUserPermission);
			}else{
				UpmsUserPermissionExample upmsUserPermissionExample = new UpmsUserPermissionExample();
				upmsUserPermissionExample.createCriteria().andUserIdEqualTo(userId).andPermissionIdEqualTo(userPermission.getInteger("id"));
				result += upmsUserPermissionMapper.deleteByExample(upmsUserPermissionExample);
			}
		}
		
		return result;
	}
	public int insertTen(List<UpmsUserPermission> users){
		int result = 0;
		int cnt = 0;
		for(UpmsUserPermission user : users){
			result += upmsUserPermissionMapper.insertSelective(user);
			cnt++;
			if(cnt==5){
				throw new RuntimeException("===============================现在抛出异常了===============================");
			}
		}
		return result;
	}
}
