package com.xiaozhi.blog.redis;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.support.collections.DefaultRedisSet;
import org.springframework.data.redis.support.collections.RedisSet;
import org.springframework.stereotype.Component;

import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.User;

//@Component
public class FollowingDao1 {


    private static Log logger = LogFactory.getLog(FollowingDao1.class);

    @Autowired
    private  StringRedisTemplate template;


    /**
     * 获取关注列表
     * @param uid
     * @return
     */
    public Set<String> getFollowings(String uid){
        return template.opsForSet().members(KeyUtils.following(uid));
    }


    /**
     * 分页获取关注列表
     * @param uid
     * @return
     */
    public List<User> getFollowings(final String uid, Range range) {

        SortQuery<String> query = SortQueryBuilder.sort(KeyUtils.following(uid)).noSort()
                .get("#")
                .get("uid:*->name")
                .get("uid:*->school")
                .get("uid:*->company")
                .get("uid:*->followerNum")
                .get("uid:*->portraitUrl")
                .get("uid:*->nickname")
                .limit(range.being, range.end-range.being+1).build();
        final Set<String> followerSet= template.opsForSet().members(KeyUtils.followers(uid));//

        BulkMapper<User, String> hm = new BulkMapper<User, String>() {
            public User mapBulk(List<String> bulk) {
                User user = new User();
                user.setId(bulk.get(0));
                user.setName(bulk.get(1));
                user.setSchool(bulk.get(2));
                user.setCompany(bulk.get(3));
                user.setFollowerNum(Integer.valueOf(bulk.get(4)==null?"0":bulk.get(4)));
                user.setPortraitUrl(bulk.get(5));
                user.setNickname(bulk.get(6));
                if(followerSet.contains(user.getId().toString())){//判断是否相互关注
                 user.setLink(true);
                }else{
                 user.setLink(false);
                }
                //logger.debug("######################user :"+user);
                return user;
            }
        };
        return template.sort(query, hm);
    }


    /**
     * 返回总关注数
     * @param uid
     * @return
     */
    public int getFollowingsNum(String uid){
        return template.opsForSet().size(KeyUtils.following(uid)).intValue();
    }



    public boolean isFollowing(String uid, String targetUid) {
        return following(uid).contains(targetUid);
    }

    /**
     * 加关注
     * @param targetUser
     */
    public boolean follow(final String targetUid,final String uid) {

        boolean result = (Boolean) template.execute(new RedisCallback<Object>() {
             @Override
             public Object doInRedis(RedisConnection connection)throws DataAccessException {
                try {
                     connection.multi();//事务开启
                     connection.sAdd(KeyUtils.following(uid).getBytes(), targetUid.getBytes());
                     connection.hIncrBy(KeyUtils.uid(uid).getBytes(), "followingNum".getBytes(), 1);//对自己关注数+1操作
                     connection.sAdd(KeyUtils.followers(targetUid).getBytes(), uid.getBytes());
                     connection.hIncrBy(KeyUtils.uid(targetUid).getBytes(), "followerNum".getBytes(), 1);//对对方的粉丝数+1操作
                     connection.exec();
                     return true;
                } catch (Exception e) {
                    logger.error("==================> follow error :"+e.toString());
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

        boolean result = (Boolean) template.execute(new RedisCallback<Object>() {
             @Override
             public Object doInRedis(RedisConnection connection)throws DataAccessException {
                try {
                     connection.multi();//事务开启
                     connection.sRem(KeyUtils.following(uid).getBytes(), targetUid.getBytes());
                     connection.hIncrBy(KeyUtils.uid(uid).getBytes(), "followingNum".getBytes(), -1);//对自己关注数-1操作
                     connection.sRem(KeyUtils.followers(targetUid).getBytes(), uid.getBytes());
                     connection.hIncrBy(KeyUtils.uid(targetUid).getBytes(), "followerNum".getBytes(), -1);//对对方的粉丝数-1操作
                     connection.exec();
                     return true;
                } catch (Exception e) {
                    logger.error("==================> stopFollowing error :"+e.toString());
                }finally{
                    connection.close();
                }
                return false;
              }
            });

        return result;
    }

    private RedisSet<String> following(String uid) {
        return new DefaultRedisSet<String>(KeyUtils.following(uid), template);
    }

//	private List<String> covertUidsToNames(String key) {
//		return template.sort(SortQueryBuilder.sort(key).noSort().get("uid:*->name").build());
//	}


//	private RedisSet<String> followers(String uid) {
//		return new DefaultRedisSet<String>(KeyUtils.followers(uid), template);
//	}

}
