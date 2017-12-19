package com.leecp.upms.api;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.leecp.upms.common.base.BaseService;
import com.leecp.upms.dao.model.UpmsUserPermission;
import com.leecp.upms.dao.model.UpmsUserPermissionExample;

public interface UpmsUserPermissionService extends BaseService<UpmsUserPermission,UpmsUserPermissionExample> {
	/**
	 * 对用户增减权限
	 * @param data 权限Id及增减类型。type 1：表示增，-1表示减；checked true表示添加， false表示删除
	 * @param userId 用户Id
	 * @return 操作数量
	 */
	int distribute(JSONArray data,Integer userId);
	
	int insertTen(List<UpmsUserPermission> users);
}
