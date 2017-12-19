package com.leecp.upms.client.shiro.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.leecp.upms.client.shiro.util.SerializableUtil;
import com.leecp.upms.common.base.BaseConstant;
import com.leecp.upms.common.util.RedisUtil;

public class UpmsSessionDao extends CachingSessionDAO {
	
    private static Logger log = LoggerFactory.getLogger(UpmsSessionDao.class);
	@Override
	protected void doUpdate(Session session) {
		if(null == session || null == session.getId()) {
			return;
		}
		String sessionId = session.getId().toString();
		//如果过期则不更新
		if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()) {
			return ;
		}
		RedisUtil.set(BaseConstant.UPMS_SHIRO_SESSION_ID + sessionId, SerializableUtil.serialize(session),(int)session.getTimeout() / 1000);
		log.debug("doUpdate LEE_UPMS_SHIRO_SESSION_ID = {}",sessionId);
	}

	@Override
	protected void doDelete(Session session) {
		if(null == session || null == session.getId()) {
			return;
		}
		String upmsRole = session.getAttribute(BaseConstant.UPMS_ROLE).toString();
		String sessionId = session.getId().toString();
		if("client".equals(upmsRole)) {
			String token = RedisUtil.get(BaseConstant.UPMS_CLIENT_SESSION_ID + sessionId);
			//删除token中的一个子系统的会话Id
			RedisUtil.srem(BaseConstant.UPMS_CLIENT_SESSION_IDS +  token,sessionId);
			//删除局部会话的Id
			RedisUtil.remove(BaseConstant.UPMS_CLIENT_SESSION_ID  + sessionId);
		}
		//删除CAS中心的token
		if("server".equals(upmsRole)) {
			String token = RedisUtil.get(BaseConstant.UPMS_SERVER_SESSION_ID  + sessionId);
			Jedis jedis = RedisUtil.getJedis();
			//删除局部会话的token
			Set<String> sessionIds = jedis.smembers(BaseConstant.UPMS_CLIENT_SESSION_IDS + token);
			for (String clientSessionId : sessionIds) {
				jedis.del(BaseConstant.UPMS_CLIENT_SESSION_ID  + clientSessionId);
				jedis.srem(BaseConstant.UPMS_CLIENT_SESSION_IDS  + token, sessionId);
			}
			//删除全局的会话token
			RedisUtil.remove(BaseConstant.UPMS_CLIENT_SESSION_ID  + token);
			//删除全局中会话管理中的sessionId
			jedis.lrem(BaseConstant.UPMS_CLIENT_SESSION_IDS,1, sessionId);
			jedis.close();
		}
		//删除会话
		RedisUtil.remove(BaseConstant.UPMS_SHIRO_SESSION_ID  + sessionId);
		log.debug("doDelete LEE_UPMS_SHIRO_SESSION_ID = {}",sessionId);
	}

	@Override
	protected Serializable doCreate(Session session) {
		if(session == null){
			return null;
		}
		//使用shiro的SessionId产生器
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session,sessionId);
		RedisUtil.set(BaseConstant.UPMS_SHIRO_SESSION_ID + sessionId, SerializableUtil.serialize(session), (int) session.getTimeout() / 1000);
		log.debug("doCreate LEE_UPMS_SHIRO_SESSION_ID = {}",sessionId);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		if(sessionId == null){
			return null;
		}
		Session session = SerializableUtil.deserialize(RedisUtil.get(BaseConstant.UPMS_SHIRO_SESSION_ID  + sessionId));
		log.debug("doReadSession UPMS_SHIRO_SESSION_ID = {}",sessionId);
		return session;
	}
	
	/**
     * 获取会话列表
     * @param offset
     * @param limit
     * @return
     */
    public Map getActiveSessions(int offset, int limit) {
        Map sessions = new HashMap();
        Jedis jedis = RedisUtil.getJedis();
        // 获取在线会话总数
        long total = jedis.llen(BaseConstant.UPMS_CLIENT_SESSION_IDS);
        // 获取当前页会话详情
        List<String> ids = jedis.lrange(BaseConstant.UPMS_CLIENT_SESSION_IDS, offset, (offset + limit - 1));
        List<Session> rows = new ArrayList<Session>();
        for (String id : ids) {
            String session = RedisUtil.get(BaseConstant.UPMS_SHIRO_SESSION_ID  + id);
            // 过滤redis过期session
            if (null == session) {
                RedisUtil.lrem(BaseConstant.UPMS_CLIENT_SESSION_IDS, 1, id);
                total = total - 1;
                continue;
            }
             rows.add(SerializableUtil.deserialize(session));
        }
        jedis.close();
        sessions.put("total", total);
        sessions.put("rows", rows);
        return sessions;
    }



}
