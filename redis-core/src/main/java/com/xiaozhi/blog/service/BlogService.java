package com.xiaozhi.blog.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.xiaozhi.blog.redis.BlogDao;
import com.xiaozhi.blog.mongo.MongoBlogDao;
import com.xiaozhi.blog.redis.BlogIndexDao;
import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.utils.MentionUtil;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.WebPost;

import solr.index.BlogIndexData;


@Service
public class BlogService {


    private static Log logger = LogFactory.getLog(BlogService.class);
    @Autowired
    private MongoBlogDao mongoBlogDao;
    @Autowired
    private BlogIndexDao blogIndexDao;
    @Autowired
    private MentionUtil mentionUtil;



    /**
     * 根据微薄id获取微搏信息(不包括转发)
     * @param pid
     * @return
     */
    public WebPost getBlogById(String pid){

        return this.mongoBlogDao.getBlogById(pid);
    }


    /**
     * 根据微薄id获取微搏信息(包括转发)
     * @param pid
     * @return
     */
    public WebPost getBlogWithForwardById(String pid) {
        return this.mongoBlogDao.getBlogWithForwardById(pid);
    }


    /**
     * 分页获取当前用户发布了的微搏列表
     * @param uid
     * @param range
     * @return
     */
    public ListPage<WebPost> getPostsByPage(String uid, Integer page,Integer pagesize) {

    	List<String> pids= this.mongoBlogDao.getPostsIdList(uid,KeyUtils.posts(uid));
        int firstResult = (page-1)*pagesize;
        int lastResult = firstResult+pagesize-1;
        int allResults = pids.size();
        List<WebPost> users=this.mongoBlogDao.getPosts(pids, new Range(firstResult,lastResult));
        return new ListPage<WebPost>(users, firstResult, lastResult, allResults);
    }


    /**
     * 分页获得自己的timeline(包括关注了的人发的微搏)
     * @param uid
     * @param range
     * @return
     */
    public ListPage<WebPost> getTimelineByPage(String uid, Integer page,Integer pagesize) {
    	List<String> pids= this.mongoBlogDao.getPostsIdList(uid,KeyUtils.timeline(uid));
        int firstResult = (page-1)*pagesize;
        int lastResult = firstResult+pagesize-1;
        int allResults = pids.size();
        List<WebPost> blogs=this.mongoBlogDao.getPosts(pids, new Range(firstResult,lastResult));
        return new ListPage<WebPost>(blogs, firstResult, lastResult, allResults);
    }


    /**
     * 分页获得提及自己的微博
     * @param uid
     * @param page
     * @param pagesize
     * @return
     */
    public ListPage<WebPost> getMentions(String uid, Integer page,Integer pagesize) {
    	List<String> pids= this.mongoBlogDao.getPostsIdList(uid,KeyUtils.mentions(uid));
        int firstResult = (page-1)*pagesize;
        int lastResult = firstResult+pagesize-1;
        List<WebPost> blogs=this.mongoBlogDao.getPosts(pids, new Range(firstResult,lastResult));
        int allResults = pids.size();
        return new ListPage<WebPost>(blogs, firstResult, lastResult, allResults);
    }

    /**
     * 发布微搏
     * @param uid
     * @param post
     */
    public WebPost post(String uid, WebPost post ,boolean isForword) {
        try {
            post.setContent(this.mentionUtil.replayFaceImages(post.getContent()));
            String pid = this.mongoBlogDao.post(uid, post,isForword);
            return this.getBlogWithForwardById(pid);
        } catch (Exception e) {
            logger.error("=================>post is error :"+e.toString(),e);
        }
        return null;
    }


    /**
     * 删除自己的微搏
     * @param username
     * @param pid
     */
    public boolean removeBlogByMe(String uid, String pid) {
        return this.mongoBlogDao.removeBlogByMe(uid, pid);
    }


    /**
     * 别人删除你的微搏残余
     * @param uid
     * @param pid
     * @return
     */
    public boolean removeBlogByOther(String uid, String pid) {
        return this.mongoBlogDao.removeBlogByOther(uid, pid);
    }

    /**
     * 删除提及我的微博残余
     * @param uid
     * @param pid
     * @return
     */
    public boolean removeMention(String uid, String pid){
    	return this.mongoBlogDao.removeMention(uid, pid);
    }

    /**
     * 分页获取所有微博为建索引准备
     * @param page
     * @param pagesize
     * @return
     */
    public ListPage<BlogIndexData> timeline(Integer page,Integer pagesize) {
        int firstResult = (page-1)*pagesize;
        int lastResult = firstResult+pagesize-1;
        int allResults = this.blogIndexDao.getAllTimelineNum();
        List<BlogIndexData> blogs=this.blogIndexDao.getAlltimeline(new Range(firstResult,lastResult));
        return new ListPage<BlogIndexData>(blogs, firstResult, lastResult, allResults);
    }

}
