package com.xiaozhi.blog.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;
import com.tianji.test.core.redis.AuthUser;
import com.xiaozhi.blog.formbean.UserForm;
import com.xiaozhi.blog.utils.Collections3;
import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.utils.MD5;
import com.xiaozhi.blog.vo.SinaAccessToken;
import com.xiaozhi.blog.vo.User;


@Repository
public class MongoUserDao {

	private static Log logger = LogFactory.getLog(MongoUserDao.class);

	private static final Pattern MENTION_REGEX = Pattern.compile("@([-_a-zA-Z0-9\u4e00-\u9fa5]{2,20}+)");

	private  RedisAtomicLong userIdCounter;

	@Autowired
	private MongoOperations mongoTemplate;


	@Autowired
    private  StringRedisTemplate template;


    @PostConstruct
    public void init () {
        userIdCounter = new RedisAtomicLong(KeyUtils.globalUid(), template.getConnectionFactory());
    }


	/**
	 * 新注册用户
	 *
	 * @param name
	 * @param password
	 * @param company
	 * @param school
	 * @return
	 */
	public boolean addUser(User user) {
		final String uid = String.valueOf(userIdCounter.incrementAndGet());

		user.setId(uid);
		user.setPass(MD5.calcMD5(user.getPass()));
		user.setRoles("[\"ROLE_USER\"]");
		/** 给新注册用户增加默认用户权限 */
		try {
			this.mongoTemplate.save(user);
		} catch (Exception e) {
			logger.error("------------addUser error :" + e.toString(), e);
			return false;
		}

		return true;
	}

	/**
	 * 用户名是否重复
	 *
	 * @param name
	 * @return
	 */
	public boolean isNameDuplicate(String name) {
		User user = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(name)), User.class);
		return user == null ? false : true;
	}

	/**
	 * 用户昵称是否重复
	 *
	 * @param nickname
	 * @return
	 */
	public boolean isNickNameDuplicate(String nickname) {
		User user = this.mongoTemplate.findOne(new Query(Criteria.where("nickname").is(nickname)), User.class);
		return user == null ? false : true;
	}


//	public User getUserByNickName(String nickname) {
//		User user = this.mongoTemplate.findOne(new Query(Criteria.where("nickname").is(nickname)), User.class);
//		return user;
//	}

    /**
     * 通过用户昵称集合一次性获取用户集合
     * @param nicknames
     * @return
     */
    public Map<String, User> getUsersByNickNames(List<String> nicknames){
    	List<User> users=this.mongoTemplate.find(new Query(Criteria.where("nickname").in(nicknames.toArray())),User.class);
    	return new Collections3<String,User>().extractToMap(users, "nickname");
    }

	/**
	 * 编辑用户信息
	 *
	 * @param user
	 * @param company
	 * @param school
	 * @return
	 */
	public boolean editUser(final String uid, final UserForm userForm) {
		try {
			WriteResult result = this.mongoTemplate.updateFirst(
					new Query(Criteria.where("id").is(uid)),
					new Update().set("nickname", userForm.getNickname())
							.set("company", userForm.getCompany())
							.set("school", userForm.getSchool()), User.class);

			return true;
		} catch (Exception e) {
			logger.error("==================> editUser error :" + e.toString());
			return false;
		}

	}

	/**
	 * 保存weibo头像
	 *
	 * @param url
	 * @param uid
	 * @return
	 */
	public boolean editPortrait(String url, String uid) {
		try {
			WriteResult result = this.mongoTemplate.updateFirst(
					new Query(Criteria.where("id").is(uid)),
					new Update().set("portraitUrl", url), User.class);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("----------------------->error:" + e.toString());
		}
		return false;
	}

    /**
     * 获取用户
     * @param user
     * @return
     */
    public User getUserById(String uid) {
    	return this.mongoTemplate.findOne(new Query(Criteria.where("id").is(uid)),
    			User.class);
    }


    /**
     * 通过登录用户名拿到登录用户 权限列表
     * @param user
     * @return
     */
    public AuthUser getAuthUserByName(String name) {
        User user = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(name)),User.class);
        if(user.getRoles()==null)return null;
        List<String> rolList = new ArrayList<String>();
        try {
            rolList = new ObjectMapper().readValue(user.getRoles(), new TypeReference<List<String>>() {});
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new AuthUser(name,user.getId(), user.getPass(),rolList);
    }



    /**
     * 保存accessToken
     * @param accessTokenObj
     */
    public boolean saveAccessToken(String uid,Object accessTokenObj){

		try {
			WriteResult result = this.mongoTemplate.updateFirst(
					new Query(Criteria.where("id").is(uid)),
					new Update().set("accessToken", accessTokenObj), User.class);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("----------------------->error:" + e.toString());
		}
		return false;
    }

    /**
     * 获取用户accesstoken
     * @param user
     * @return
     */
    public SinaAccessToken getAccessTokenByUser(String uid){
    	User user = this.mongoTemplate.findOne(new Query(Criteria.where("id").is(uid)),User.class);
    	if(logger.isDebugEnabled()){
			logger.debug("--------------------------->user :"+user.toString());
		}
    	return user.getAccessToken();
    }

    /**
     * 通過登錄名得到用戶id
     * @param name
     * @return
     */
//    public String findUid(String name) {
//    	User user = this.mongoTemplate.findOne(new Query(Criteria.where("name").is(name)),User.class);
//    	return user.getId();
//    }

    /**
     * 通過昵稱獲得用戶id
     * @param nickname
     * @return
     */
//    public String findUidByNickname(String nickname) {
//    	User user = this.mongoTemplate.findOne(new Query(Criteria.where("nickname").is(nickname)),User.class);
//    	return user.getId();
//    }


    /**
     * 通过用户id集合获取用户Map集合
     * @param ids
     * @return
     */
    public Map<String, User> getUsersByIds(List<String> ids){
    	List<User> users=this.mongoTemplate.find(new Query(Criteria.where("id").in(ids.toArray())),User.class);
    	return new Collections3<String,User>().extractToMap(users, "id");
    }


    public List<User> getUserListByIds(List<String> ids){
    	List<User> users=this.mongoTemplate.find(new Query(Criteria.where("id").in(ids.toArray())),User.class);
    	return users;
    }




    public static Collection<String> findMentions(String content) {
        Matcher regexMatcher = MENTION_REGEX.matcher(content);
        List<String> mentions = new ArrayList<String>();

        while (regexMatcher.find()) {
            String name = regexMatcher.group().substring(1);
            if(!mentions.contains(name))mentions.add(name);
        }

        return mentions;
    }

}
