package com.xiaozhi.blog.redis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.support.collections.DefaultRedisSet;
import org.springframework.data.redis.support.collections.RedisSet;
import org.springframework.stereotype.Component;

import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.User;

//@Component
public class FollowerDao1 {

    private static Log logger = LogFactory.getLog(FollowerDao1.class);

    @Autowired
    private  StringRedisTemplate template;

    /**
     * 分页获取粉丝列表
     * @param uid
     * @return
     */
    public List<User> getFollowers(final String uid,String key, Range range) {

        SortQuery<String> query = SortQueryBuilder.sort(key).noSort()
                .get("#")
                .get("uid:*->name")
                .get("uid:*->school")
                .get("uid:*->company")
                .get("uid:*->followerNum")
                .get("uid:*->portraitUrl")
                .get("uid:*->nickname")
                .limit(range.being, range.end-range.being+1).build();
        final Set<String> followingSet= template.opsForSet().members(KeyUtils.following(uid));//

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
                if(followingSet.contains(user.getId().toString())){//判断是否相互关注
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
     * 返回总粉丝数
     * @param uid
     * @return
     */
    public int getFollowersNum(String uid){
        return template.opsForSet().size(KeyUtils.followers(uid)).intValue();
    }


    /**
     * 我关注的人关注他的人
     * @param uid
     * @param targetUid
     * @return
     */
    public ListPage<User> alsoFollowed(String uid, String targetUid,Integer page,Integer pagesize) {
        RedisSet<String> tempSet = following(uid).intersectAndStore(followers(targetUid),KeyUtils.alsoFollowed(uid, targetUid));

        String key = tempSet.getKey();
        template.expire(key, 5, TimeUnit.SECONDS);

        int firstResult = (page-1)*pagesize;
        int lastResult = (page-1)*pagesize+pagesize-1;
        int allResults = tempSet.size();

        List<User> users=this.getFollowers(uid, key, new Range(firstResult,lastResult));
        return new ListPage<User>(users, firstResult, lastResult, allResults);
    }


    /**
     * 我和他共同关注的人
     * @param uid
     * @param targetUid
     * @return
     */
    public ListPage<User> commonFollowers(String uid, String targetUid,Integer page,Integer pagesize) {
        RedisSet<String> tempSet = following(uid).intersectAndStore(following(targetUid),KeyUtils.commonFollowers(uid, targetUid));
        String key = tempSet.getKey();
        template.expire(key, 5, TimeUnit.SECONDS);

        int firstResult = (page-1)*pagesize;
        int lastResult = (page-1)*pagesize+pagesize-1;
        int allResults = tempSet.size();

        List<User> users=this.getFollowers(uid, key, new Range(firstResult,lastResult));
        return new ListPage<User>(users, firstResult, lastResult, allResults);
    }


    private RedisSet<String> following(String uid) {
        return new DefaultRedisSet<String>(KeyUtils.following(uid), template);
    }

    private RedisSet<String> followers(String uid) {
        return new DefaultRedisSet<String>(KeyUtils.followers(uid), template);
    }

}
