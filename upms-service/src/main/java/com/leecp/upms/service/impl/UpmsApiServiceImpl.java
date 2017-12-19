package com.leecp.upms.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leecp.upms.api.UpmsApiService;
import com.leecp.upms.dao.mapper.UpmsApiMapper;
import com.leecp.upms.dao.mapper.UpmsLogMapper;
import com.leecp.upms.dao.mapper.UpmsRolePermissionMapper;
import com.leecp.upms.dao.mapper.UpmsSystemMapper;
import com.leecp.upms.dao.mapper.UpmsUserMapper;
import com.leecp.upms.dao.mapper.UpmsUserPermissionMapper;
import com.leecp.upms.dao.model.UpmsLogWithBLOBs;
import com.leecp.upms.dao.model.UpmsPermission;
import com.leecp.upms.dao.model.UpmsRole;
import com.leecp.upms.dao.model.UpmsRolePermission;
import com.leecp.upms.dao.model.UpmsRolePermissionExample;
import com.leecp.upms.dao.model.UpmsSystem;
import com.leecp.upms.dao.model.UpmsSystemExample;
import com.leecp.upms.dao.model.UpmsUser;
import com.leecp.upms.dao.model.UpmsUserExample;
import com.leecp.upms.dao.model.UpmsUserPermission;
import com.leecp.upms.dao.model.UpmsUserPermissionExample;
@Service
@Transactional
public class UpmsApiServiceImpl implements UpmsApiService {
    private static Logger log = LoggerFactory.getLogger(UpmsApiServiceImpl.class);

    @Autowired
    UpmsUserMapper upmsUserMapper;

    @Autowired
    UpmsApiMapper upmsApiMapper;

    @Autowired
    UpmsRolePermissionMapper upmsRolePermissionMapper;

    @Autowired
    UpmsUserPermissionMapper upmsUserPermissionMapper;

    @Autowired
    UpmsSystemMapper upmsSystemMapper;


    @Autowired
    UpmsLogMapper upmsLogMapper;

	public List<UpmsPermission> selectUpmsPermissionByUpmsUserId(Integer upmsUserId) {
		if(null == upmsUserId){
			log.debug("selectUpmsPermissionByUpmsUserId,upmsuserId =null");
			return null;
		}else{
			UpmsUser upmsUser = upmsUserMapper.selectByPrimaryKey(upmsUserId);
			if(null == upmsUser || 0 == upmsUser.getStatus()){
				log.debug("selectUpmsPermissionByUpmsUserId upmsUserId={}：无此用户或被锁定", upmsUserId);
				return null;
			}
		}
		List<UpmsPermission> result = upmsApiMapper.selectUpmsPermissionByUpmsUserId(upmsUserId);
		log.debug("selectUpmsPermissionByUpmsUserId,upmsuserId ={}：执行成功",upmsUserId);
		return result;
		
	}
	//这个暂不实现
	public List<UpmsPermission> selectUpmsPermissionByUpmsUserIdByCache(
			Integer upmsUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<UpmsRole> selectUpmsRoleByUpmsUserId(Integer upmsUserId) {
		if(null == upmsUserId){
			log.debug("selectUpmsRoleByUpmsUserId upmsUserId=null");
			return null;
		}else{//判断用户是否存在或锁定
			UpmsUser upmsUser = upmsUserMapper.selectByPrimaryKey(upmsUserId);
			if(null == upmsUser || 0 == upmsUser.getStatus()){
				log.debug("selectUpmsPermissionByUpmsUserId upmsUserId={}：无此用户或被锁定", upmsUserId);
				return null;
			}
		}
		List<UpmsRole> result = upmsApiMapper.selectUpmsRoleByUpmsUserId(upmsUserId);
		log.debug("selectUpmsRoleByUpmsUserId,upmsuserId ={}：成功",upmsUserId);
		return result;
	}

	public List<UpmsRole> selectUpmsRoleByUpmsUserIdByCache(Integer upmsUserId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<UpmsRolePermission> selectUpmsRolePermissionByUpmsRoleId(
			Integer upmsRoleId) {
		if(null == upmsRoleId){
			log.debug("selectUpmsRolePermisstionByUpmsRoleId upmsRole=null");
			return null;
		}
		UpmsRolePermissionExample upmsRoleExample = new UpmsRolePermissionExample();
		upmsRoleExample.createCriteria().andRoleIdEqualTo(upmsRoleId);
		List<UpmsRolePermission> result = upmsRolePermissionMapper.selectByExample(upmsRoleExample);
		log.debug("selectUpmsRolePermisstionByUpmsRoleId upmsRoleId={}:成功",upmsRoleId);
		return result;
	}

	public List<UpmsUserPermission> selectUpmsUserPermissionByUpmsUserId(
			Integer upmsUserId) {
		if(null == upmsUserId){
			log.debug("selectUpmsUserPermissionByUpmsUserId upmsUserId=null");
			return null;
		}else{
			UpmsUser upmsUser = upmsUserMapper.selectByPrimaryKey(upmsUserId);
			if(null == upmsUser || 0 == upmsUser.getStatus()){
				log.debug("selectUpmsUserPermissionByUpmsUserId upmsUserId={}：无此用户或被锁定", upmsUserId);
				return null;
			}
		}
		UpmsUserPermissionExample  upmUserPermissionExample = new UpmsUserPermissionExample();
		upmUserPermissionExample.createCriteria().andUserIdEqualTo(upmsUserId);
		List<UpmsUserPermission> result = upmsUserPermissionMapper.selectByExample(upmUserPermissionExample);
		log.debug("selectUpmsUserPermissionByUpmsUserId upmsUserId={}:成功",upmsUserId);
		return result;
	}

	public List<UpmsSystem> selectUpmsSystemByExample(
			UpmsSystemExample upmsSystemExample) {
		if(null == upmsSystemExample){
			log.debug("selectUpmsSystemByExample upmsSystemExample=null");
			return null;
		}
		List<UpmsSystem> result = upmsSystemMapper.selectByExample(upmsSystemExample);
		log.debug("selectUpmsSystemByExample upmsSystemExample={}：成功", upmsSystemExample);
		return result;
	}

	public UpmsUser selectUpmsUserByUsername(String username) {
		if(null == username || "".equals(username)){
			log.debug("selectUpmsUserByUsername username=null or ''");
			return null;
		}
		UpmsUserExample upmsUserExample = new UpmsUserExample();
		upmsUserExample.createCriteria().andUsernameEqualTo(username);
		List<UpmsUser> upmsUsers = upmsUserMapper.selectByExample(upmsUserExample);
		if(null == upmsUsers || upmsUsers.size() <=0){
			return null;
		}
		log.debug("selectUpmsUserByUsername username={}:成功",username);
		return upmsUsers.get(0);
	}

	public int insertUpmsLogSelective(UpmsLogWithBLOBs record) {
		if(null == record){
			log.debug("insertUpmsLogSelective record=null");
			return 0;
		}
		int result = upmsLogMapper.insertSelective(record);
		log.debug("insertUpmsLogSelective record={}",record);
		return result;
	}

}
