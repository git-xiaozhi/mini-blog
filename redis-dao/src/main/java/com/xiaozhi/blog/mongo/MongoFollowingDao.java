package com.xiaozhi.blog.mongo;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ArrayListMultimap;
import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.utils.UserGroupHelper;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.User;
import com.xiaozhi.blog.vo.UserGroup;

@Repository
public class MongoFollowingDao {


    private static Log logger = LogFactory.getLog(MongoFollowingDao.class);

    @Autowired
    private  StringRedisTemplate template;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoUserGroupDao mongoUserGroupDao;



    public List<String> getFollowings(String uid){
    	return template.opsForList().range(KeyUtils.followers(uid),0,-1);
    }
    /**
     * 分页获取关注列表
     * @param uid
     * @return
     */
    public List<User> getFollowings(String uid, Range range) {
        //当前页用户集合
    	List<String> ids= template.opsForList().range(KeyUtils.following(uid), range.being, range.end-range.being+1);
    	List<String> followers= template.opsForList().range(KeyUtils.followers(uid),0,-1);
    	List<User> users = this.mongoTemplate.find(new Query(Criteria.where("id").in(ids.toArray())),User.class);

    	List<UserGroup> groups = this.mongoUserGroupDao.getGroupByUserId(uid);
    	ArrayListMultimap<String,UserGroup> gMultimap = UserGroupHelper.getUserGroupsMulitMap(groups);

    	for(User user :users){
    		if(followers.contains(user.getId().toString())){//判断是否相互关注
               user.setLink(true);
            }else{
               user.setLink(false);
            }
    		user.setGroups(gMultimap.get(user.getId()));//设置他所在的用户组
    	}
       return users;
    }


    /**
     * 返回总关注数
     * @param uid
     * @return
     */
    public int getFollowingsNum(String uid){
        return template.opsForList().size(KeyUtils.following(uid)).intValue();
    }


    /**
     * 判断是否关注
     * @param uid
     * @param targetUid
     * @return
     */
    public boolean isFollowing(String uid, String targetUid) {
        return following(uid).contains(targetUid);
    }

    /**
     * 加关注
     * @param targetUser
     */
    public boolean follow(final String targetUid,final String uid) {

       try {
       	this.mongoTemplate.updateFirst(new Query(Criteria.where("id").is(uid)),
        		new Update().inc("followingNum", 1),User.class);//对自己关注数+1操作
    	this.mongoTemplate.updateFirst(new Query(Criteria.where("id").is(targetUid)),
        		new Update().inc("followerNum", 1),User.class);//对对方的粉丝数+1操作

	   } catch (Exception e) {
		   logger.error("==================> Mongo follow error :"+e.toString());
		   return false;
	   }

      boolean result = (Boolean) template.execute(new RedisCallback<Object>() {
             @Override
             public Object doInRedis(RedisConnection connection)throws DataAccessException {
                try {
                     connection.multi();//事务开启
                     connection.lPush(KeyUtils.following(uid).getBytes(), targetUid.getBytes());
                     connection.lPush(KeyUtils.followers(targetUid).getBytes(), uid.getBytes());
                     connection.exec();
                     return true;
                } catch (Exception e) {
                    logger.error("==================> follow error :"+e.toString());
                	mongoTemplate.updateFirst(new Query(Criteria.where("id").is(uid)),
                    		new Update().inc("followingNum", -1),User.class);//对自己关注数-1操作
                	mongoTemplate.updateFirst(new Query(Criteria.where("id").is(targetUid)),
                    		new Update().inc("followerNum", -1),User.class);//对对方的粉丝数-1操作

                }finally{
                    connection.close();
                }
                return false;
              }
            });

        return result;
    }

    /**
     * 停止关注
     * @param targetUser
     */
    public boolean stopFollowing(final String targetUid,final String uid) {
        try {
           	this.mongoTemplate.updateFirst(new Query(Criteria.where("id").is(uid)),
            		new Update().inc("followingNum", -1),User.class);//对自己关注数-1操作
        	this.mongoTemplate.updateFirst(new Query(Criteria.where("id").is(targetUid)),
            		new Update().inc("followerNum", -1),User.class);//对对方的粉丝数-1操作

    	   } catch (Exception e) {
    		   logger.error("==================> Mongo stopFollowing error :"+e.toString());
    		   return false;
    	   }

        boolean result = (Boolean) template.execute(new RedisCallback<Object>() {
             @Override
             public Object doInRedis(RedisConnection connection)throws DataAccessException {
                try {
                     connection.multi();//事务开启
                     connection.lRem(KeyUtils.following(uid).getBytes(), 0, targetUid.getBytes());
                     connection.lRem(KeyUtils.followers(targetUid).getBytes(),0, uid.getBytes());
                     connection.exec();
                     return true;
                } catch (Exception e) {
                    logger.error("==================> stopFollowing error :"+e.toString());
                    //callback
                	mongoTemplate.updateFirst(new Query(Criteria.where("id").is(uid)),
                    		new Update().inc("followingNum", 1),User.class);//对自己关注数+1操作
                	mongoTemplate.updateFirst(new Query(Criteria.where("id").is(targetUid)),
                    		new Update().inc("followerNum", 1),User.class);//对对方的粉丝数+1操作
                }finally{
                    connection.close();
                }
                return false;
              }
            });

        return result;
    }

    private RedisList<String> following(String uid) {
        return new DefaultRedisList<String>(KeyUtils.following(uid), template);
    }

}
