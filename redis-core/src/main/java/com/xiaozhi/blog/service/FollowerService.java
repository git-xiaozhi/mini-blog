package com.xiaozhi.blog.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaozhi.blog.mongo.MongoFollowerDao;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.User;


/**
 * 粉丝管理
 * @author Administrator
 *
 */
@Service
public class FollowerService {

	private static Log logger = LogFactory.getLog(FollowerService.class);
    @Autowired
    private MongoFollowerDao mongoFollowerDao;



    /**
     * 获取第一页9个粉丝
     * @param uid
     * @return
     */
	public List<User> getFollowers(String uid) {
		return this.mongoFollowerDao.getFollowerByPage(uid,new Range(1));
	}


	/**
	 * 分页获取粉丝
	 * @param uid
	 * @param range
	 * @return
	 */
	public ListPage<User> getFollowersByPage(String uid, Integer page,Integer pagesize) {

		int firstResult = (page-1)*pagesize;
		int lastResult = (page-1)*pagesize+pagesize-1;
		int allResults = this.mongoFollowerDao.getFollowersNum(uid);
		List<User> users=this.mongoFollowerDao.getFollowerByPage(uid, new Range(firstResult,lastResult));
		return new ListPage<User>(users, firstResult, lastResult, allResults);
	}


	/**
	 * 我关注的人关注他的人
	 * @param uid
	 * @param targetUid
	 * @param page
	 * @param pagesize
	 * @return
	 */
	public ListPage<User> alsoFollowed(String uid, String targetUid,Integer page,Integer pagesize) {
		return this.mongoFollowerDao.alsoFollowed(uid, targetUid, page, pagesize);
	}


	/**
	 * 我和他共同关注的人
	 * @param uid
	 * @param targetUid
	 * @param page
	 * @param pagesize
	 * @return
	 */
	public ListPage<User> commonFollowers(String uid, String targetUid,Integer page,Integer pagesize) {
		return this.mongoFollowerDao.commonFollowers(uid, targetUid, page, pagesize);
	}

}
