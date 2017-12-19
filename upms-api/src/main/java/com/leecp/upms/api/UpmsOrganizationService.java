package com.leecp.upms.api;

import com.leecp.upms.common.base.BaseService;
import com.leecp.upms.dao.model.UpmsOrganization;
import com.leecp.upms.dao.model.UpmsOrganizationExample;

/**
 * 组织服务
 * @author LeeCP
 *
 */
public interface UpmsOrganizationService extends BaseService<UpmsOrganization,UpmsOrganizationExample> {
	/**
	 * 分配组织
	 * @param userId
	 * @param organizationId
	 * @return 执行结果
	 */
	int distributeOrganization(int userId,String[] organizationId);
}
