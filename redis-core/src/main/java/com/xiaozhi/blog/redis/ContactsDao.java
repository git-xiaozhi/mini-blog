package com.xiaozhi.blog.redis;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.xiaozhi.blog.utils.KeyUtils;



@Component
public class ContactsDao {

	private static Log logger = LogFactory.getLog(ContactsDao.class);

	@Autowired
	private  StringRedisTemplate template;

	/**
	 * 加用户到收藏
	 * @param name
	 * @param jsonUser
	 */
	public void addUserToBookMark(String name,String jsonUser){
		logger.debug("----------------->jsonUser :"+jsonUser);
		template.opsForList().leftPush(KeyUtils.bookMark(name), jsonUser);
	}

	/**
	 * 删除收藏
	 * @param name
	 * @param userid
	 * @return
	 */
    public boolean removeBookMark(String name,String userid){
    	List<String> users = getUserBookMarkList(name);
    	for(int i=0;i<=users.size()-1;i++){
    	 if(StringUtils.hasText("id:\""+userid+"\"")){
		  long result= template.opsForList().remove(KeyUtils.bookMark(name), 0, users.get(i));
		  if(result>=0)return true;
    	 }
    	}
		return false;
	}

	/**
	 * 获取收藏列表
	 * @param name
	 * @return
	 */
    public List<String> getUserBookMarkList(String name){
    	List<String> usersList =template.opsForList().range(KeyUtils.bookMark(name), 0, -1);
    	for(String uString :usersList){
    	   logger.debug("----------------->"+uString);
    	}
		return usersList;
	}


}
