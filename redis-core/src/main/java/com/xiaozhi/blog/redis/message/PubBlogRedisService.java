package com.xiaozhi.blog.redis.message;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;



@Service
public class PubBlogRedisService {
	
	private static Log logger = LogFactory.getLog(PubBlogRedisService.class);


	@Autowired
	private  RedisTemplate<String, Object> redisTemplate;
	
	
	public void pubBlog(String content){
		logger.debug("------------------> message publish!");
		redisTemplate.convertAndSend("chatroom", content);
	}
	
	

}
