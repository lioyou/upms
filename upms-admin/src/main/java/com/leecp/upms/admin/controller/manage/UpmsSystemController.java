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
import com.leecp.upms.api.UpmsSystemService;
import com.leecp.upms.common.base.BaseController;
import com.leecp.upms.common.base.BaseResult;
import com.leecp.upms.common.constant.ResultConstant;
import com.leecp.upms.dao.model.UpmsSystem;
import com.leecp.upms.dao.model.UpmsSystemExample;

/**
 * 系统CRUD操作
 * 负责对系统操作的处理与结果反馈
 * @author LeeCP
 *
 */
@Controller
@RequestMapping("/manage/system")
public class UpmsSystemController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(UpmsSystemController.class);
	@Autowired 
	private UpmsSystemService upmsSystemService;
	

	
	/**
	 * 返回系统查看页面
	 * @return
	 */
	@RequestMapping(value="/index",method=RequestMethod.GET)
	@RequiresPermissions("upms:system:read")
	public String index(){
		return "/manage/system/index";
	}
	
	/**
	 * 获取系统数据
	 * @param offset 数据起始位置
	 * @param limit 获取数据数量
	 * @return 返回系统数据
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@RequiresPermissions("upms:system:read")
	@ResponseBody
	public Object list(@RequestParam(required=true,defaultValue="0",value="offset")int offset,@RequestParam(required=true,defaultValue="0",value="limit")int limit){
		UpmsSystemExample upmsSystemExample = new UpmsSystemExample();
		List<UpmsSystem> upmsSystems = upmsSystemService.selectByExampleForOffsetPage(upmsSystemExample, offset, limit);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("total", upmsSystems.size());
		result.put("rows", upmsSystems);
		return result;
	}
	
	/**
	 * 返回系统创建页面
	 * @return
	 */
	@RequestMapping(value="/create",method=RequestMethod.GET)
	@RequiresPermissions("upms:system:create")
	public String create(){
		return "/manage/system/create";
	}
	
	@RequestMapping(value="/create",method=RequestMethod.POST)
	@RequiresPermissions("upms:system:create")
	@ResponseBody
	public Object doCreate(UpmsSystem upmsSystem,@RequestParam("imagePrefix") String imagePrefix){
		/*
		 * 1. 检查参数
		 * 2. 插入
		 * 3. 返回结果
		 */
		//验证参数信息
		ComplexResult result = FluentValidator.checkAll()
		.on(upmsSystem.getName(),new StringValidator("系统名称",true))
		.on(upmsSystem.getTheme(),new StringValidator("主题"))
		.on(upmsSystem.getIcon(),new StringValidator("图标"))
		.on(upmsSystem.getLabel(),new StringValidator("标签名"))
		.on(upmsSystem.getBanner(),new StringValidator("背景图"))
		.on(upmsSystem.getDescription(),new StringValidator("描述"))
		.on(upmsSystem.getBaseurl(),new StringValidator("根目录"))
		.on(upmsSystem.getStatus(),new NumberValidator("状态"))
		.doValidate().result(ResultCollectors.toComplex());
		//验证不成功，返回错误信息
		if(!result.isSuccess()){
			return new BaseResult(ResultConstant.Fail,result.getErrors());
		}
		//拼接Image路径
		upmsSystem.setBanner(imagePrefix + "-" + upmsSystem.getBanner());
		if(upmsSystemService.insertSelective(upmsSystem) > 0){ //创建成功
			log.debug("UpmsSystemController->insertSelective：成功");
			return new BaseResult(ResultConstant.SUCCESS,1);
		}
		//创建失败
		log.error("UpmsSystemController->insertSelective：失败");
		return new BaseResult(ResultConstant.Fail,"未知错误");
	}
	
	/**
	 * 返回更新页面
	 * @param systemId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/update/{systemId}",method=RequestMethod.GET)
	@RequiresPermissions("upms:system:update")
	public String update(@PathVariable("systemId") int systemId,Model model){
		UpmsSystem upmsSystem = upmsSystemService.selectByPrimaryKey(systemId);
		//获取分割符位置
		int index = upmsSystem.getBanner().indexOf("-");
		//返回名称前缀
		String imagePrefix = upmsSystem.getBanner().substring(0,index);
		//图片原始名称
		upmsSystem.setBanner(upmsSystem.getBanner().substring(index+1));
		model.addAttribute("imagePrefix",imagePrefix);
		model.addAttribute("system", upmsSystem);
		return "/manage/system/update";	
	}
	/**
	 * 处理更新操作
	 * @param systemId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@RequiresPermissions("upms:system:update")
	@ResponseBody
	public Object doUpdate(UpmsSystem upmsSystem,@RequestParam("imagePrefix") String imagePrefix){
		//验证参数信息
				ComplexResult result = FluentValidator.checkAll()
				.on(upmsSystem.getSystemId(),new NumberValidator("编号"))
				.on(upmsSystem.getName(),new StringValidator("系统名称",true))
				.on(upmsSystem.getTheme(),new StringValidator("主题"))
				.on(upmsSystem.getIcon(),new StringValidator("图标"))
				.on(upmsSystem.getLabel(),new StringValidator("标签名"))
				.on(upmsSystem.getBanner(),new StringValidator("背景图"))
				.on(upmsSystem.getDescription(),new StringValidator("描述"))
				.on(upmsSystem.getBaseurl(),new StringValidator("根目录"))
				.on(upmsSystem.getStatus(),new NumberValidator("状态"))
				.doValidate().result(ResultCollectors.toComplex());
				//验证不成功，返回错误信息
				if(!result.isSuccess()){
					return new BaseResult(ResultConstant.Fail,result.getErrors());
				}
				//拼接Image路径
				upmsSystem.setBanner(imagePrefix + "-" + upmsSystem.getBanner());
				if(upmsSystemService.updateByPrimaryKeySelective(upmsSystem) > 0){ //更新成功
					log.debug("UpmsSystemController->updateByPrimaryKeySelective：成功");
					return new BaseResult(ResultConstant.SUCCESS,1);
				}
				//创建失败
				log.error("UpmsSystemController->updateByPrimaryKeySelective：失败");
				return new BaseResult(ResultConstant.Fail,"未知错误");
	}
	
	/**
	 * 用户删除
	 * @param deleteIds 用户的Id集合
	 * @return
	 */
	@RequiresPermissions("upms:system:delete")
	@RequestMapping(value="/delete/{deleteIds}",method=RequestMethod.GET)
	@ResponseBody
	public Object delete(@PathVariable("deleteIds") String deleteIds){
		if(StringUtils.isBlank(deleteIds)){
			return new BaseResult(ResultConstant.Fail,"删除用户Id不能为空！");
		}
		int result = upmsSystemService.deleteByPrimaryKeys(deleteIds);
		return new BaseResult(ResultConstant.SUCCESS,result);
	}
	
}
