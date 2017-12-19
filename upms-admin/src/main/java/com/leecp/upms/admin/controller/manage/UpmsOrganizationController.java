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
import com.leecp.upms.api.UpmsOrganizationService;
import com.leecp.upms.common.base.BaseController;
import com.leecp.upms.common.base.BaseResult;
import com.leecp.upms.common.constant.ResultConstant;
import com.leecp.upms.dao.model.UpmsOrganization;
import com.leecp.upms.dao.model.UpmsOrganizationExample;

/**
 * 组织控制器
 * @author LeeCP
 *
 */
@Controller
@RequestMapping("/manage/organization")
public class UpmsOrganizationController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(UpmsOrganizationController.class);
	@Autowired
	private UpmsOrganizationService upmsOrganizationService;
	/*
	 * 组织查看操作
	 */
	@RequestMapping(value="/index",method=RequestMethod.GET)
	@RequiresPermissions("upms:organization:read")
	public String index(){
		return "/manage/organization/index";
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@RequiresPermissions("upms:organization:read")
	@ResponseBody
	public Object list(@RequestParam(required=false,defaultValue="0",value="offset") int offset,@RequestParam(required=false,defaultValue="5",value="limit") int limit){
		//查询全部组织
		UpmsOrganizationExample upmsOrganizationExample = new UpmsOrganizationExample();
		List<UpmsOrganization> upmsOrganizations = upmsOrganizationService.selectByExampleForOffsetPage(upmsOrganizationExample, offset, limit);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total", upmsOrganizationService.countByExample(upmsOrganizationExample));
		result.put("rows", upmsOrganizations);
		return result;
	}
	
	
	/**
	 * 返回组织创建页面
	 * @return
	 */
	@RequestMapping(value="/create",method=RequestMethod.GET)
	@RequiresPermissions("upms:organization:create")
	public String create(Model model){
		//查询全部组织
		UpmsOrganizationExample upmsOrganizationExample = new UpmsOrganizationExample();
		List<UpmsOrganization> upmsOrganizations = upmsOrganizationService.selectByExample(upmsOrganizationExample);
		model.addAttribute("upmsOrganizations", upmsOrganizations);
		return "/manage/organization/create";
	}
	
	@RequestMapping(value="/create",method=RequestMethod.POST)
	@RequiresPermissions("upms:organization:create")
	@ResponseBody
	public Object doCreate(UpmsOrganization upmsOrganization){
		/*
		 * 1. 检查参数
		 * 2. 插入
		 * 3. 返回结果
		 */
		//验证参数信息
		ComplexResult result = FluentValidator.checkAll()
		.on(upmsOrganization.getName(),new StringValidator("组织名称"))
		.on(upmsOrganization.getLabel(),new StringValidator("标签名"))
		.on(upmsOrganization.getDescription(),new StringValidator("描述"))
		.on(upmsOrganization.getPid(),new NumberValidator("所属上级"))
		.doValidate().result(ResultCollectors.toComplex());
		//验证不成功，返回错误信息
		if(!result.isSuccess()){
			return new BaseResult(ResultConstant.Fail,result.getErrors());
		}
		if(upmsOrganizationService.insertSelective(upmsOrganization) > 0){ //创建成功
			log.debug("upmsOrganizationController->insertSelective：成功");
			return new BaseResult(ResultConstant.SUCCESS,1);
		}
		//创建失败
		log.error("upmsOrganizationController->insertSelective：失败");
		return new BaseResult(ResultConstant.Fail,"未知错误");
		
	}
	
	/**
	 * 返回更新页面
	 * @param organizationId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/update/{organizationId}",method=RequestMethod.GET)
	@RequiresPermissions("upms:organization:update")
	public String update(@PathVariable("organizationId") int organizationId,Model model){
		UpmsOrganization upmsOrganization = upmsOrganizationService.selectByPrimaryKey(organizationId);
		//查询全部组织
		UpmsOrganizationExample upmsOrganizationExample = new UpmsOrganizationExample();
		List<UpmsOrganization> upmsOrganizations = upmsOrganizationService.selectByExample(upmsOrganizationExample);
		model.addAttribute("upmsOrganizations", upmsOrganizations);
		model.addAttribute("organization", upmsOrganization);
		return "/manage/organization/update";	
	}
	/**
	 * 处理更新操作
	 * @param organizationId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@RequiresPermissions("upms:organization:update")
	@ResponseBody
	public Object doUpdate(UpmsOrganization upmsOrganization){
		/*
		 * 1. 检查参数
		 * 2. 插入
		 * 3. 返回结果
		 */
		//验证参数信息
		ComplexResult result = FluentValidator.checkAll()
		.on(upmsOrganization.getOrganizationId(),new NumberValidator("编号"))
		.on(upmsOrganization.getName(),new StringValidator("组织名称"))
		.on(upmsOrganization.getLabel(),new StringValidator("标签名"))
		.on(upmsOrganization.getDescription(),new StringValidator("描述"))
		.on(upmsOrganization.getPid(),new NumberValidator("所属上级"))
		.doValidate().result(ResultCollectors.toComplex());
		//验证不成功，返回错误信息
		if(!result.isSuccess()){
			return new BaseResult(ResultConstant.Fail,result.getErrors());
		}
		if(upmsOrganizationService.updateByPrimaryKeySelective(upmsOrganization) > 0){ //创建成功
			log.debug("upmsOrganizationController->updateByPrimaryKeySelective：成功");
			return new BaseResult(ResultConstant.SUCCESS,1);
		}
		//创建失败
		log.error("upmsOrganizationController->updateByPrimaryKeySelective：失败");
		return new BaseResult(ResultConstant.Fail,"未知错误");
		
	}
	
	/**
	 * 用户删除
	 * @param deleteIds 用户的Id集合
	 * @return
	 */
	@RequestMapping(value="/delete/{deleteIds}",method=RequestMethod.GET)
	@RequiresPermissions("upms:organization:delete")
	@ResponseBody
	public Object delete(@PathVariable("deleteIds") String deleteIds){
		if(StringUtils.isBlank(deleteIds)){
			return new BaseResult(ResultConstant.Fail,"删除组织Id不能为空！");
		}
		int result = upmsOrganizationService.deleteByPrimaryKeys(deleteIds);
		return new BaseResult(ResultConstant.SUCCESS,result);
	}
}
