package com.xiaozhi.blog.redis.message;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;




@Service
public class DefaultMessageDelegate implements MessageDelegate{
	
	private static Log logger = LogFactory.getLog(DefaultMessageDelegate.class);
	
	
	@Override
	public void handleMessage(String message) {
		logger.debug("################### String message :"+message);
		
	}

	@Override
	public void handleMessage(Map message) {
		logger.debug("################### Map message :"+message);
		
	}

	@Override
	public void handleMessage(byte[] message) {
		logger.debug("################### byte message :"+message);
		
	}

	@Override
	public void handleMessage(Serializable message) {
		logger.debug("################### Serializable message :"+message);
		
	}

}
