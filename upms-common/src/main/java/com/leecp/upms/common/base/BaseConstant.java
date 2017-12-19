package com.leecp.upms.common.base;

/**
 * 定义系统常量
 * @author LeeCP
 *
 */
public abstract class BaseConstant {
    // 表示当前权限系统的角色，服务端 或者 客户端
    public static final String UPMS_ROLE = "upms-role";
    // 会话key
    public final static String UPMS_SHIRO_SESSION_ID = "upms-shiro-session-id_";
    // 全局会话Id
    public final static String UPMS_SERVER_SESSION_ID = "upms-server-session-id_";
    // 全局会话列表Id
    public final static String UPMS_SERVER_SESSION_IDS = "upms-server-session-ids";
    // 用于客户端验证的token
    public final static String UPMS_SERVER_TOKEN = "upms-server-token_";
    // 客户端Session前缀
    public static final String UPMS_CLIENT_SESSION_ID = "upms-client-session-id_";
    // 客户端同一个Token下的会话Id
    public static final String UPMS_CLIENT_SESSION_IDS = "upms-client-session-ids_";
    
}
