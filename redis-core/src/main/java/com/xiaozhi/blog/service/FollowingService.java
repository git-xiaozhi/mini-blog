package com.xiaozhi.blog.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaozhi.blog.mongo.MongoBlogDao;
import com.xiaozhi.blog.mongo.MongoFollowingDao;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.User;

@Service
public class FollowingService {

    private static Log logger = LogFactory.getLog(FollowingService.class);

    @Autowired
    private  MongoFollowingDao mongoFollowingDao;
    @Autowired
    private MongoBlogDao mongoBlogDao;

    /**
     * 获取第一页的几个关注人
     * @param uid
     * @return
     */
    public List<User> getFollowing(String uid) {
        return this.mongoFollowingDao.getFollowings(uid,new Range(1));
    }

    /**
     * 分页获取关注列表
     * @param uid
     * @param range
     * @return
     */
    public ListPage<User> getFollowingsByPage(String uid, Integer page,Integer pagesize) {

        int firstResult = (page-1)*pagesize;
        int lastResult = (page-1)*pagesize+pagesize-1;
        int allResults = this.mongoFollowingDao.getFollowingsNum(uid);
        List<User> users=this.mongoFollowingDao.getFollowings(uid, new Range(firstResult,lastResult));
        return new ListPage<User>(users, firstResult, lastResult, allResults);
    }


    public boolean isFollowing(String uid, String targetUid) {
        return this.mongoFollowingDao.isFollowing(uid, targetUid);
    }

    /**
     * 加关注
     * @param targetUser
     */
    public boolean follow(String targetUid,String uid) {
        try {
            this.mongoFollowingDao.follow(targetUid,uid);
            this.mongoBlogDao.updateTimeLineForFllowing(uid, targetUid);
            return true;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return false;
    }

    /**
     * 停止关注
     * @param targetUser
     */
    public boolean stopFollowing(String targetUid,String uid) {
        try {
            this.mongoFollowingDao.stopFollowing(targetUid,uid);
            this.mongoBlogDao.delTimeLineForFllowing(uid, targetUid);
            return true;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return false;
    }

}
