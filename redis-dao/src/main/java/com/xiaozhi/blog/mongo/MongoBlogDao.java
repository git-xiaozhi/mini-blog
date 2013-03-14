
package com.xiaozhi.blog.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.stereotype.Repository;

import com.xiaozhi.blog.utils.Collections3;
import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.utils.MentionUtil;
import com.xiaozhi.blog.utils.SortList;
import com.xiaozhi.blog.vo.Post;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.User;
import com.xiaozhi.blog.vo.WebPost;



/**
 * Twitter-clone on top of Redis.
 *
 * @author Costin Leau
 */
@Repository
public class MongoBlogDao {

    private static Log logger = LogFactory.getLog(MongoBlogDao.class);

    @Autowired
    private MongoOperations mongoTemplate;

    @Autowired
    private  StringRedisTemplate template;
    @Autowired
    private MongoUserDao mongoUserDao;

    @Autowired
    private MentionUtil mentionUtil;


    /**
     * 根据微博id获取微博信息(不包括转发)
     * @param pid
     * @return
     */
    public WebPost getBlogById(String pid){

        Post post = this.mongoTemplate.findOne(new Query(Criteria.where("pid").is(pid)),
        		Post.class);

        if(post==null || post.getUid()==null){
            WebPost wPost = new WebPost();
            wPost.setPid(Integer.valueOf(pid));
            return  wPost;
        }
        WebPost wPost = new WebPost(post);
        wPost.setUser(mongoUserDao.getUserById(post.getUid()));
        return wPost;
    }



    /**
     * 根据微薄id获取微博信息(包括转发)
     * @param pid
     * @return
     */
    public WebPost getBlogWithForwardById(String pid) {
    	Post post = this.mongoTemplate.findOne(new Query(Criteria.where("pid").is(pid)),
        		Post.class);
        return convertPost(post);
    }

    /**
     * 获取微博列表
     * @param uid
     * @param range
     * @return
     */
    public List<WebPost> getPosts(List<String> keys, Range range) {
        return convertPidsToPosts(keys, range);
    }

    /**
     * 获取微博id列表
     * @param uid
     * @return
     */
    public List<String> getPostsIdList(String uid,String key){
        return this.template.opsForList().range(key, 0,-1);
    }


