package com.xiaozhi.blog.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.xiaozhi.blog.redis.CommentDao;
import com.xiaozhi.blog.mongo.MongoCommentDao;
import com.xiaozhi.blog.mongo.MongoUserDao;
import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.utils.MentionUtil;
import com.xiaozhi.blog.vo.Comment;
import com.xiaozhi.blog.vo.CommentShow;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.User;

@Service
public class CommentService {

    private static Log logger = LogFactory.getLog(CommentService.class);

    @Autowired
    private MongoCommentDao commentDao;
    @Autowired
    MongoUserDao mongoUserDao;

    @Autowired
    private MentionUtil mentionUtil;

    /**
     * 发评论
     * @param pid
     * @param comment
     * @param isReplay :是否是回复评论标志
     * @return
     */
    public CommentShow addComment(String pid,Comment comment,String blogOwner,boolean isReplay){
        try {
            comment.setContent(this.mentionUtil.replayFaceImages(comment.getContent()));
            this.commentDao.addComment(pid, comment,blogOwner,isReplay);
            //构建返回的评论对象
            CommentShow commentShow = new CommentShow();
            BeanUtils.copyProperties(comment, commentShow,new String[]{"commentId"});
            commentShow.setCommentId(Integer.valueOf(comment.getCommentId()));
            User user = this.mongoUserDao.getUserById(comment.getUid());
            user.setName(user.getName());
            user.setId(comment.getUid());
            commentShow.setUser(user);
            commentShow.setCanDelete(true);
            commentShow.initTimeShow();

            return commentShow;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得微博评论
     * @param pid
     * @return
     */
    public List<CommentShow> getCommentListByPid(String pid,String userid){

        List<CommentShow> comments = this.commentDao.getCommentListByPid(pid,userid);

        return comments;
    }

    /**
     * 删除微薄评论
     * @param pid
     * @param userid
     * @return
     */
    public boolean removeComment(String pid, String commentId, String uid, String blogOwner){
        return this.commentDao.removeComment( pid,  commentId,  uid,  blogOwner);
    }

    /**
     * 删除我发出的评论的残余
     * @param uid
     * @param commentId
     * @return
     */
    public boolean delCommentForNoBlog(final String uid,final String commentId){
    	return this.commentDao.delCommentForNoBlog(uid,  commentId);

    }

    /**
     * 分页获取用户收到的评论列表
     * @param uid
     * @param page
     * @param pagesize
     * @return
     */
    public ListPage<CommentShow> getCommentListByReceiveCommentByPage(String uid, Integer page,Integer pagesize) {
    	List<String> commentIds= this.commentDao.getCommentListNum(uid,KeyUtils.receiveComments(uid));
        int firstResult = (page-1)*pagesize;
        int lastResult = firstResult+pagesize-1;
        int allResults = commentIds.size();
        List<CommentShow> comments=this.commentDao.getCommentList(uid,commentIds, new Range(firstResult,lastResult));
        return new ListPage<CommentShow>(comments, firstResult, lastResult, allResults);
    }

    /**
     * 分页获取用户发出的评论列表
     * @param uid
     * @param page
     * @param pagesize
     * @return
     */
    public ListPage<CommentShow> getCommentListByPostCommentByPage(String uid, Integer page,Integer pagesize) {
    	List<String> commentIds= this.commentDao.getCommentListNum(uid,KeyUtils.postComments(uid));
        int firstResult = (page-1)*pagesize;
        int lastResult = firstResult+pagesize-1;
        int allResults = commentIds.size();
        List<CommentShow> comments=this.commentDao.getCommentList(uid, commentIds,new Range(firstResult,lastResult));
        return new ListPage<CommentShow>(comments, firstResult, lastResult, allResults);
    }



    /**
     * 提到我的评论列表
     * @param uid
     * @param page
     * @param pagesize
     * @return
     */
    public ListPage<CommentShow> getCommentListByMentionByPage(String uid, Integer page,Integer pagesize) {
    	List<String> commentIds= this.commentDao.getCommentListNum(uid,KeyUtils.commentmentions(uid));
        int firstResult = (page-1)*pagesize;
        int lastResult = firstResult+pagesize-1;
        int allResults = commentIds.size();
        List<CommentShow> comments=this.commentDao.getCommentList(uid, commentIds,new Range(firstResult,lastResult));
        return new ListPage<CommentShow>(comments, firstResult, lastResult, allResults);
    }


}
