package com.leecp.upms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.leecp.upms.api.UpmsApiService;
import com.leecp.upms.api.UpmsUserPermissionService;
import com.leecp.upms.api.UpmsUserService;
import com.leecp.upms.dao.model.UpmsPermission;
import com.leecp.upms.dao.model.UpmsUser;
import com.leecp.upms.dao.model.UpmsUserPermission;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext-dubbo-consumer.xml"})
public class UpmsApiServiceImplTest {
	@Autowired
	private UpmsApiService upmsApiService;
	
	@Autowired
	private UpmsUserService upmsUserService;
	
	@Autowired
	private UpmsUserPermissionService upmsUserPermissionService;
	
	private void printResult(String content){
		System.out.println("========================RESULT OF OPERATOR========================");
		System.out.println(content);
		System.out.println("========================RESULT OF OPERATOR========================");
	}
	@Test
	public void testSelectPermissionByUserId()throws Exception{
		CountDownLatch countDownLatch = new CountDownLatch(1);
		List<UpmsPermission> permissions = upmsApiService.selectUpmsPermissionByUpmsUserId(1);
		if(null == permissions || permissions.size() == 0) return ;
		System.out.println("the reslut is-------------------------------------------------------------");
		for(UpmsPermission permission : permissions){
			System.out.println(permission.toString());
		}
		System.out.println("the reslut is-------------------------------------------------------------");
		countDownLatch.await();
	}
	
	@Test
	public void testSelectUserByUserId(){
		UpmsUser user = upmsUserService.selectByPrimaryKey(1);
		if(null == user) return;
		printResult(user.toString());
	}
	
	@Test
	public void testTransaction(){
		List<UpmsUserPermission> upmsUsers = new ArrayList<UpmsUserPermission>();
		for(int i=0;i<10;i++){
			UpmsUserPermission upmsUserPermission = new UpmsUserPermission();
			upmsUserPermission.setUserId(i);
			upmsUserPermission.setPermissionId(i);
			upmsUserPermission.setType((byte)1);
			upmsUsers.add(upmsUserPermission);
		}
		upmsUserPermissionService.insertTen(upmsUsers);
	}
	
	
}
