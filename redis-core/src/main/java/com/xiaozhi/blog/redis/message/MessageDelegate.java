package com.xiaozhi.blog.redis.message;

import java.io.Serializable;
import java.util.Map;



public interface MessageDelegate {
	
	void handleMessage(String message);

    void handleMessage(Map message);

    void handleMessage(byte[] message);

    void handleMessage(Serializable message);

}
