package com.leecp.upms.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leecp.upms.api.UpmsRoleService;
import com.leecp.upms.common.base.BaseServiceImpl;
import com.leecp.upms.dao.mapper.UpmsRoleMapper;
import com.leecp.upms.dao.model.UpmsRole;
import com.leecp.upms.dao.model.UpmsRoleExample;
@Service
@Transactional
public class UpmsRoleServiceImpl extends BaseServiceImpl<UpmsRoleMapper,UpmsRole,UpmsRoleExample> implements UpmsRoleService{

}
