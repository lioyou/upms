package com.leecp.upms.api;

import com.alibaba.fastjson.JSONArray;
import com.leecp.upms.common.base.BaseService;
import com.leecp.upms.dao.model.UpmsPermission;
import com.leecp.upms.dao.model.UpmsPermissionExample;

public interface UpmsPermissionService extends BaseService<UpmsPermission,UpmsPermissionExample> {
	/**
	 * 根据用户Id返回权限的JSONArray数组
	 * @param upmsUserId
	 * @param type
	 * @return 权限集合
	 */
	JSONArray selectUpmsPermissionTreeByUpmsUserId(Integer upmsUserId,byte type);
	
	
	/**
	 * 根据角色Id返回权限的JSONArray数组
	 * @param upmsRoleId
	 * @return 权限集合
	 */
	JSONArray selectUpmsPermissionTreeByUpmsRoleId(Integer upmsRoleId);
	
	
}
