
package com.xiaozhi.blog.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;

import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.DefaultRedisSet;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.data.redis.support.collections.RedisSet;
import org.springframework.stereotype.Component;

import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.utils.MentionUtil;
import com.xiaozhi.blog.utils.SortList;
import com.xiaozhi.blog.vo.Page;
import com.xiaozhi.blog.vo.Post;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.Video;
import com.xiaozhi.blog.vo.WebPost;



/**
 * Twitter-clone on top of Redis.
 *
 * @author Costin Leau
 */
@Component
public class BlogDao {

    private static Log logger = LogFactory.getLog(BlogDao.class);

    @Autowired
    private  StringRedisTemplate template;
    @Autowired
    private RetwisRepository repository;

    @Autowired
    private MentionUtil mentionUtil;

    //global timeline
    private RedisList<String> timeline;

    private final HashMapper<Post, String, String> postMapper = new DecoratingStringHashMapper<Post>(new JacksonHashMapper<Post>(Post.class) {
    });


    @PostConstruct
    public void init () {
        timeline = new DefaultRedisList<String>(KeyUtils.timeline(), template);
    }


    /**
     * 为收到或者发出评论获取微博信息
     * @param pid
     * @param isAddUser
     * @return
     */
    public WebPost getBlogForCommentINfo(String pid,boolean isAddUser){
        Post post = postMapper.fromHash(post(pid));
        if(post==null || post.getUid()==null){
            WebPost wPost = new WebPost();
            wPost.setPid(Integer.valueOf(pid));
            return  wPost;
        }
        WebPost wPost = new WebPost(post);

        wPost.setPid(Integer.valueOf(pid));
        if(isAddUser)wPost.setUser(repository.getUserById(post.getUid()));
        wPost.setContent(mentionUtil.replaceMentions(post.getContent()));
        return wPost;
    }


    /**
     * 根据微薄id获取微薄信息(不包括转发)
     * @param pid
     * @return
     */
    public WebPost getBlogById(String pid,boolean isreplaceName){
        Post post = postMapper.fromHash(post(pid));
        if(post==null || post.getUid()==null){
            WebPost wPost = new WebPost();
            wPost.setPid(Integer.valueOf(pid));
            return  wPost;
        }
        WebPost wPost = new WebPost(post);

        wPost.setPid(Integer.valueOf(pid));
        wPost.setUser(repository.getUserById(post.getUid()));
        if(isreplaceName){
            wPost.setContent(mentionUtil.replaceMentions(post.getContent()));
        }else{
            wPost.setContent(post.getContent());
        }
//            try {
//                if(post.getVideoJson()!=null)wPost.setVideo(new ObjectMapper().readValue(post.getVideoJson(),Video.class));
//                if(post.getPageJson()!=null)wPost.setPage(new ObjectMapper().readValue(post.getPageJson(),Page.class));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        return wPost;
    }



    /**
     * 根据微薄id获取微薄信息(包括转发)
     * @param pid
     * @return
     */
    public WebPost getPost(String pid) {
        return convertPost(pid, post(pid));
    }

    /**
     * 获取当前用户发布了的微薄列表
     * @param uid
     * @param range
     * @return
     */
    public List<WebPost> getPosts(String uid, Range range) {
        return convertPidsToPosts(KeyUtils.posts(uid), range);
    }

    /**
     *  获取当前用户发布了的微薄总数
     * @param uid
     * @return
     */
    public int getPostsNum(String uid){
        int num = template.opsForList().size(KeyUtils.posts(uid)).intValue();
        return num;
    }

    /**
     * 获得自己的timeline(包括关注了的人发的微搏)
     * @param uid
     * @param range
     * @return
     */
    public List<WebPost> getTimeline(String uid, Range range) {
        return convertPidsToPosts(KeyUtils.timeline(uid), range);
    }

    /**
     *  获得自己的timeline总数(包括关注了的人发的微搏)
     * @param uid
     * @return
     */
    public int getTimelineNum(String uid){

        return template.opsForList().size(KeyUtils.timeline(uid)).intValue();
    }



    /**
     * 获得微薄提及列表
     * @param uid
     * @param range
     * @return
     */
    public List<WebPost> getMentions(String uid, Range range) {
        return convertPidsToPosts(KeyUtils.mentions(uid), range);
    }


    public int getMentionsNum(String uid){

        return template.opsForList().size(KeyUtils.mentions(uid)).intValue();
    }


    /**
     * 获取所有微博
     * @param range
     * @return
     */
    public List<WebPost> getAlltimeline(Range range) {
        return convertPidsToPosts(KeyUtils.timeline(), range);
    }

