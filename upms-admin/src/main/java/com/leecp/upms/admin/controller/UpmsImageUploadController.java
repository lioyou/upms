package com.leecp.upms.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.leecp.upms.admin.util.FileUploadUtil;
import com.leecp.upms.common.base.BaseController;
import com.leecp.upms.common.base.BaseResult;
import com.leecp.upms.common.constant.ResultConstant;

/**
 * 图片上传处理类
 * @author LeeCP
 *
 */
@Controller
@RequestMapping("/manage/image")
public class UpmsImageUploadController extends BaseController{
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	@ResponseBody
	public Object doUpload(@RequestParam(required=true,defaultValue="",value="file") MultipartFile file){
		String relativePath = FileUploadUtil.upload(file);
		//返回为null即上传失败
		if(null == relativePath){
			return new BaseResult(ResultConstant.Fail,"文件上传失败");
		}
		//上传成功，返回相对路径
		return new BaseResult(ResultConstant.SUCCESS,relativePath);
	}
}
