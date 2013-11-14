package com.xiaozhi.blog.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.stereotype.Repository;

import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.utils.MentionUtil;
import com.xiaozhi.blog.vo.Comment;
import com.xiaozhi.blog.vo.CommentShow;
import com.xiaozhi.blog.vo.Post;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.User;
import com.xiaozhi.blog.vo.WebPost;


@Repository
public class MongoCommentDao {

private static Log logger = LogFactory.getLog(MongoCommentDao.class);

    private  RedisAtomicLong commentIdCounter;

    @Autowired
    private MongoOperations mongoTemplate;

    @Autowired
    private  StringRedisTemplate template;

    @Autowired
    private MongoUserDao mongoUserDao;

    @Autowired
    private MongoBlogDao blogDao;

    @Autowired
    private MentionUtil mentionUtil;


    @PostConstruct
    public void init () {
        commentIdCounter = new RedisAtomicLong(KeyUtils.globalPid(), template.getConnectionFactory());
    }

    /**
     * 对微博进行评论
     * @param pid
     * @param comment
     * @param blogOwner 微博对应的拥有者id
     * @return
     */
    public boolean addComment(final String pid,final Comment comment,final String blogOwner,boolean isReplay) {
        final String cid = String.valueOf(commentIdCounter.incrementAndGet());
        comment.setCommentId(cid);
        if(!isReplay)comment.setTargetBlogId(pid);//评论目标为微博
        this.handleMentions(comment);//处理提及@用户的评论

        try {
        	this.mongoTemplate.save(comment);
        	this.mongoTemplate.updateFirst(new Query(Criteria.where("pid").is(pid)),
            		new Update().inc("commentNum", 1),Post.class);///对评论数进行+1操作
		} catch (Exception e) {
			logger.error("==================> addComment mongodb error :"+e.toString());
		  return false;
		}


        boolean result = (Boolean) template.execute(new RedisCallback<Object>() {
             @Override
             public Object doInRedis(RedisConnection connection)throws DataAccessException {
                try {
                     connection.multi();//事务开启
                     connection.lPush(KeyUtils.commentByBlog(pid).getBytes(), cid.getBytes());//加入微薄评论列表
                     connection.lPush(KeyUtils.postComments(comment.getUid()).getBytes(), cid.getBytes());//发出评论列表
                     //如果不是自己给自己评论则加入收到评论列表
                     if(!comment.getUid().equals(blogOwner))connection.lPush(KeyUtils.receiveComments(blogOwner).getBytes(), cid.getBytes());
                     //connection.hIncrBy(KeyUtils.post(pid).getBytes(), "commentNum".getBytes(), 1);//对评论数进行+1操作
                     connection.exec();
                     return true;
                } catch (Exception e) {
                	/**callback*/
        			mongoTemplate.remove(new Query(Criteria.where("commentId").is(comment.getCommentId())),Comment.class);//删除评论对象
        			mongoTemplate.updateFirst(new Query(Criteria.where("pid").is(Integer.valueOf(pid))),
                    		new Update().inc("commentNum", -1),Post.class);///对评论数进行-1操作
                    logger.error("==================> addComment error :"+e.toString());
                }finally{
                    connection.close();
                }
                return false;
              }
            });

        return result;
    }


    /**
     * 删除自己发出的评论的残余
     * @param uid
     * @param commentId
     * @return
     */
    public boolean delCommentForNoBlog(final String uid,final String commentId) {
    	final Comment comment;
    	try {
    		comment = this.mongoTemplate.findAndRemove(new Query(Criteria.where("commentId").is(commentId)),Comment.class);//删除评论对象
		} catch (Exception e) {
			return false;
		}


        boolean result = (Boolean) template.execute(new RedisCallback<Object>() {
             @Override
             public Object doInRedis(RedisConnection connection)throws DataAccessException {
                try {
                     connection.multi();//事务开启
                     connection.lRem(KeyUtils.postComments(uid).getBytes(), 0, commentId.getBytes());//删除发出的评论
                     connection.exec();
                     return true;
                } catch (Exception e) {
                	//call back
                	mongoTemplate.save(comment);
                    logger.error("==================> delCommentForNoBlog error :"+e.toString());
                }finally{
                    connection.close();
                }
                return false;
              }
            });

        return result;
    }

    /**
     * 删除微博评论
     * @param name
     * @param userid
     * @return
     */
    public boolean removeComment(final String pid,final String commentId,final String uid,final String blogOwner){
    	final Comment comment;
    	try {
    		comment = this.mongoTemplate.findAndRemove(new Query(Criteria.where("commentId").is(commentId)),Comment.class);//删除评论对象
    		this.mongoTemplate.updateFirst(new Query(Criteria.where("pid").is(pid)),new Update().inc("commentNum", -1),Post.class);//对评论数进行-1操作
		} catch (Exception e) {
			return false;
		}


        boolean result = (Boolean) template.execute(new RedisCallback<Object>() {
             @Override
             public Object doInRedis(RedisConnection connection)throws DataAccessException {
                try {
                     connection.multi();//事务开启
                     connection.lRem(KeyUtils.commentByBlog(pid).getBytes(), 0, commentId.getBytes());//删除微薄对应评论Id
                     connection.lRem(KeyUtils.postComments(uid).getBytes(), 0, commentId.getBytes());//删除发出的评论
                     if(!uid.equals(blogOwner))connection.lRem(KeyUtils.receiveComments(blogOwner).getBytes(), 0, commentId.getBytes());//删除收到的评论
                     connection.exec();
                     return true;
                } catch (Exception e) {
                	//call back
                	mongoTemplate.save(comment);
                	mongoTemplate.updateFirst(new Query(Criteria.where("pid").is(pid)),new Update().inc("commentNum", 1),Post.class);//对评论数进行+1操作
                    logger.error("==================> removeComment error :"+e.toString());
                }finally{
                    connection.close();
                }
                return false;
              }
            });

        return result;
    }


