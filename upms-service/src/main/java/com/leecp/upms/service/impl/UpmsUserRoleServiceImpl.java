package com.leecp.upms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leecp.upms.api.UpmsUserRoleService;
import com.leecp.upms.common.base.BaseServiceImpl;
import com.leecp.upms.dao.mapper.UpmsUserMapper;
import com.leecp.upms.dao.mapper.UpmsUserRoleMapper;
import com.leecp.upms.dao.model.UpmsUser;
import com.leecp.upms.dao.model.UpmsUserRole;
import com.leecp.upms.dao.model.UpmsUserRoleExample;
@Service
@Transactional
public class UpmsUserRoleServiceImpl extends BaseServiceImpl<UpmsUserRoleMapper,UpmsUserRole,UpmsUserRoleExample> implements UpmsUserRoleService{
	private static final Logger log = LoggerFactory.getLogger(UpmsUserRoleServiceImpl.class);
	
	@Autowired
	private UpmsUserMapper upmsUserMapper;
	
	@Autowired
	private UpmsUserRoleMapper upmsUserRoleMapper;
	
	public int distributeRole(Integer userId, String[] roleId) {
		/*
		 * 1.查询userId
		 * 2.插入数据
		 */
		if(userId == null) return 0;
		UpmsUser upmsUser = upmsUserMapper.selectByPrimaryKey(userId);
		if(null == upmsUser) return 0;
		//删除用户原先所拥有的全部角色
		UpmsUserRoleExample upmsUserRoleExample = new UpmsUserRoleExample();
		upmsUserRoleExample.createCriteria().andUserIdEqualTo(userId);
		upmsUserRoleMapper.deleteByExample(upmsUserRoleExample);
		if(roleId == null || roleId.length == 0) return 0;
		int result = 0;
		for(String id : roleId){
			if("null".equalsIgnoreCase(id)) continue;
			UpmsUserRole upmsUserRole = new UpmsUserRole();
			upmsUserRole.setUserId(userId);
			upmsUserRole.setRoleId(Integer.parseInt(id));
			result += upmsUserRoleMapper.insertSelective(upmsUserRole);
		}
		return result;
	}
	
}