    /**
     * 获取所有微博数
     * @param uid
     * @return
     */
    public int getAllTimelineNum(){
        return template.opsForList().size(KeyUtils.timeline()).intValue();
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
     * 获取收藏列表
     * @param uid
     * @param range
     * @return
     */
    public List<WebPost> getCollectBlogs(String uid, Range range) {
        List<WebPost> list = convertPidsToPosts(KeyUtils.collect(uid), range);
        return list;
    }

    /**
     * 获取收藏微博数
     * @param uid
     * @return
     */
    public int getCollectBlogsNum(String uid){

        return template.opsForList().size(KeyUtils.collect(uid)).intValue();
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
        post(pid).putAll(postMapper.toHash(p));
        if(isForword){
            template.opsForHash().increment(KeyUtils.post(p.getTransmitid()), "transmitNum", 1);//对微博转发数+1操作
        }

        //add links
        posts(uid).addFirst(pid);//自己发的微薄列表
        timeline.addFirst(pid);//总时间线
        timeline(uid).addFirst(pid);//加入自己可见的时间线(包括能看到你追随的人发的微薄)
        template.opsForHash().increment(KeyUtils.uid(uid), "blogNum", 1);//对自己微博数+1操作


        //update followers 推模式更新粉丝time line
        for (String follower : followers(uid)) {
            timeline(follower).addFirst(pid);
        }

        return pid;
    }


    /**
     * 删除自己的微薄
     * @param username
     * @param pid
     */
    public boolean removeBlogByMe(final String uid, final String pid) {

        boolean result = (Boolean) template.execute(new RedisCallback<Object>() {
             @Override
             public Object doInRedis(RedisConnection connection)throws DataAccessException {
                try {
                     connection.multi();//事务开启
                     connection.lRem(KeyUtils.posts(uid).getBytes(), 0, pid.getBytes());
                     connection.lRem(KeyUtils.timeline(uid).getBytes(), 0, pid.getBytes());
                     connection.lRem(KeyUtils.timeline().getBytes(), 0, pid.getBytes());
                     connection.del(KeyUtils.post(pid).getBytes());
                     connection.del(KeyUtils.commentByBlog(pid).getBytes());/**删除微薄评论列表(不做发出、收到评论的列表和评论对象本身删除动作)*/
                     connection.hIncrBy(KeyUtils.uid(uid).getBytes(), "blogNum".getBytes(), -1);
                     connection.exec();
                     return true;
                } catch (Exception e) {
                    logger.error("==================> removeBlogByMe error :"+e.toString());
                }finally{
                    connection.close();
                }
                return false;
              }
            });

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
    private List<WebPost> convertPidsToPosts(String key, Range range) {
        String pid = "pid:*->";
        final String pidKey = "#";
        final String uid = "uid";
        final String content = "content";
        final String replyPid = "replyPid";
        final String replyUid = "replyUid";
        final String time = "time";
        final String commentNum = "commentNum";
        final String transmitNum = "transmitNum";
        final String transmitid = "transmitid";
        final String videoJson = "videoJson";
        final String pageJson = "pageJson";
        final String pic = "pic";


        SortQuery<String> query = SortQueryBuilder.sort(key).noSort()
        .get(pidKey)
        .get(pid + uid)
        .get(pid + content)
        .get(pid + replyPid)
        .get(pid + replyUid)
        .get(pid + time)
        .get(pid + commentNum)
        .get(pid + transmitNum)
        .get(pid + transmitid)
        .get(pid + videoJson)
        .get(pid + pageJson)
        .get(pid + pic)
        .limit(range.being, range.end-range.being+1).build();

        BulkMapper<WebPost, String> hm = new BulkMapper<WebPost, String>() {
            public WebPost mapBulk(List<String> bulk) {
                Map<String, String> map = new LinkedHashMap<String, String>();
                //Iterator<String> iterator = bulk.iterator();

                String pid = bulk.get(0);
                map.put(uid, bulk.get(1));
                map.put(content, bulk.get(2));
                map.put(replyPid, bulk.get(3));
                map.put(replyUid, bulk.get(4));
                map.put(time, bulk.get(5));
                map.put(commentNum, bulk.get(6));
                map.put(transmitNum, bulk.get(7)==null?"0":bulk.get(7));
                map.put(transmitid, bulk.get(8));
                map.put(videoJson, bulk.get(9)==null?null:bulk.get(9));
                map.put(pageJson, bulk.get(10)==null?null:bulk.get(10));
                map.put(pic, bulk.get(11)==null?"":bulk.get(11));
                return convertPost(pid, map);
            }
        };
        List<WebPost> sort = template.sort(query, hm);
        return sort;
    }



    private WebPost convertPost(String pid, Map<String,String> hash) {
        Post post = postMapper.fromHash(hash);

        if(post==null || post.getUid()==null){
            WebPost wPost = new WebPost();
            wPost.setPid(Integer.valueOf(pid));
            return  wPost;
        }
        WebPost wPost = new WebPost(post);
        if(null!=post.getTransmitid() && !"".equals(post.getTransmitid())){//转发微薄
            wPost.setWebPost(this.getBlogById(post.getTransmitid(),true));
        }

        wPost.setPid(Integer.valueOf(pid));
        wPost.setUser(repository.getUserById(post.getUid()));
        wPost.setContent(mentionUtil.replaceMentions(post.getContent()));

//        try {
//            if(post.getVideoJson()!=null)wPost.setVideo(new ObjectMapper().readValue(post.getVideoJson(),Video.class));
//            if(post.getPageJson()!=null)wPost.setPage(new ObjectMapper().readValue(post.getPageJson(),Page.class));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return wPost;
    }


    /**
     * 处理提及自己的事宜
     * @param post
     * @param pid
     */
    private void handleMentions(Post post) {
        // find mentions
        Collection<String> mentions = mentionUtil.findMentions(post.getContent());
        if(mentions.isEmpty()&& post.getTransmitid()!=null){
            Post post1 = postMapper.fromHash(post(post.getTransmitid()));
            mentions = mentionUtil.findMentions(post1.getContent());
        }

        for (String mention : mentions) {
            if(logger.isDebugEnabled()){
                logger.debug("********************** mention :"+mention);
            }
            String uid = repository.findUidByNickname(mention);
            if(logger.isDebugEnabled()){
                logger.debug("********************** uid :"+uid);
            }
            if (uid != null) {
                mentions(uid).addFirst(post.getPid());
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

    private RedisMap<String, String> post(String pid) {
        return new DefaultRedisMap<String, String>(KeyUtils.post(pid), template);
    }

    private RedisList<String> posts(String uid) {
        return new DefaultRedisList<String>(KeyUtils.posts(uid), template);
    }

    private RedisSet<String> followers(String uid) {
        return new DefaultRedisSet<String>(KeyUtils.followers(uid), template);
    }
}