package com.xiaozhi.blog.service.other.sina;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weibo4j.Timeline;
import weibo4j.model.Emotion;
import weibo4j.model.WeiboException;

import com.xiaozhi.blog.mongo.MongoUserDao;
import com.xiaozhi.blog.mongo.SinaBlogDao;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.vo.SinaAccessToken;
import com.xiaozhi.blog.vo.SinaPost;



@Service
public class SinaBlogService {

	private static Log logger = LogFactory.getLog(SinaBlogService.class);
	
	@Autowired
	private SinaBlogDao sinaBlogDao;
	@Autowired
	private BlogTime blogTime;
	@Autowired
	private Timeline timeline;
	
	@Autowired
	private  MongoUserDao mongoUserDao;
	
	
	
	public void setSinaBlogDao(SinaBlogDao sinaBlogDao) {
		this.sinaBlogDao = sinaBlogDao;
	}


	/**
	 * 保存定时微博
	 * @param post
	 * @return
	 * @throws WeiboException 
	 */
	public SinaPost saveSinaPost(SinaPost post) throws WeiboException{
		SinaAccessToken accessToken = mongoUserDao.getAccessTokenByUser(post.getUid());
		timeline.client.setToken(accessToken.getAccesstoken());
		Map<String, Emotion> map = blogTime.getEmotionsMap(timeline);
		SinaPost returnPost = this.sinaBlogDao.saveSinaPost(post);
		returnPost.setContent(blogTime.replayFaceImages(post.getContent(), map));
		return returnPost;
	}
	
	
	 /**
	  * 通过id获取一个定时微博
	  * @param id
	  * @return
	  */
	 public SinaPost getSinaPostById(long id){
		 return this.sinaBlogDao.getSinaPostById(id);
	 }
	
	
	/**
	 * 删除一个定时微博
	 * @param id
	 * @return
	 */
	public SinaPost deleteSinapost(long id){
		SinaPost post = this.sinaBlogDao.removeSinaPostById(id);
		return post;
	}
	
	/**
	 * 分页获取定时微博列表
	 * @param uid
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws WeiboException 
	 */
	public ListPage<SinaPost> getSinaBlogsByPage(String uid, Integer page,int pagesize) throws WeiboException {
		
		int firstResult = (page-1)*pagesize;
		int lastResult = firstResult+pagesize-1;
		int allResults = this.sinaBlogDao.getSinaPostCount(uid);
		List<SinaPost> posts=this.sinaBlogDao.findSinaPosts(uid, firstResult, pagesize);
		
		SinaAccessToken accessToken = mongoUserDao.getAccessTokenByUser(uid);
		timeline.client.setToken(accessToken.getAccesstoken());
		Map<String, Emotion> map = blogTime.getEmotionsMap(timeline);
		for(SinaPost post:posts){//替换新浪表情
			post.setContent(blogTime.replayFaceImages(post.getContent(), map));
		}
		
		return new ListPage<SinaPost>(posts, firstResult, lastResult, allResults);
	}
	
}
