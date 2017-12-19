//package com.leecp.upms.dao.mapper;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.leecp.upms.dao.model.UpmsUser;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"classpath:applicationContext-*.xml"})
//public class UpmsUserTest {
//	@Autowired
//	private UpmsUserMapper upmsUserMapper;
//	@Test
//	public void testSelectUserById(){
//		UpmsUser user = upmsUserMapper.selectByPrimaryKey(1);
//		System.out.println(user.getUserId());
//	}
//	public static void main(String [] args){
//		UpmsUserTest user = new UpmsUserTest();
//		user.testSelectUserById();
//	}
//}