    /**
     * 加入收藏
     * @param uid
     * @param pid
     * @return
     */
    public boolean collectBlog(String uid,String pid){
        try {
            template.opsForList().remove(KeyUtils.collect(uid), 0, pid);
            collect(uid).addFirst(pid);//加入收藏
            return true;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    /**
     * 删除收藏
     * @param uid
     * @param pid
     * @return
     */
    public boolean removeCollectBlog(String uid, String pid) {
        try {
            template.opsForList().remove(KeyUtils.collect(uid), 0, pid);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }


    /**
     * 发布微搏
     * @param username
     * @param post
     */
    public String post(String uid, WebPost post ,boolean isForword) {
        Post p = post.asPost();
        p.setUid(uid);
        String pid = String.valueOf(new RedisAtomicLong(KeyUtils.globalPid(), template.getConnectionFactory()).incrementAndGet());
        p.setPid(pid);
        handleMentions(p);//处理提及的人和提及人的链接

        //add post
        try {
            this.mongoTemplate.save(p);
            this.mongoTemplate.updateFirst(new Query(Criteria.where("id").is(uid)),
            		new Update().inc("blogNum", 1),User.class);//对自己微博数+1操作
            if(isForword){
                this.mongoTemplate.updateFirst(new Query(Criteria.where("pid").is(p.getTransmitid())),
                		new Update().inc("transmitNum", 1),Post.class);//对微博转发数+1操作
            }

		} catch (Exception e) {
			logger.error("==================> post mogodb error :"+e.toString());
			return null;
			// TODO: handle exception
		}

        //add links
        try {
        	posts(uid).addFirst(pid);//自己发的微薄列表
            timeline(uid).addFirst(pid);//加入自己可见的时间线(包括能看到你追随的人发的微薄)

		} catch (Exception e) {
			//call back
			this.mongoTemplate.remove(new Query(Criteria.where("pid").is(pid)),Post.class);
            this.mongoTemplate.updateFirst(new Query(Criteria.where("id").is(uid)),
            		new Update().inc("blogNum", -1),User.class);//对自己微博数-1操作
	        if(isForword){
	            this.mongoTemplate.updateFirst(new Query(Criteria.where("pid").is(p.getTransmitid())),
	            		new Update().inc("transmitNum", -1),Post.class);//对微博转发数-1操作
	        }
	        logger.error("==================> post redis error :"+e.toString());
			return null;
		}



        //update followers 推模式更新粉丝time line
        for (String follower : followers(uid)) {
            timeline(follower).addFirst(pid);
        }

        return pid;
    }


    /**
     * 删除自己的微博
     * @param username
     * @param pid
     */
    public boolean removeBlogByMe(final String uid, final String pid) {

    	final Post post;
    	try {
    		post = this.mongoTemplate.findAndRemove(new Query(Criteria.where("pid").is(pid)),Post.class);
    		//this.mongoTemplate.remove(new Query(Criteria.where("pid").is(pid)),Post.class);
		} catch (Exception e) {
			return false;
		}

		boolean result = false;
		try {
          result = (Boolean) template.execute(new RedisCallback<Object>() {
             @Override
             public Object doInRedis(RedisConnection connection)throws DataAccessException {
                try {
                     connection.multi();//事务开启
                     connection.lRem(KeyUtils.posts(uid).getBytes(), 0, pid.getBytes());
                     connection.lRem(KeyUtils.timeline(uid).getBytes(), 0, pid.getBytes());
                     connection.lRem(KeyUtils.timeline().getBytes(), 0, pid.getBytes());
                     //connection.del(KeyUtils.post(pid).getBytes());
                     connection.del(KeyUtils.commentByBlog(pid).getBytes());/**删除微博评论列表(不做发出、收到评论的列表和评论对象本身删除动作)*/
                     //connection.hIncrBy(KeyUtils.uid(uid).getBytes(), "blogNum".getBytes(), -1);
                     connection.exec();
                     return true;
                } catch (Exception e) {
                	//call back
                	mongoTemplate.save(post);
                    logger.error("==================> removeBlogByMe error :"+e.toString());
                }finally{
                    connection.close();
                }
                return false;
              }
            });
		} catch (Exception e) {
			// TODO: handle exception
		}
        return result;
    }


    /**
     * 别人删除你的微薄残余
     * @param uid
     * @param pid
     * @return
     */
    public boolean removeBlogByOther(String uid, String pid) {
        try {
            template.opsForList().remove(KeyUtils.timeline(uid), 0, pid);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    /**
     * 删除提及我的微博残余
     * @param uid
     * @param pid
     * @return
     */
    public boolean removeMention(String uid, String pid) {
        try {
        	mentions(uid).remove(pid);
            //template.opsForList().remove(KeyUtils.timeline(uid), 0, pid);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }


    /**
     * 关注的时候更新本人TimeLine
     * @param uid
     * @param targetId
     */
    public boolean updateTimeLineForFllowing(final String uid,String targetId){
       List<String> targetTimeLine =  posts(targetId).range(0, -1);
       List<String> utimeLine = timeline(uid).range(0, -1);
       utimeLine.addAll(targetTimeLine);

       final List<Long> newList = new  ArrayList<Long>();
       for(String pid:utimeLine){
           newList.add(Long.valueOf(pid));
       }

       SortList<Long> sortList = new SortList<Long>();
       sortList.sort(newList, "longValue", "Long","desc");//重新按pid排序

       boolean result = (Boolean) template.execute(new RedisCallback<Object>() {
           @Override
           public Object doInRedis(RedisConnection connection)throws DataAccessException {
              try {
                   connection.multi();//事务开启
                   connection.del(KeyUtils.timeline(uid).getBytes());
                   for(Long pid:newList){
                    connection.rPush(KeyUtils.timeline(uid).getBytes(), pid.toString().getBytes());
                   }
                   connection.exec();
                   return true;
              } catch (Exception e) {
                  logger.error("==================> updateTimeLineForFllowing error :"+e.toString());
              }finally{
                  connection.close();
              }
              return false;
            }
          });
       return result;
    }


    /**
     * 取消关注时候剔除被关注人微博信息
     * @param uid
     * @param targetId
     * @return
     */
    public boolean delTimeLineForFllowing(final String uid,String targetId){

        final List<String> targetTimeLine =  posts(targetId).range(0, -1);
        final List<String> utimeLine = timeline(uid).range(0, -1);


        boolean result = (Boolean) template.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)throws DataAccessException {
               try {
                    connection.multi();//事务开启
                    for(String pid:utimeLine){
                      if(targetTimeLine.contains(pid)){
                       connection.lRem(KeyUtils.timeline(uid).getBytes(), 0, pid.getBytes());
                      }
                    }
                    connection.exec();
                    return true;
               } catch (Exception e) {
                   logger.error("==================> updateTimeLineForFllowing error :"+e.toString());
               }finally{
                   connection.close();
               }
               return false;
             }
           });
        return result;
     }


    /**
     * 获取微薄列表
     */
    private List<WebPost> convertPidsToPosts(List<String> keys, Range range) {
    	if(keys==null || keys.isEmpty())return new ArrayList<WebPost>();
    	List<String> pagekeysList=keys.subList(range.being, (range.end+1)>keys.size()?keys.size():(range.end+1));
    	Query query = new Query(Criteria.where("pid").in(pagekeysList.toArray()));
    	//query.sort().on("pid",Order.DESCENDING);//按pid倒序
    	List<Post> posts = this.mongoTemplate.find(query,Post.class);

    	List<WebPost> webPosts= new ArrayList<WebPost>();
    	Map<String, Post> postMap= new ConcurrentHashMap<String, Post>();

    	List<String> uids = new ArrayList<String>();//定义当前页所有用户id集合
    	List<String>  relayPids = new ArrayList<String>();//定义当前页所有转发微博的转发id集合

        for(Post post :posts){
        	uids.add(post.getUid());
        	if(post.getTransmitid()!=null)relayPids.add(post.getTransmitid());
        	postMap.put(post.getPid(), post);
        }
        //获取转发微博集合
        Map<String,WebPost> subWebPosts = convertrelayPidsToPosts(relayPids);
        //获取当前页微博所属用户集合
        Map<String, User> userMap = this.mongoUserDao.getUsersByIds(uids);

        //将用户信息和转发微博添加进去
        for(String key :pagekeysList){
        	Post post = postMap.get(key);
        	WebPost webPost = new WebPost();
        	webPost.setPid(Integer.valueOf(key));
        	if(post!=null)webPost = convertPost(post,userMap.get(post.getUid()));
        	if(post!=null && post.getTransmitid()!=null){
        		webPost.setWebPost(subWebPosts.get(post.getTransmitid()));
        	}
        	webPosts.add(webPost);
        }

        return webPosts;
    }



    /**
     * 批量获得当前页的转发微博的微博内容。
     * @param keys
     * @param range
     * @return
     */
    public Map<String,WebPost> convertrelayPidsToPosts(List<String> keys) {
        if(null==keys || keys.isEmpty())return new ConcurrentHashMap<String, WebPost>();
    	List<Post> posts = this.mongoTemplate.find(new Query(Criteria.where("pid").in(keys.toArray())),Post.class);

    	Map<String,WebPost> webPosts= new ConcurrentHashMap<String, WebPost>();
    	List<String> uids = Collections3.extractToList(posts, "uid");//当前页所有用户id集合
        //将用户信息添加进去
        Map<String, User> userMap = this.mongoUserDao.getUsersByIds(uids);


        for(Post post :posts){
        	webPosts.put(post.getPid(),convertPost(post,userMap.get(post.getUid())));
        }

        return webPosts;
    }

    /**
     * 转换微博信息
     * @param post
     * @return
     */
    private WebPost convertPost(Post post,User user) {

        if(post==null || post.getUid()==null){
            WebPost wPost = new WebPost();
            wPost.setPid(Integer.valueOf(post.getPid()));
            return  wPost;
        }
        WebPost wPost = new WebPost(post);
        wPost.setPid(Integer.valueOf(post.getPid()));
        wPost.setUser(user);
        return wPost;
    }


    private WebPost convertPost(Post post) {

        if(post==null || post.getUid()==null){
            WebPost wPost = new WebPost();
            wPost.setPid(Integer.valueOf(post.getPid()));
            return  wPost;
        }
        WebPost wPost = new WebPost(post);
        wPost.setUser(this.mongoUserDao.getUserById(post.getUid()));
        if(null!=post.getTransmitid()){//转发微博
            wPost.setWebPost(this.getBlogById(post.getTransmitid()));
        }
        return wPost;
    }


    /**
     * 处理提及自己的事宜
     * @param post
     * @param pid
     */
    private void handleMentions(Post post) {
        // find mentions
        List<String> mentions = mentionUtil.findMentions(post.getContent());
        post.setContent(mentionUtil.replaceMentions(post.getContent()));//为提及的人加链接

        //将此微博加入提及人的被@列表中
        Map<String, User> map = this.mongoUserDao.getUsersByNickNames(mentions);

        for (String mention : mentions) {
            User user = map.get(mention);
            if (user != null) {
                mentions(user.getId()).addFirst(post.getPid());
            }
        }
    }



    // collections mapping the core data structures
    private RedisList<String> collect(String uid) {
        return new DefaultRedisList<String>(KeyUtils.collect(uid), template);
    }

    private RedisList<String> timeline(String uid) {
        return new DefaultRedisList<String>(KeyUtils.timeline(uid), template);
    }

    private RedisList<String> mentions(String uid) {
        return new DefaultRedisList<String>(KeyUtils.mentions(uid), template);
    }

    private RedisList<String> posts(String uid) {
        return new DefaultRedisList<String>(KeyUtils.posts(uid), template);
    }

    private RedisList<String> followers(String uid) {
        return new DefaultRedisList<String>(KeyUtils.followers(uid), template);
    }
}