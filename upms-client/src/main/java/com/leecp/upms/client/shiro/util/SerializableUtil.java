package com.leecp.upms.client.shiro.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;

public class SerializableUtil {
	//序列化
	public static String serialize(Session session){
		if(null == session){
			return null;
		}
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos= new ObjectOutputStream(bos);
			oos.writeObject(session);
			return Base64.encodeToString(bos.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException("serialize session error",e);
		}
	}
	//反序列
	public static Session deserialize(String sessionStr){
		if(null == sessionStr || "".equals(sessionStr))return null;
		try{
			ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decode(sessionStr));
			ObjectInputStream ois = new ObjectInputStream(bis);
			return (Session)ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException("deserialize session error",e);
		}
	}
}
