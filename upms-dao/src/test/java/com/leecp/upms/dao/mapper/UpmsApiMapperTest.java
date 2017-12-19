//package com.leecp.upms.dao.mapper;
//
//import java.util.List;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.leecp.upms.dao.model.UpmsPermission;
//import com.leecp.upms.dao.model.UpmsRole;
//
///**
// * 根据用户Id查询权限与角色 测试
// * @author LeeCP
// *
// */
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"classpath:applicationContext-*.xml"})
//public class UpmsApiMapperTest {
//	@Autowired
//	private UpmsApiMapper upmsApiMapper;
//	/**
//	 * 通过用户Id查询权限
//	 */
//	@Test
//	public void testSelectPermissionByUserId(){
//		List<UpmsPermission> permissions = upmsApiMapper.selectUpmsPermissionByUpmsUserId(1);
//		for(UpmsPermission upmsPermission : permissions){
//			System.out.println(upmsPermission.toString());
//		}
//	}
//	
//	
//	@Test
//	public void testSelectRoleByUserId(){
//		List<UpmsRole> roles = upmsApiMapper.selectUpmsRoleByUpmsUserId(1);
//		for(UpmsRole role : roles){
//			System.out.println(role.toString());
//		}
//	}
//	
//	
//	@Test
//	public void testSelectPermissionByRoleId(){
//		List<UpmsPermission> permissions = upmsApiMapper.selectUpmsPermissionByUpmsUserId(2);
//		System.out.println("the result is ========================================");
//		for(UpmsPermission upmsPermission : permissions){
//			System.out.println(upmsPermission.toString());
//		}
//		System.out.println("the result is ========================================");
//	}
//	
//}
