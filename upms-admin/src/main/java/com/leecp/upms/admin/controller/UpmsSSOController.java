package com.leecp.upms.admin.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.leecp.upms.common.base.BaseConstant;
import com.leecp.upms.common.base.BaseController;
import com.leecp.upms.common.base.BaseResult;
import com.leecp.upms.common.constant.ResultConstant;
import com.leecp.upms.common.util.RedisUtil;

/**
 * 单点登录控制器
 * @author LeeCP
 *
 */
@Controller
@RequestMapping(value="/sso")
public class UpmsSSOController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(UpmsSSOController.class);
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login(Model model,HttpServletRequest request,HttpServletResponse response){
		log.info("已经进入获取登录界面的方法中");
		/**
		 * 1.getSession 判断是否已经登录
		 * 2.登录，直接拼接code然后，更新返回
		 * 3.未登录，返回登录界面
		 */
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		String sessionId = session.getId().toString();
		//判断是否已经存在code
		String code = RedisUtil.get(BaseConstant.UPMS_SERVER_SESSION_ID + sessionId);
		if(StringUtils.isNotBlank(code)){//存在
			log.info("已经登录了");
			//更新Token，过期时间
			RedisUtil.set(BaseConstant.UPMS_SERVER_SESSION_ID + sessionId, code, (int)session.getTimeout() / 1000);
			RedisUtil.set(BaseConstant.UPMS_SERVER_TOKEN + code, code,(int)session.getTimeout()/1000);
			RedisUtil.sadd(BaseConstant.UPMS_SERVER_SESSION_IDS,sessionId,(int)session.getTimeout()/1000);
			
			//获取获取的url
			String backurl = request.getParameter("backurl");
			String username = (String)subject.getPrincipal();
			if(StringUtils.isNotBlank(backurl)){//存在backurl，即为客户端
				if(backurl.contains("?")){
					backurl+="&code="+ code + "&username=" + username;
				}else{
					backurl+="?code=" + code + "&username=" + username;
				}
				return "redirect:backurl";
			}
			//在Server端
			log.info("来自服务端");
			model.addAttribute("username",username);
			return "redirect:/manage/index";
		}
		//不存在，返回登录界面
		return "/sso/login";
	}
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public Object doLogin(HttpServletRequest request,HttpServletResponse response){
		/**
		 * 1.验证参数
		 * 2.验证是否已经登录
		 * 3.未登录，密码验证，并创建code
		 * 4.登录，拼接返回,修改过期时间
		 * 
		 */
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(StringUtils.isBlank(username)){
			return new BaseResult(ResultConstant.USERNAME_EMPTY,"用户为空");
		}
		if(StringUtils.isBlank(password)){
			return new BaseResult(ResultConstant.PASSWORD_EMPTY,"密码为空");
		}
		//验证是否已经登录
		Subject subject = SecurityUtils.getSubject();
		String sessionId = subject.getSession().getId().toString();
		String code = RedisUtil.get(BaseConstant.UPMS_SERVER_SESSION_ID + sessionId);
		if(StringUtils.isNotBlank(code)){
			//更新过期时间
			RedisUtil.set(BaseConstant.UPMS_SERVER_SESSION_ID + sessionId,code,(int)subject.getSession().getTimeout()/1000);
			RedisUtil.set(BaseConstant.UPMS_SERVER_TOKEN + code,code,(int)subject.getSession().getTimeout()/1000);
			RedisUtil.sadd(BaseConstant.UPMS_SERVER_SESSION_IDS, sessionId, (int)subject.getSession().getTimeout()/1000);
			//回跳
			String backurl = request.getParameter("backurl");
			String username1 = subject.getPrincipal().toString();
			if(StringUtils.isNotBlank(backurl)){
				if(backurl.contains("?")){
					backurl += "&code=" + code + "&username=" + username1;
				}else{
					backurl += "?code=" + code + "&username=" + username1;
				}
				try {
					response.sendRedirect(backurl);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				return new BaseResult(ResultConstant.SUCCESS,"manage/index?username=" + username1) ;
			}
		}
		//未登录，使用username + password进行验证
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
		  try {
//              if (BooleanUtils.toBoolean(rememberMe)) {
//                  usernamePasswordToken.setRememberMe(true);
//              } else {
//                  usernamePasswordToken.setRememberMe(false);
//              }
              subject.login(usernamePasswordToken);
          } catch (UnknownAccountException e) {
              return new BaseResult(ResultConstant.USERNAME_INVALID, "帐号不存在！");
          } catch (IncorrectCredentialsException e) {
              return new BaseResult(ResultConstant.PASWWORD_INVALID,"密码错误!");
          } catch (LockedAccountException e) {
              return new BaseResult(ResultConstant.ACCOUNT_LOCKED,"帐号被锁定!");
          }
          // 创建code
          code = UUID.randomUUID().toString();
		  int timeout = (int)subject.getSession().getTimeout()/1000;
		  RedisUtil.set(BaseConstant.UPMS_SERVER_SESSION_ID + sessionId, code,timeout);
		  RedisUtil.set(BaseConstant.UPMS_SERVER_TOKEN + code,code,timeout);
		  RedisUtil.sadd(BaseConstant.UPMS_SERVER_SESSION_IDS, sessionId,timeout);
		  
		  return new BaseResult(ResultConstant.SUCCESS,"/manage/index?username="+subject.getPrincipal().toString());
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String logout(HttpServletRequest request){
		//退出
		SecurityUtils.getSubject().logout();
	    // 跳回原地址
        String redirectUrl = request.getHeader("Referer");
        if (null == redirectUrl) {
            redirectUrl = "/";
        }
        return "redirect:" + redirectUrl;
	}
}
