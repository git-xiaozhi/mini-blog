package com.xiaozhi.blog.redis;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.stereotype.Component;

import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.utils.MentionUtil;
import com.xiaozhi.blog.vo.Comment;
import com.xiaozhi.blog.vo.CommentShow;
import com.xiaozhi.blog.vo.Range;


@Component
public class CommentDao {

private static Log logger = LogFactory.getLog(CommentDao.class);

    private  RedisAtomicLong commentIdCounter;

    @Autowired
    private  StringRedisTemplate template;

    @Autowired
    private RetwisRepository retwisRepository;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private MentionUtil mentionUtil;


    @PostConstruct
    public void init () {
        commentIdCounter = new RedisAtomicLong(KeyUtils.globalPid(), template.getConnectionFactory());
    }

    /**
     * 对微薄进行评论
     * @param pid
     * @param comment
     * @param blogOwner 微薄对应的拥有者id
     * @return
     */
    public boolean addComment(final String pid,final Comment comment,final String blogOwner,boolean isReplay) {
        final String cid = String.valueOf(commentIdCounter.incrementAndGet());
        comment.setCommentId(cid);

        this.handleMentions(comment);//处理提及@用户的评论

        final Map<byte[], byte[]> commentMap= new HashMap<byte[], byte[]>();
        commentMap.put("uid".getBytes(), comment.getUid().getBytes());
        commentMap.put("commentId".getBytes(), comment.getCommentId().toString().getBytes());
        if(!isReplay){
            commentMap.put("targetBlogId".getBytes(), pid.getBytes());//评论目标为微博
        }else{
            commentMap.put("targetCommentId".getBytes(), comment.getTargetCommentId().getBytes());//回复目标
        }

        commentMap.put("time".getBytes(), comment.getTime().getBytes());
        commentMap.put("content".getBytes(), comment.getContent().getBytes(Charset.forName("UTF-8")));

        boolean result = (Boolean) template.execute(new RedisCallback<Object>() {
             @Override
             public Object doInRedis(RedisConnection connection)throws DataAccessException {
                try {
                     connection.multi();//事务开启
                     connection.hMSet(KeyUtils.commentKey(cid).getBytes(), commentMap);//Hash形式存储评论
                     connection.lPush(KeyUtils.commentByBlog(pid).getBytes(), cid.getBytes());//加入微薄评论列表
                     connection.lPush(KeyUtils.postComments(comment.getUid()).getBytes(), cid.getBytes());//发出评论列表
                     //如果不是自己给自己评论则加入收到评论列表
                     if(!comment.getUid().equals(blogOwner))connection.lPush(KeyUtils.receiveComments(blogOwner).getBytes(), cid.getBytes());
                     connection.hIncrBy(KeyUtils.post(pid).getBytes(), "commentNum".getBytes(), 1);//对评论数进行+1操作
                     connection.exec();
                     return true;
                } catch (Exception e) {
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
     * 删除微薄评论
     * @param name
     * @param userid
     * @return
     */
    public boolean removeComment(final String pid,final String commentId,final String uid,final String blogOwner){

        boolean result = (Boolean) template.execute(new RedisCallback<Object>() {
             @Override
             public Object doInRedis(RedisConnection connection)throws DataAccessException {
                try {
                     connection.multi();//事务开启
                     connection.del(KeyUtils.commentKey(commentId).getBytes());//删除评论对象
                     connection.lRem(KeyUtils.commentByBlog(pid).getBytes(), 0, commentId.getBytes());//删除微薄对应评论Id
                     connection.lRem(KeyUtils.postComments(uid).getBytes(), 0, commentId.getBytes());//删除发出的评论
                     if(!uid.equals(blogOwner))connection.lRem(KeyUtils.receiveComments(blogOwner).getBytes(), 0, commentId.getBytes());//删除收到的评论
                     connection.hIncrBy(KeyUtils.post(pid).getBytes(), "commentNum".getBytes(), -1);//对评论数进行-1操作
                     connection.exec();
                     return true;
                } catch (Exception e) {
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

        String cidKey = "commentid:*->";
        SortQuery<String> query = SortQueryBuilder.sort(KeyUtils.commentByBlog(pid)).noSort()
        .get("#")
        .get(cidKey + "uid")
        .get(cidKey + "commentId")
        .get(cidKey + "targetBlogId")
        .get(cidKey + "time")
        .get(cidKey + "content")
        .limit(0,9).build();

        BulkMapper<CommentShow, String> hm = new BulkMapper<CommentShow, String>() {
            public CommentShow mapBulk(List<String> bulk) {
                CommentShow commentShow = new CommentShow();
                commentShow.setContent(mentionUtil.replaceMentions(bulk.get(5)));
                commentShow.setTime(bulk.get(4));
                commentShow.setCommentId(Integer.valueOf(bulk.get(2)));
                commentShow.setUser(retwisRepository.getUserById(bulk.get(1)));
                commentShow.setCanDelete(bulk.get(1).equals(userid)?true:false);
                commentShow.initTimeShow();//初始化时间显示
                return commentShow;
            }
        };

        List<CommentShow> comments = template.sort(query, hm);
        return comments;
    }



    /**
     * 获得用户收到的评论列表
     * @param uid
     * @return
     */
    public List<CommentShow> getCommentListByReceiveComment(final String uid,Range range){

        String cidKey = "commentid:*->";
        SortQuery<String> query = SortQueryBuilder.sort(KeyUtils.receiveComments(uid)).noSort()
        .get("#")
        .get(cidKey + "uid")
        .get(cidKey + "commentId")
        .get(cidKey + "targetBlogId")
        .get(cidKey + "time")
        .get(cidKey + "content")
        .limit(range.being, range.end-range.being+1).build();

        BulkMapper<CommentShow, String> hm = new BulkMapper<CommentShow, String>() {
            public CommentShow mapBulk(List<String> bulk) {
                CommentShow commentShow = new CommentShow();
                commentShow.setWebPost(blogDao.getBlogForCommentINfo(bulk.get(3), false));
                commentShow.setContent(mentionUtil.replaceMentions(bulk.get(5)));
                commentShow.setTime(bulk.get(4));
                commentShow.setCommentId(Integer.valueOf(bulk.get(2)));
                commentShow.setUser(retwisRepository.getUserById(bulk.get(1)));
                commentShow.setCanDelete(bulk.get(1).equals(uid)?true:false);
                commentShow.initTimeShow();//初始化时间显示
                return commentShow;
            }
        };

        List<CommentShow> comments = template.sort(query, hm);
        return comments;
    }


    /**
     * 获得用户收到的评论数
     * @param uid
     * @return
     */
    public int getCommentListByReceiveCommentNum(String uid){

        return template.opsForList().size(KeyUtils.receiveComments(uid)).intValue();
    }


    /**
     * 获得用户发出的评论列表
     * @param uid
     * @return
     */
    public List<CommentShow> getCommentListByPostComment(final String uid,Range range){

        String cidKey = "commentid:*->";
        SortQuery<String> query = SortQueryBuilder.sort(KeyUtils.postComments(uid)).noSort()
        .get("#")
        .get(cidKey + "uid")
        .get(cidKey + "commentId")
        .get(cidKey + "targetBlogId")
        .get(cidKey + "time")
        .get(cidKey + "content")
        .limit(range.being, range.end-range.being+1).build();

        BulkMapper<CommentShow, String> hm = new BulkMapper<CommentShow, String>() {
            public CommentShow mapBulk(List<String> bulk) {
                CommentShow commentShow = new CommentShow();
                commentShow.setWebPost(blogDao.getBlogForCommentINfo(bulk.get(3), true));
                commentShow.setContent(mentionUtil.replaceMentions(bulk.get(5)));
                commentShow.setTime(bulk.get(4));
                commentShow.setCommentId(Integer.valueOf(bulk.get(2)));
                commentShow.setUser(retwisRepository.getUserById(bulk.get(1)));
                commentShow.setCanDelete(bulk.get(1).equals(uid)?true:false);
                commentShow.initTimeShow();//初始化时间显示
                return commentShow;
            }
        };

        List<CommentShow> comments = template.sort(query, hm);
        return comments;
    }

    /**
     * 获得用户发出的评论数
     * @param uid
     * @return
     */
    public int getCommentListByPostCommentNum(String uid){

        return template.opsForList().size(KeyUtils.postComments(uid)).intValue();
    }


    /**
     * 提到我的评论列表
     * @param uid
     * @return
     */
    public List<CommentShow> getCommentListByMention(final String uid,Range range){

        String cidKey = "commentid:*->";
        SortQuery<String> query = SortQueryBuilder.sort(KeyUtils.commentmentions(uid)).noSort()
        .get("#")
        .get(cidKey + "uid")
        .get(cidKey + "commentId")
        .get(cidKey + "targetBlogId")
        .get(cidKey + "time")
        .get(cidKey + "content")
        .limit(range.being, range.end-range.being+1).build();

        BulkMapper<CommentShow, String> hm = new BulkMapper<CommentShow, String>() {
            public CommentShow mapBulk(List<String> bulk) {
                CommentShow commentShow = new CommentShow();
                commentShow.setWebPost(blogDao.getBlogForCommentINfo(bulk.get(3), true));
                commentShow.setContent(mentionUtil.replaceMentions(bulk.get(5)));
                commentShow.setTime(bulk.get(4));
                commentShow.setCommentId(Integer.valueOf(bulk.get(2)));
                commentShow.setUser(retwisRepository.getUserById(bulk.get(1)));
                commentShow.setCanDelete(bulk.get(1).equals(uid)?true:false);
                commentShow.initTimeShow();//初始化时间显示
                return commentShow;
            }
        };

        List<CommentShow> comments = template.sort(query, hm);
        return comments;
    }

    /**
     * 提到我的评论数
     * @param uid
     * @return
     */
    public int getCommentListByMentionNum(String uid){

        return template.opsForList().size(KeyUtils.commentmentions(uid)).intValue();
    }



    /**
     * 处理提及@用户的事宜
     * @param post
     * @param pid
     */
    private void handleMentions(Comment comment) {
        // find mentions
        Collection<String> mentions = mentionUtil.findMentions(comment.getContent());
        for (String mention : mentions) {
            String uid = retwisRepository.findUidByNickname(mention);
            if (uid != null) {
                commentmentions(uid).addFirst(comment.getCommentId().toString());
            }
        }
    }

    private RedisList<String> commentmentions(String uid) {
        return new DefaultRedisList<String>(KeyUtils.commentmentions(uid), template);
    }
}
