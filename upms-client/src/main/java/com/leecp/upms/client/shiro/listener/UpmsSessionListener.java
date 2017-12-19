package com.leecp.upms.client.shiro.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shiro Session监听器
 * @author LeeCP
 *
 */
public class UpmsSessionListener implements SessionListener {
	private static final Logger log = LoggerFactory.getLogger(SessionListener.class);

	public void onStart(Session session) {
		log.debug("Shiro session had been started!");
		
	}

	public void onStop(Session session) {
		log.debug("Shiro session had been stoped!");
	}

	public void onExpiration(Session session) {
		log.debug("Shiro session had been expired!");
	}

}
