package com.leecp.upms.admin.controller.manage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.leecp.upms.admin.validator.EmailValidator;
import com.leecp.upms.admin.validator.NumberValidator;
import com.leecp.upms.admin.validator.StringValidator;
import com.leecp.upms.api.UpmsOrganizationService;
import com.leecp.upms.api.UpmsPermissionService;
import com.leecp.upms.api.UpmsRoleService;
import com.leecp.upms.api.UpmsUserOrganizationService;
import com.leecp.upms.api.UpmsUserPermissionService;
import com.leecp.upms.api.UpmsUserRoleService;
import com.leecp.upms.api.UpmsUserService;
import com.leecp.upms.common.base.BaseController;
import com.leecp.upms.common.base.BaseResult;
import com.leecp.upms.common.constant.ResultConstant;
import com.leecp.upms.common.util.MD5Util;
import com.leecp.upms.dao.model.UpmsOrganization;
import com.leecp.upms.dao.model.UpmsOrganizationExample;
import com.leecp.upms.dao.model.UpmsRole;
import com.leecp.upms.dao.model.UpmsRoleExample;
import com.leecp.upms.dao.model.UpmsUser;
import com.leecp.upms.dao.model.UpmsUserExample;
import com.leecp.upms.dao.model.UpmsUserOrganization;
import com.leecp.upms.dao.model.UpmsUserOrganizationExample;
import com.leecp.upms.dao.model.UpmsUserRole;
import com.leecp.upms.dao.model.UpmsUserRoleExample;

/**
 * 用户处理控制器
 * @author LeeCP
 *
 */
