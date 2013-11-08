package com.xiaozhi.blog.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.xiaozhi.blog.redis.BlogDao;
import com.xiaozhi.blog.mongo.MongoBlogDao;
import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.WebPost;

@Service
public class CollectBlogService {

private static Log logger = LogFactory.getLog(CollectBlogService.class);

	@Autowired
	private  MongoBlogDao mongoBlogDao;


	/**
	 * 加入收藏
	 * @param uid
	 * @param pid
	 * @return
	 */
	public boolean collectBlog(String uid,String pid){

		return this.mongoBlogDao.collectBlog(uid, pid);
	}

	/**
	 * 删除收藏
	 * @param uid
	 * @param pid
	 * @return
	 */
	public boolean removeCollectBlog(String uid, String pid) {

		return this.mongoBlogDao.removeCollectBlog(uid, pid);
	}


	/**
	 * 分页获取收藏列表
	 * @param uid
	 * @param page
	 * @param pagesize
	 * @return
	 */
	public ListPage<WebPost> getCollectsByPage(String uid, Integer page,Integer pagesize) {
		List<String> pids= this.mongoBlogDao.getPostsIdList(uid,KeyUtils.collect(uid));
		int firstResult = (page-1)*pagesize;
		int lastResult = firstResult+pagesize-1;
		int allResults = pids.size();
		List<WebPost> users=this.mongoBlogDao.getPosts(pids, new Range(firstResult,lastResult));
		return new ListPage<WebPost>(users, firstResult, lastResult, allResults);
	}

}
