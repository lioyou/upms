package com.leecp.upms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leecp.upms.api.UpmsUserService;
import com.leecp.upms.common.base.BaseServiceImpl;
import com.leecp.upms.dao.mapper.UpmsUserMapper;
import com.leecp.upms.dao.model.UpmsUser;
import com.leecp.upms.dao.model.UpmsUserExample;
@Service
@Transactional
public class UpmsUserServiceImpl extends BaseServiceImpl<UpmsUserMapper,UpmsUser,UpmsUserExample>implements UpmsUserService {
	private static final Logger log = LoggerFactory.getLogger(UpmsUserServiceImpl.class);
	@Autowired
	private UpmsUserMapper upmsUserMapper;
	
}