@Controller
@RequestMapping("/manage/user")
public class UpmsUserController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(UpmsUserController.class);
	
	@Autowired
	private UpmsUserService upmsUserService;
	
	@Autowired
	private UpmsRoleService upmsRoleService;
	
	@Autowired
	private UpmsUserRoleService upmsUserRoleService;
	
	@Autowired 
	private UpmsPermissionService upmsPermissionService;
	
	@Autowired
	private UpmsUserPermissionService upmsUserPermissionService; 
	
	@Autowired
	private UpmsUserOrganizationService upmsUserOrganizationService;
	
	@Autowired
	private UpmsOrganizationService upmsOrganizationService;
	
	/**
	 * 返回用户列表
	 */
	@RequiresPermissions("upms:user:read")
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(){
		return "/manage/user/index";
	}
	
	@RequiresPermissions("upms:user:read")
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody
	public Object list(@RequestParam(required=false,defaultValue="0",value="offset") int offset,@RequestParam(required=false,defaultValue="5",value="limit") int limit){
		UpmsUserExample upmsUserExample = new UpmsUserExample();
		upmsUserExample.createCriteria().andStatusEqualTo((byte)1);
		List<UpmsUser> upmsUsers = upmsUserService.selectByExampleForOffsetPage(upmsUserExample, offset, limit);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total", upmsUserService.countByExample(upmsUserExample));
		result.put("rows",upmsUsers);
		return result;
	}
	
	/**
	 * 返回用户创建页面
	 */
	@RequiresPermissions("upms:user:create")
	@RequestMapping(value="/create",method=RequestMethod.GET)
	public String create(){
		return "/manage/user/create";
	}
	
	/**
	 * 处理用户创建请求
	 * @param upmsUser 用户模型数据
	 * @param imagePrefix 图片名称的前缀  
	 * @return 创建结果
	 */
	@RequiresPermissions("upms:user:create")
	@RequestMapping(value="/create",method=RequestMethod.POST)
	@ResponseBody
	public Object doCreate(UpmsUser upmsUser,@RequestParam(value="imagePrefix") String imagePrefix){
		//验证参数信息
		ComplexResult result = FluentValidator.checkAll()
		.on(upmsUser.getUsername(),new StringValidator("用户名",true,6))
		.on(upmsUser.getPassword(),new StringValidator("密码",true,6))
		.on(upmsUser.getRealname(),new StringValidator("真实姓名"))
		.on(upmsUser.getAvatar(),new StringValidator("头像"))
		.on(upmsUser.getPhone(),new StringValidator("电话",true,11))
		.on(upmsUser.getEmail(),new EmailValidator("邮箱"))
		.on(upmsUser.getSex(),new NumberValidator("性别"))
		.on(upmsUser.getStatus(),new NumberValidator("状态"))
		.doValidate().result(ResultCollectors.toComplex());
		//验证不成功，返回错误信息
		if(!result.isSuccess()){
			return new BaseResult(ResultConstant.Fail,result.getErrors());
		}
		/*
		 * 1.加密密码 password + salt
		 * 2.拼接图片路径
		 * 3.Id为唯一性，所以不查重
		 */
		String salt = UUID.randomUUID().toString().replaceAll("-", "");
		upmsUser.setSalt(salt);
		upmsUser.setPassword(MD5Util.MD5(upmsUser.getPassword() + salt));
		//拼接Image路径
		upmsUser.setAvatar(imagePrefix + "-" + upmsUser.getAvatar());
		if(upmsUserService.insertSelective(upmsUser) > 0){ //创建成功
			log.info("insertSelective：成功");
			return new BaseResult(ResultConstant.SUCCESS,1);
		}
		//创建失败
		log.error("insertSelective：失败");
		return new BaseResult(ResultConstant.Fail,"未知错误");
	}
	
	/**
	 * 返回用户更新页面
	 * @param userId 用户Id
	 * @param model 存储数据的model
	 * @return 返回界面
	 */
	@RequiresPermissions("upms:user:update")
	@RequestMapping(value="/update/{userId}",method=RequestMethod.GET)
	public String update(@PathVariable("userId") int userId,Model model){
		UpmsUser upmsUser = upmsUserService.selectByPrimaryKey(userId);
		//获取分割符位置
		int index = upmsUser.getAvatar().indexOf("-");
		//返回名称前缀
		String imagePrefix = upmsUser.getAvatar().substring(0,index);
		//图片原始名称
		upmsUser.setAvatar(upmsUser.getAvatar().substring(index+1));
		model.addAttribute("user", upmsUser);
		model.addAttribute("imagePrefix", imagePrefix);
		return "/manage/user/update";
	}
	
	/**
	 * 处理用户更新操作
	 * @param upmsUser 用户模型对象
	 * @param imagePrefix 图片名称前缀
	 * @return 返回操作结果
	 */
	@RequiresPermissions("upms:user:update")
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public Object doUpdate(UpmsUser upmsUser,@RequestParam("imagePrefix") String imagePrefix){
		/*
		 * 1. 数据验证
		 *     拼接图片的名称，imagePrefix默认就不可以为空，所以不用再重复验证
		 * 2. 执行更新
		 * 3. 返回结果
		 */
		//验证参数信息
		ComplexResult complexResult = FluentValidator.checkAll()
		.on(upmsUser.getUserId(),new NumberValidator("编号"))
		.on(upmsUser.getUsername(),new StringValidator("用户名",true))
		.on(upmsUser.getRealname(),new StringValidator("真实姓名"))
		.on(upmsUser.getAvatar(),new StringValidator("头像"))
		.on(upmsUser.getPhone(),new StringValidator("电话",true,11))
		.on(upmsUser.getEmail(),new EmailValidator("邮箱"))
		.on(upmsUser.getSex(),new NumberValidator("性别"))
		.on(upmsUser.getStatus(),new NumberValidator("状态"))
		.doValidate().result(ResultCollectors.toComplex());
		//验证不成功，返回错误信息
		if(!complexResult.isSuccess()){
			return new BaseResult(ResultConstant.Fail,complexResult.getErrors());
		}
		//拼接图片名称
		upmsUser.setAvatar(imagePrefix + "-" + upmsUser.getAvatar());
		UpmsUserExample example = new UpmsUserExample();
		example.createCriteria().andUserIdEqualTo(upmsUser.getUserId());
		int result = 0;
		result = upmsUserService.updateByExampleSelective(upmsUser, example);
		return new BaseResult(ResultConstant.SUCCESS,result);
	}
	
	
	/**
	 * 用户删除
	 * @param deleteIds 用户的Id集合
	 * @return
	 */
	@RequiresPermissions("upms:user:delete")
	@RequestMapping(value="/delete/{deleteIds}",method=RequestMethod.GET)
	@ResponseBody
	public Object delete(@PathVariable("deleteIds") String deleteIds){
		if(StringUtils.isBlank(deleteIds)){
			return new BaseResult(ResultConstant.Fail,"删除用户Id不能为空！");
		}
		int result = upmsUserService.deleteByPrimaryKeys(deleteIds);
		return new BaseResult(ResultConstant.SUCCESS,result);
	}
	
	
	/*
	 * 目的：用户角色分配
	 * 1.返回界面.select option
	 * 2.处理提交的结果
	 * 3.调用服务写入库
	 * 
	 */
	/**
	 * 角色分配
	 * @param userId 用户Id
	 * @param model 返回给页面的数据
	 * @return 界面分配页面
	 */
	@RequiresPermissions("upms:user:role")
	@RequestMapping(value="/role/{userId}",method=RequestMethod.GET)
	public String role(@PathVariable("userId") int userId,Model model){
		UpmsRoleExample  upmsRoleExample = new UpmsRoleExample();
		List<UpmsRole> upmsRoles = upmsRoleService.selectByExample(upmsRoleExample);
		model.addAttribute("upmsRoles",upmsRoles);
		UpmsUserRoleExample upmsUserRoleExample = new UpmsUserRoleExample();
		upmsUserRoleExample.createCriteria().andUserIdEqualTo(userId);
		List<UpmsUserRole> upmsUserRoles = upmsUserRoleService.selectByExample(upmsUserRoleExample);
		model.addAttribute("upmsUserRoles", upmsUserRoles);
		model.addAttribute("userId", userId);
		return "/manage/user/role";
	}
	
	/**
	 * 处理角色分配
	 * @param userId 用户Id
	 * @param roleId 分配的角色Id
	 * @return 分配的个数
	 */
	@RequiresPermissions("upms:user:role")
	@RequestMapping(value="/role",method=RequestMethod.POST)
	@ResponseBody
	public Object doRole(@RequestParam("userId") int userId,@RequestParam(required=true,defaultValue="null",value="roleId") String[] roleId){
		if(null == roleId) return new BaseResult(ResultConstant.Fail,"请选择角色！");
		int result = upmsUserRoleService.distributeRole(userId, roleId);
		return new BaseResult(ResultConstant.SUCCESS, result);
	}
	
	/*
	 * 目的：用户权限增减
	 * 1.返回显示权限界面
	 * 2.返回权限数据
	 * 3.接收并处理用户选择
	 * 4.调用service写入库
	 */
	/**
	 * 返回权限分配页面
	 * @return
	 */
	@RequiresPermissions("upms:user:permission")
	@RequestMapping(value="/permission",method=RequestMethod.GET)
	public String permission(){
		return "/manage/user/permission";
	}
	
	/**
	 * 返回权限集合，非法Id，则返回null
	 * @param userId 用户Id
	 * @param type 权限的类型，1：表示增加，2：表示减少
	 * @return 返回权限集合
	 */
	@RequiresPermissions("upms:user:permission")
	@RequestMapping(value="/permission/{userId}",method=RequestMethod.GET)
	@ResponseBody
	public JSONArray permissionTree(@PathVariable("userId") int userId,@RequestParam(value="type") byte type){
		if(userId < 0) return null;
		if(type == 1 || type == -1){
			JSONArray tree = upmsPermissionService.selectUpmsPermissionTreeByUpmsUserId(userId,type);
			return tree;
		}
		return null;
	}
	/**
	 * 处理权限分配
	 * @param datas 权限集合
	 * @param userPermissionId 被分配权限的用户Id
	 * @return
	 */
	@RequiresPermissions("upms:user:permission")
	@RequestMapping(value="/permission",method=RequestMethod.POST)
	@ResponseBody
	public Object doPermission(@RequestParam(value="datas") String datas,@RequestParam(value="permissionUserId") int userPermissionId){
		/*
		 * 获取参数
		 * 调用业务
		 * 返回结果
		 */
		JSONArray data = JSONArray.parseArray(datas);
		int result = 0;
		result = upmsUserPermissionService.distribute(data,userPermissionId);
		return new BaseResult(ResultConstant.SUCCESS,result);
	}
	
	
	
	/**
	 * 返回组织页面
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequiresPermissions("upms:user:organization")
	@RequestMapping(value="/organization/{userId}",method=RequestMethod.GET)
	public String organization(@PathVariable("userId") int userId,Model model){
		UpmsUserOrganizationExample upmsUserOrganizationExample = new UpmsUserOrganizationExample();
		upmsUserOrganizationExample.createCriteria().andUserIdEqualTo(userId);
		List<UpmsUserOrganization> upmsUserOrganizations = upmsUserOrganizationService.selectByExample(upmsUserOrganizationExample);
		model.addAttribute("upmsUserOrganizations", upmsUserOrganizations);
		UpmsOrganizationExample upmsOrganizationExample = new UpmsOrganizationExample();
		List<UpmsOrganization> upmsOrganizations = upmsOrganizationService.selectByExample(upmsOrganizationExample);
		model.addAttribute("upmsOrganizations",upmsOrganizations);
		model.addAttribute("userId",userId);
		return "/manage/user/organization";
	}
	/**
	 * 处理组织分配
	 * @param userId
	 * @param organizationId
	 * @return
	 */
	@RequiresPermissions("upms:user:organization")
	@RequestMapping(value="/organization",method=RequestMethod.POST)
	@ResponseBody
	public Object doOrganization(@RequestParam("userId") int userId,@RequestParam(required=true,defaultValue="null",value="organizationId") String[] organizationId){
		int result = 0;
		result = upmsOrganizationService.distributeOrganization(userId, organizationId);
		return new BaseResult(ResultConstant.SUCCESS,1);
	}
}
