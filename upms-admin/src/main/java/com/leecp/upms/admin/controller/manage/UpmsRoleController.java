package com.leecp.upms.admin.controller.manage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import com.alibaba.fastjson.JSONArray;
import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.leecp.upms.admin.validator.NumberValidator;
import com.leecp.upms.admin.validator.StringValidator;
import com.leecp.upms.api.UpmsPermissionService;
import com.leecp.upms.api.UpmsRolePermissionService;
import com.leecp.upms.api.UpmsRoleService;
import com.leecp.upms.common.base.BaseController;
import com.leecp.upms.common.base.BaseResult;
import com.leecp.upms.common.constant.ResultConstant;
import com.leecp.upms.dao.model.UpmsRole;
import com.leecp.upms.dao.model.UpmsRoleExample;

/**
 * 角色控制类
 * @author LeeCP
 *
 */
@Controller
@RequestMapping("/manage/role")
public class UpmsRoleController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(UpmsRoleController.class);
	@Autowired
	private UpmsRoleService upmsRoleService;
	@Autowired
	private UpmsRolePermissionService upmsRolePermissionService;
	
	@Autowired
	private UpmsPermissionService upmsPermissionService;
	/**
	 * 返回查看角色界面
	 * @return
	 */
	@RequiresPermissions("upms:role:read")
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(){
		return "/manage/role/index";
	}
	
	@RequiresPermissions("upms:role:read")
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody
	public Object list(@RequestParam(required=true,defaultValue="0",value="offset")int offset,@RequestParam(required=true,defaultValue="5",value="limit")int limit){
		UpmsRoleExample upmsRoleExample = new UpmsRoleExample();
		upmsRoleExample.createCriteria().andStatusEqualTo((byte)1);
		List<UpmsRole> upmsRoles = upmsRoleService.selectByExample(upmsRoleExample);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total", upmsRoleService.countByExample(upmsRoleExample));
		result.put("rows",upmsRoles);
		return result;
	}
	
	/**
	 * 角色创建
	 */
	@RequestMapping(value="/create",method=RequestMethod.GET)
	@RequiresPermissions("upms:role:create")
	public String create(){
		return "/manage/role/create";
	}
	
	@RequestMapping(value="/create",method=RequestMethod.POST)
	@RequiresPermissions("upms:role:create")
	@ResponseBody
	public Object doCreate(UpmsRole upmsRole){
		/*
		 * 1. 检查参数
		 * 2. 插入
		 * 3. 返回结果
		 */
		//验证参数信息
		ComplexResult result = FluentValidator.checkAll()
		.on(upmsRole.getName(),new StringValidator("角色名"))
		.on(upmsRole.getLabel(),new StringValidator("标签名"))
		.on(upmsRole.getDescription(),new StringValidator("描述"))
		.doValidate().result(ResultCollectors.toComplex());
		//验证不成功，返回错误信息
		if(!result.isSuccess()){
			return new BaseResult(ResultConstant.Fail,result.getErrors());
		}
		if(upmsRoleService.insertSelective(upmsRole) > 0){ //创建成功
			log.debug("upmsRoleController->insertSelective：成功");
			return new BaseResult(ResultConstant.SUCCESS,1);
		}
		//创建失败
		log.error("upmsRoleController->insertSelective：失败");
		return new BaseResult(ResultConstant.Fail,"未知错误");
	}
	
	
	/**
	 * 更新角色
	 */
	@RequestMapping(value="/update/{roleId}",method=RequestMethod.GET)
	@RequiresPermissions("upms:role:update")
	public String update(@PathVariable("roleId") int roleId,Model model){		
		UpmsRole upmsRole = upmsRoleService.selectByPrimaryKey(roleId);
		model.addAttribute("role",upmsRole);
		return "/manage/role/update";
	}
	
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@RequiresPermissions("upms:role:update")
	@ResponseBody
	public Object doUpdate(UpmsRole upmsRole){
		/*
		 * 1. 检查参数
		 * 2. 插入
		 * 3. 返回结果
		 */
		//验证参数信息
		ComplexResult result = FluentValidator.checkAll()
		.on(upmsRole.getRoleId(),new NumberValidator("编号"))
		.on(upmsRole.getName(),new StringValidator("角色名"))
		.on(upmsRole.getLabel(),new StringValidator("标签名"))
		.on(upmsRole.getDescription(),new StringValidator("描述"))
		.doValidate().result(ResultCollectors.toComplex());
		//验证不成功，返回错误信息
		if(!result.isSuccess()){
			return new BaseResult(ResultConstant.Fail,result.getErrors());
		}
		if(upmsRoleService.updateByPrimaryKeySelective(upmsRole) > 0){ //创建成功
			log.debug("upmsRoleController->updateByPrimaryKeySelective：成功");
			return new BaseResult(ResultConstant.SUCCESS,1);
		}
		//创建失败
		log.error("upmsRoleController->updateByPrimaryKeySelective：失败");
		return new BaseResult(ResultConstant.Fail,"未知错误");
	}
	
	
	/**
	 * 返回权限页面
	 * @return
	 */
	@RequiresPermissions("upms:role:permission")
	@RequestMapping(value="/permission",method=RequestMethod.GET)
	public String permission(){
		return "/manage/role/permission";
	}
	
	/**
	 * 返回权限树
	 * @param roleId
	 * @return
	 */
	@RequiresPermissions("upms:role:permission")
	@RequestMapping(value="/permission/{roleId}",method=RequestMethod.GET)
	@ResponseBody
	public JSONArray permissionTree(@PathVariable("roleId") int roleId){
		if(roleId < 0) return null;
		return upmsPermissionService.selectUpmsPermissionTreeByUpmsRoleId(roleId);
	}
	/**
	 * 处理权限
	 * @param roleId 被分配权限的角色Id
	 * @param rolePermissions
	 * @return
	 */
	@RequiresPermissions("upms:role:permission")
	@RequestMapping(value="/permission",method=RequestMethod.POST)
	@ResponseBody
	public Object doPermission(@RequestParam("roleId") int roleId,@RequestParam("rolePermissions") String rolePermissions){
		if(null == rolePermissions || "".equalsIgnoreCase(rolePermissions)) return new BaseResult(ResultConstant.Fail,"非法参数");
		int result = upmsRolePermissionService.distributePermission(roleId, rolePermissions);
		return new BaseResult(ResultConstant.SUCCESS,result);
	}
	
	/**
	 * 角色删除
	 * @param deleteIds 角色的Id集合
	 * @return
	 */
	@RequestMapping(value="/delete/{deleteIds}",method=RequestMethod.GET)
	@RequiresPermissions("upms:role:delete")
	@ResponseBody
	public Object delete(@PathVariable("deleteIds") String deleteIds){
		if(StringUtils.isBlank(deleteIds)){
			return new BaseResult(ResultConstant.Fail,"删除用户Id不能为空！");
		}
		int result = upmsRoleService.deleteByPrimaryKeys(deleteIds);
		return new BaseResult(ResultConstant.SUCCESS,result);
	}
}
