package com.leecp.upms.client.shiro.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSONObject;
import com.leecp.upms.client.shiro.session.UpmsSessionDao;
import com.leecp.upms.client.shiro.util.ParameterUtil;
import com.leecp.upms.common.base.BaseConstant;
import com.leecp.upms.common.util.PropertiesFileUtil;
import com.leecp.upms.common.util.RedisUtil;

/**
 * 重写authc过滤器
 * Created by shulee on 2017/3/11.
 */
public class UpmsAuthenticationFilter extends AuthenticationFilter {

    private final static Logger log = LoggerFactory.getLogger(UpmsAuthenticationFilter.class);
    @Autowired
    UpmsSessionDao upmsSessionDao;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    	log.debug("请求已经进入isAccessAllowed，对用户进行认证检查");
        Subject subject = getSubject(request, response);
        Session session = subject.getSession();
        // 判断请求类型
        String upmsType = PropertiesFileUtil.getInstance("upms-client").get("upms.role");
        if ("client".equals(upmsType)) {
        	session.setAttribute(BaseConstant.UPMS_ROLE,"client");
            return validateClient(request, response);
        }
        if ("server".equals(upmsType)) {
        	session.setAttribute(BaseConstant.UPMS_ROLE,"server");
            return subject.isAuthenticated();
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    	log.debug("用户未认证，跳转到CAS进行登录验证");
        StringBuffer sso_server_url = new StringBuffer(PropertiesFileUtil.getInstance("upms-client").get("upms.sso.server.url"));
        // server需要登录
        String upmsType = PropertiesFileUtil.getInstance("upms-client").get("upms.role");
        //本身就是Server，直接跳转就可以
        if ("server".equals(upmsType)) {
            WebUtils.toHttp(response).sendRedirect(sso_server_url.toString());
            return false;
        }
        //需要对应用服务器进行一个验证
//        sso_server_url.append("?").append("appid").append("=").append(PropertiesFileUtil.getInstance("upms-client").get("upms.appID"));
        // 回跳地址
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        StringBuffer backurl = httpServletRequest.getRequestURL();
        String queryString = httpServletRequest.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            backurl.append("?").append(queryString);
        }
//        sso_server_url.append("&").append("backurl").append("=").append(URLEncoder.encode(backurl.toString(), "utf-8"));
        sso_server_url.append("?").append("backurl").append("=").append(URLEncoder.encode(backurl.toString(), "utf-8"));
        WebUtils.toHttp(response).sendRedirect(sso_server_url.toString());
        return false;
    }

    /**
     * 认证中心登录成功带回code
     * @param request
     */
    private boolean validateClient(ServletRequest request, ServletResponse response) {
    	log.debug("对来自第三方的用户进行验证");
        Subject subject = getSubject(request, response);
        Session session = subject.getSession();
        String sessionId = session.getId().toString();
        int timeOut = (int) session.getTimeout() / 1000;
        // 判断局部会话是否登录
        String cacheClientSession = RedisUtil.get(BaseConstant.UPMS_CLIENT_SESSION_ID  + session.getId());
        if (StringUtils.isNotBlank(cacheClientSession)) {
            // 更新code有效期
            RedisUtil.set(BaseConstant.UPMS_CLIENT_SESSION_ID + sessionId, cacheClientSession, timeOut);
            Jedis jedis = RedisUtil.getJedis();
            jedis.expire(BaseConstant.UPMS_CLIENT_SESSION_IDS + cacheClientSession, timeOut);
            jedis.close();
            // 移除url中的code参数
            if (null != request.getParameter("code")) {
                String backUrl = ParameterUtil.removeToken(WebUtils.toHttp(request));
                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                try {
                    httpServletResponse.sendRedirect(backUrl.toString());
                } catch (IOException e) {
                    log.error("局部会话已登录，移除code参数跳转出错：", e);
                }
            } else {
                return true;
            }
        }
        // 判断是否有认证中心code
        String code = request.getParameter("upms_code");
        // 已拿到code，进行验证，如果没有，直接返回false,到onAccessDenied中跳转登录
        if (StringUtils.isNotBlank(code)) {
            // HttpPost去校验code
            try {
                StringBuffer sso_server_url = new StringBuffer(PropertiesFileUtil.getInstance("lee-upms-client").get("lee.upms.sso.server.url"));
                HttpClient httpclient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost(sso_server_url.append("/sso/code").toString());

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("code", code));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse httpResponse = httpclient.execute(httpPost);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity));
                    if (1 == result.getIntValue("code") && result.getString("data").equals(code)) {
                        // code校验正确，创建局部会话
                        RedisUtil.set(BaseConstant.UPMS_CLIENT_SESSION_ID + sessionId, code, timeOut);
                        // 保存code对应的局部会话sessionId，方便退出操作
                        RedisUtil.sadd(BaseConstant.UPMS_CLIENT_SESSION_IDS  + code, sessionId, timeOut);
                        log.debug("当前code={}，对应的注册系统个数：{}个", code, RedisUtil.getJedis().scard(BaseConstant.UPMS_CLIENT_SESSION_IDS + code));
                        // 移除url中的code参数
                        String backUrl = ParameterUtil.removeToken(WebUtils.toHttp(request));
                        // 返回请求资源
                        try {
                            HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                            httpServletResponse.sendRedirect(backUrl.toString());
                            return true;
                        } catch (IOException e) {
                            log.error("已拿到code，移除code参数跳转出错：", e);
                        }
                    } else {
                        log.warn(result.getString("data"));
                    }
                }
            } catch (IOException e) {
                log.error("验证code失败：", e);
            }
        }
        //没有登录，返回false进行登录操作
        return false;
    }

}
 