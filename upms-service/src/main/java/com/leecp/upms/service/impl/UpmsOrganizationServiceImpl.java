package com.leecp.upms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leecp.upms.api.UpmsOrganizationService;
import com.leecp.upms.common.base.BaseServiceImpl;
import com.leecp.upms.dao.mapper.UpmsOrganizationMapper;
import com.leecp.upms.dao.mapper.UpmsUserOrganizationMapper;
import com.leecp.upms.dao.model.UpmsOrganization;
import com.leecp.upms.dao.model.UpmsOrganizationExample;
import com.leecp.upms.dao.model.UpmsUserOrganization;
import com.leecp.upms.dao.model.UpmsUserOrganizationExample;
@Service
@Transactional
public class UpmsOrganizationServiceImpl extends BaseServiceImpl<UpmsOrganizationMapper,UpmsOrganization,UpmsOrganizationExample> implements UpmsOrganizationService {
	private static final Logger log = LoggerFactory.getLogger(UpmsOrganizationServiceImpl.class);
	@Autowired
	private UpmsUserOrganizationMapper upmsUserOrganizationMapper; 
	public int distributeOrganization(int userId,String[] organizationId) {
		if(null == organizationId) return 0;
		//删除全部旧记录
		int result =0;
		UpmsUserOrganizationExample upmsUserOrganizationExample = new UpmsUserOrganizationExample();
		upmsUserOrganizationExample.createCriteria().andUserIdEqualTo(userId);
		result = upmsUserOrganizationMapper.deleteByExample(upmsUserOrganizationExample);
		if(organizationId.length == 0) return result;
		//记录插入的结果
		result = 0;
		//插入新记录
		for(String id : organizationId){
			if("null".equalsIgnoreCase(id)) continue;
			log.info(id);
			UpmsUserOrganization userOrganization = new UpmsUserOrganization();
			userOrganization.setUserId(userId);
			userOrganization.setOrganizationId(Integer.parseInt(id));
			result += upmsUserOrganizationMapper.insertSelective(userOrganization);
		}
		return result ;
	}

}
