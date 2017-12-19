package com.leecp.upms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leecp.upms.api.UpmsSystemService;
import com.leecp.upms.common.base.BaseServiceImpl;
import com.leecp.upms.dao.mapper.UpmsSystemMapper;
import com.leecp.upms.dao.model.UpmsSystem;
import com.leecp.upms.dao.model.UpmsSystemExample;

/**
 * 系统操作服务
 * @author LeeCP
 *
 */
@Service
@Transactional
public class UpmsSystemServiceImpl extends BaseServiceImpl<UpmsSystemMapper,UpmsSystem,UpmsSystemExample> implements UpmsSystemService{
	private static final Logger log = LoggerFactory.getLogger(UpmsSystemServiceImpl.class);
	
}
