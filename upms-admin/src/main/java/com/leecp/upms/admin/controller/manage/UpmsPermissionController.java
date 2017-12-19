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

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.leecp.upms.admin.validator.NumberValidator;
import com.leecp.upms.admin.validator.StringValidator;
import com.leecp.upms.api.UpmsPermissionService;
import com.leecp.upms.api.UpmsSystemService;
import com.leecp.upms.common.base.BaseController;
import com.leecp.upms.common.base.BaseResult;
import com.leecp.upms.common.constant.ResultConstant;
import com.leecp.upms.dao.model.UpmsPermission;
import com.leecp.upms.dao.model.UpmsPermissionExample;
import com.leecp.upms.dao.model.UpmsSystem;
import com.leecp.upms.dao.model.UpmsSystemExample;

/**
 * 权限控制器
 * CRUD
 * @author LeeCP
 *
 */
@Controller
@RequestMapping("/manage/permission")
public class UpmsPermissionController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(UpmsPermissionController.class);
	@Autowired
	private UpmsPermissionService upmsPermissionService;
	
	@Autowired
	private UpmsSystemService upmsSystemService;
	/**
	 * 返回查看权限界面
	 * @return
	 */
	@RequiresPermissions("upms:permission:read")
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(){
		return "/manage/permission/index";
	}
	
	@RequiresPermissions("upms:permission:read")
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody
	public Object list(@RequestParam(required=true,defaultValue="0",value="offset") int offset,@RequestParam(required=true,defaultValue="5",value="limit") int limit,@RequestParam(required=false,defaultValue="0",value="systemId") int systemId){
		UpmsPermissionExample upmsPermissionExample = new UpmsPermissionExample();
		upmsPermissionExample.createCriteria().andStatusEqualTo((byte)1);
		if(systemId != 0){
			upmsPermissionExample.createCriteria().andSystemIdEqualTo(systemId);
		}
		List<UpmsPermission> upmsPermissions = upmsPermissionService.selectByExampleForOffsetPage(upmsPermissionExample,offset,limit);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total", upmsPermissionService.countByExample(upmsPermissionExample));
		result.put("rows", upmsPermissions);
		return result;
	}
		
	
	
	/**
	 * 返回创建页面
	 * @return
	 */
	@RequestMapping(value="/create",method=RequestMethod.GET)
	@RequiresPermissions("upms:permission:create")
	public String create(Model model){
		UpmsSystemExample example = new UpmsSystemExample();
		List<UpmsSystem> upmsSystems = upmsSystemService.selectByExample(example);
		model.addAttribute("upmsSystems", upmsSystems);
		return "/manage/permission/create";
	}
	
	@RequiresPermissions("upms:permission:create")
	@RequestMapping(value="/create",method=RequestMethod.POST)
	@ResponseBody
	public Object doCreate(UpmsPermission upmsPermission){
		/*
		 * 1. validate data
		 * 2. insert into db
		 * 3. return result
		 */
		ComplexResult result = FluentValidator.checkAll()
				.on(upmsPermission.getType(),new NumberValidator("类别"))
				.on(upmsPermission.getSystemId(),new NumberValidator("所属系统"))
				.on(upmsPermission.getPid(),new NumberValidator("所属上级")).when(upmsPermission.getType() == 2 || upmsPermission.getType() == 3)
				.on(upmsPermission.getLabel(),new StringValidator("标签名"))
				.on(upmsPermission.getDescription(),new StringValidator("描述"))
				.on(upmsPermission.getName(),new StringValidator("资源标识")).when(upmsPermission.getType() == 3)
				.on(upmsPermission.getUri(),new StringValidator("路径")).when(upmsPermission.getType() == 2 || upmsPermission.getType() == 3)
				.on(upmsPermission.getIcon(),new StringValidator("图标")).when(upmsPermission.getType() == 1 || upmsPermission.getType() == 3)
				.on(upmsPermission.getStatus(),new NumberValidator("状态"))
				.doValidate().result(ResultCollectors.toComplex());
		//验证不成功，返回错误信息
		if(!result.isSuccess()){
			return new BaseResult(ResultConstant.Fail,result.getErrors());
		}
		if(upmsPermissionService.insertSelective(upmsPermission) > 0){ //创建成功
			log.debug("upmsPermissionController->insertSelective：成功");
			return new BaseResult(ResultConstant.SUCCESS,1);
		}
		//创建失败
		log.error("upmsPermissionController->insertSelective：失败");
		return new BaseResult(ResultConstant.Fail,"未知错误");
	}
	
	
	/**
	 * 更新权限
	 */
	
	@RequiresPermissions("upms:permission:update")
	@RequestMapping(value="/update/{permissionId}",method=RequestMethod.GET)
	public String update(@PathVariable("permissionId") int permissionId,Model model){
		UpmsPermission upmsPermission = upmsPermissionService.selectByPrimaryKey(permissionId);
		model.addAttribute("permission",upmsPermission);
		UpmsSystemExample example = new UpmsSystemExample();
		List<UpmsSystem> upmsSystems = upmsSystemService.selectByExample(example);
		model.addAttribute("upmsSystems", upmsSystems);
		return "/manage/permission/update";
	}
	
	@RequiresPermissions("upms:permission:update")
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public Object doUpdate(UpmsPermission upmsPermission){
		/*
		 * 1. validate data
		 * 2. insert into db
		 * 3. return result
		 */
		ComplexResult result = FluentValidator.checkAll()
				.on(upmsPermission.getPermissionId(),new NumberValidator("编号"))
				.on(upmsPermission.getType(),new NumberValidator("类别"))
				.on(upmsPermission.getSystemId(),new NumberValidator("所属系统"))
				.on(upmsPermission.getPid(),new NumberValidator("所属上级")).when(upmsPermission.getType() == 2 || upmsPermission.getType() == 3)
				.on(upmsPermission.getLabel(),new StringValidator("标签名"))
				.on(upmsPermission.getDescription(),new StringValidator("描述"))
				.on(upmsPermission.getName(),new StringValidator("资源标识")).when(upmsPermission.getType() == 3)
				.on(upmsPermission.getUri(),new StringValidator("路径")).when(upmsPermission.getType() == 2 || upmsPermission.getType() == 3)
				.on(upmsPermission.getIcon(),new StringValidator("图标")).when(upmsPermission.getType() == 1 || upmsPermission.getType() == 3)
				.on(upmsPermission.getStatus(),new NumberValidator("状态"))
				.doValidate().result(ResultCollectors.toComplex());
		//验证不成功，返回错误信息
		if(!result.isSuccess()){
			return new BaseResult(ResultConstant.Fail,result.getErrors());
		}
		if(upmsPermissionService.updateByPrimaryKeySelective(upmsPermission) > 0){ //创建成功
			log.debug("upmsPermissionController->updateByPrimaryKeySelective：成功");
			return new BaseResult(ResultConstant.SUCCESS,1);
		}
		//创建失败
		log.error("upmsPermissionController->updateByPrimaryKeySelective：失败");
		return new BaseResult(ResultConstant.Fail,"未知错误");
	}
	
	/**
	 * 权限删除
	 * @param deleteIds 权限的Id集合
	 * @return
	 */
	@RequestMapping(value="/delete/{deleteIds}",method=RequestMethod.GET)
	@RequiresPermissions("upms:permission:delete")
	@ResponseBody
	public Object delete(@PathVariable("deleteIds") String deleteIds){
		if(StringUtils.isBlank(deleteIds)){
			return new BaseResult(ResultConstant.Fail,"删除权限Id不能为空！");
		}
		int result = upmsPermissionService.deleteByPrimaryKeys(deleteIds);
		return new BaseResult(ResultConstant.SUCCESS,result);
	}
}
