package com.leecp.upms.api;

import com.leecp.upms.common.base.BaseService;
import com.leecp.upms.dao.model.UpmsUserRole;
import com.leecp.upms.dao.model.UpmsUserRoleExample;

public interface UpmsUserRoleService extends BaseService<UpmsUserRole,UpmsUserRoleExample>{
	int distributeRole(Integer userId,String [] roleId);
}
