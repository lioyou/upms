package com.leecp.upms.client.shiro.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.leecp.upms.api.UpmsApiService;
import com.leecp.upms.common.util.MD5Util;
import com.leecp.upms.dao.model.UpmsPermission;
import com.leecp.upms.dao.model.UpmsRole;
import com.leecp.upms.dao.model.UpmsUser;

/**
 * 使用数据库数据进行认证及授权
 * @author LeeCP
 *
 */
public class UpmsRealm extends AuthorizingRealm {
	private static final Logger log =  LoggerFactory.getLogger(UpmsRealm.class); 
	
	@Autowired
	private UpmsApiService upmsApiService;
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		log.info("进入授权（权限验证）阶段");
		String username = (String)principals.getPrimaryPrincipal();
		UpmsUser upmsUser = upmsApiService.selectUpmsUserByUsername(username);
		
		//当前用户的所有角色
		List<UpmsRole> upmsRoles = upmsApiService.selectUpmsRoleByUpmsUserId(upmsUser.getUserId());
		Set<String> roles = new HashSet<String>();
		if(null !=upmsRoles && upmsRoles.size() != 0){
			//取出所有角色名
			for(UpmsRole upmsRole : upmsRoles) {
				if(StringUtils.isNotBlank(upmsRole.getName())) {
					roles.add(upmsRole.getName());
				}
			}
		}
		
		//查询当前用户所有权限
		List<UpmsPermission> upmsPermissions = upmsApiService.selectUpmsPermissionByUpmsUserId(upmsUser.getUserId());
		Set<String> permissions = new HashSet<String>();
		if(null != upmsPermissions && upmsPermissions.size() != 0){
	        for (UpmsPermission upmsPermission : upmsPermissions) {
	            if (StringUtils.isNotBlank(upmsPermission.getName())) {
	                permissions.add(upmsPermission.getName());
	            }
	        }
		}
		

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
 	}
	
	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		log.info("进入认证（登录验证）阶段");
		String username = (String)token.getPrincipal();
		String password = new String((char[])token.getCredentials());
		//查询用户信息
		UpmsUser upmsUser = upmsApiService.selectUpmsUserByUsername(username);
		if (null == upmsUser) {
            throw new UnknownAccountException();
        }
        if (!upmsUser.getPassword().equals(MD5Util.MD5(password + upmsUser.getSalt()))) {
            throw new IncorrectCredentialsException();
        }
        if (upmsUser.getStatus() == 0) {
            throw new LockedAccountException();
        }

        return new SimpleAuthenticationInfo(username, password, getName());
	}

}
