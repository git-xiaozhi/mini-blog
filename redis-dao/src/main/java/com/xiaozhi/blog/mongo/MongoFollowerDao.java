package com.xiaozhi.blog.mongo;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ArrayListMultimap;
import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.utils.UserGroupHelper;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.User;
import com.xiaozhi.blog.vo.UserGroup;



@Repository
public class MongoFollowerDao {

    private static Log logger = LogFactory.getLog(MongoFollowerDao.class);

    @Autowired
    private  StringRedisTemplate template;

    @Autowired
    private MongoTemplate mongoTemplate;



    /**
     * 分页获取粉丝列表
     * @param uid
     * @return
     */
    public List<User> getFollowerByPage(String uid, Range range) {

        //当前页用户集合
    	List<String> ids= template.opsForList().range(KeyUtils.followers(uid), range.being, range.end-range.being+1);
    	List<String> followings= template.opsForList().range(KeyUtils.following(uid),0,-1);
    	List<User> users = this.mongoTemplate.find(new Query(Criteria.where("id").in(ids.toArray())),User.class);

    	for(User user :users){
    		if(followings.contains(user.getId().toString())){//判断是否相互关注
               user.setLink(true);
            }else{
               user.setLink(false);
            }
    	}
       return users;
    }

    /**
     * 分页获取粉丝列表(共同关注 我关注的人也关注他)
     * @param uid
     * @return
     */
    private List<User> getFollowers(String uid,List<String> ids, Range range) {
        if(ids==null || ids.isEmpty())return null;
        //当前页用户集合
    	List<String> pagekeysList=ids.subList(range.being, (range.end+1)>ids.size()?ids.size():(range.end+1));
    	List<String> followings= template.opsForList().range(KeyUtils.following(uid),0,-1);
    	List<User> users = this.mongoTemplate.find(new Query(Criteria.where("id").in(pagekeysList.toArray())),User.class);
    	for(User user :users){
    		if(followings.contains(user.getId().toString())){//判断是否相互关注
               user.setLink(true);
            }else{
               user.setLink(false);
            }
    	}
       return users;
    }


    /**
     * 返回总粉丝数
     * @param uid
     * @return
     */
    public int getFollowersNum(String uid){
        return template.opsForList().size(KeyUtils.followers(uid)).intValue();
    }


    /**
     * 我关注的人关注他的人
     * @param uid
     * @param targetUid
     * @return
     */
    public ListPage<User> alsoFollowed(String uid, String targetUid,Integer page,Integer pagesize) {

        List<String> followings = following(uid);
        followings.retainAll(followers(targetUid));//交集


        int firstResult = (page-1)*pagesize;
        int lastResult = (page-1)*pagesize+pagesize-1;
        int allResults = followings.size();

        List<User> users=this.getFollowers(uid, followings, new Range(firstResult,lastResult));
        return new ListPage<User>(users, firstResult, lastResult, allResults);
    }


    /**
     * 我和他共同关注的人
     * @param uid
     * @param targetUid
     * @return
     */
    public ListPage<User> commonFollowers(String uid, String targetUid,Integer page,Integer pagesize) {

        List<String> followings = following(uid);
        followings.retainAll(following(targetUid));//交集

        int firstResult = (page-1)*pagesize;
        int lastResult = (page-1)*pagesize+pagesize-1;
        int allResults = followings.size();
        List<User> users=this.getFollowers(uid, followings, new Range(firstResult,lastResult));
        return new ListPage<User>(users, firstResult, lastResult, allResults);
    }


    private List<String> following(String uid) {
    	return template.opsForList().range(KeyUtils.following(uid), 0, -1);
    }

    private List<String> followers(String uid) {
        return template.opsForList().range(KeyUtils.followers(uid), 0, -1);
    }

}