    /**
     * 获得一条微博的评论列表
     * @param pid
     * @param userid
     * @return
     */
    public List<CommentShow> getCommentListByPid(String pid,final String userid){

    	List<String> keys = this.template.opsForList().range(KeyUtils.commentByBlog(pid), 0,-1);
    	Query query = new Query(Criteria.where("commentId").in(keys.toArray()));
    	//query.sort().on("time",Order.DESCENDING);//按时间倒序
    	query.with(new Sort(Sort.Direction.DESC,"time"));//按时间倒序
    	
    	List<Comment> comments = this.mongoTemplate.find(query,Comment.class);
    	List<CommentShow> commentShows = new ArrayList<CommentShow>();
    	List<String> uids = new ArrayList<String>();//定义当前微博评论所有用户id集合
    	for(Comment comment :comments){
    		CommentShow commentShow = new CommentShow();
            commentShow.setContent(comment.getContent());
            commentShow.setTime(comment.getTime());
            commentShow.setCommentId(Integer.valueOf(comment.getCommentId()));
            commentShow.setUid(comment.getUid());
            commentShow.setCanDelete(comment.getUid().equals(userid)?true:false);
            commentShow.initTimeShow();//初始化时间显示
            uids.add(comment.getUid());
            commentShows.add(commentShow);

    	}

    	Map<String, User> userMap = this.mongoUserDao.getUsersByIds(uids);
    	for(CommentShow comment: commentShows){
    		comment.setUser(userMap.get(comment.getUid()));
    	}


        return commentShows;
    }


    /**
     * 获得用户的评论列表
     * @param uid
     * @return
     */
    public List<CommentShow> getCommentList(final String uid,List<String> keys,Range range){

    	List<String> pagekeysList=keys.subList(range.being, (range.end+1)>keys.size()?keys.size():(range.end+1));
    	List<Comment> comments = this.mongoTemplate.find(new Query(Criteria.where("commentId").in(pagekeysList.toArray())),Comment.class);
    	Map<String,CommentShow> commentShows = new ConcurrentHashMap<String,CommentShow>();

    	List<CommentShow> returnShow = new ArrayList<CommentShow>();

    	List<String> pids= new ArrayList<String>();
    	List<String> uids = new ArrayList<String>();//定义当前页所有用户id集合

    	for(Comment comment :comments){
    		CommentShow commentShow = new CommentShow();
            commentShow.setContent(comment.getContent());
            commentShow.setTime(comment.getTime());
            commentShow.setCommentId(Integer.valueOf(comment.getCommentId()));
            commentShow.setTargetBlogId(comment.getTargetBlogId());
            commentShow.setUid(comment.getUid());
            commentShow.setCanDelete(comment.getUid().equals(uid)?true:false);
            commentShow.initTimeShow();//初始化时间显示
            commentShows.put(comment.getCommentId(),commentShow);

            uids.add(comment.getUid());
            pids.add(comment.getTargetBlogId());
    	}

    	//将用户信息和对应微博添加进去
        Map<String, User> userMap = this.mongoUserDao.getUsersByIds(uids);
        Map<String,WebPost> websMap = this.blogDao.convertrelayPidsToPosts(pids);

        for(String key :pagekeysList){
        	CommentShow commentShow = new CommentShow();
        	commentShow.setCommentId(Integer.valueOf(key));
        	CommentShow show=commentShows.get(key);
            if(show!=null){
            	show.setUser(userMap.get(show.getUid()));
            	show.setWebPost(websMap.get(show.getTargetBlogId()));
            	returnShow.add(show);
            }else{
            	returnShow.add(commentShow);
            }

        }

        return returnShow;
    }


    /**
     * 用户的评论数
     * @param uid
     * @return
     */
    public List<String> getCommentListNum(String uid,String key){

    	return this.template.opsForList().range(key, 0,-1);
    }


    /**
     * 处理提及@用户的事宜
     * @param post
     * @param pid
     */
    private void handleMentions(Comment comment) {
        // find mentions
        List<String> mentions = mentionUtil.findMentions(comment.getContent());
        comment.setContent(mentionUtil.replaceMentions(comment.getContent()));

        Map<String, User> map = this.mongoUserDao.getUsersByNickNames(mentions);

        for (String mention : mentions) {
            User user = map.get(mention);
            if (user != null) {
                commentmentions(user.getId()).addFirst(comment.getCommentId().toString());
            }
        }
    }

    private RedisList<String> commentmentions(String uid) {
        return new DefaultRedisList<String>(KeyUtils.commentmentions(uid), template);
    }
}
