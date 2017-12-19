package com.leecp.upms.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leecp.upms.api.UpmsUserOrganizationService;
import com.leecp.upms.common.base.BaseServiceImpl;
import com.leecp.upms.dao.mapper.UpmsUserOrganizationMapper;
import com.leecp.upms.dao.model.UpmsUserOrganization;
import com.leecp.upms.dao.model.UpmsUserOrganizationExample;
@Service
@Transactional
public class UpmsUserOrganizationServiceImpl extends BaseServiceImpl<UpmsUserOrganizationMapper,UpmsUserOrganization,UpmsUserOrganizationExample> implements UpmsUserOrganizationService{

}
