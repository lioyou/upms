package com.leecp.upms.api;

import com.leecp.upms.common.base.BaseService;
import com.leecp.upms.dao.model.UpmsRolePermission;
import com.leecp.upms.dao.model.UpmsRolePermissionExample;

public interface UpmsRolePermissionService extends BaseService<UpmsRolePermission,UpmsRolePermissionExample>{
	/**
	 * 分配角色权限
	 * @param roleId
	 * @param rolePermissions 权限Id字符串集合
	 * @return 插入结果
	 */
	int distributePermission(int roleId,String rolePermissions);
}
