package com.xiaozhi.blog.web;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.service.CommentService;
import com.xiaozhi.blog.vo.Comment;
import com.xiaozhi.blog.vo.CommentShow;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class CommentController {

    private static Log logger = LogFactory.getLog(CommentController.class);

    @Autowired
    private  CommentService commentService;

    @Value(value="#{globalProperties['blog.list.pagesize']}")
    private int pageSize;


    /**
     * 获得评论列表
     * @param pid
     * @param model
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="/comment/list/{blogOwner}/{pid}",method = RequestMethod.GET)
    public String commentlist(@PathVariable String blogOwner,@PathVariable String pid,Model model){

        List<CommentShow> commentShows = this.commentService.getCommentListByPid(pid,LoginHelper.getUserId());
        model.addAttribute("comments", commentShows);
        model.addAttribute("pid", pid);
        model.addAttribute("blogOwner", blogOwner);

        return "/comment/comments";
    }

    /**
     * 提交评论
     * @param userid
     * @return
     */
    @RequestMapping(value = "/comment/postcomment/{blogOwner}/{pid}", method = RequestMethod.POST)
    public  String postComment(@PathVariable String blogOwner,
            @PathVariable String pid,
            @RequestParam(required = false,defaultValue="false") boolean isReplay ,
            Comment comment,Model model) {

        comment.setUid(LoginHelper.getUserId());
        model.addAttribute("c", this.commentService.addComment(pid, comment,blogOwner,isReplay));
        model.addAttribute("pid", pid);
        model.addAttribute("blogOwner", blogOwner);

        return "/comment/comment";
    }


    /**
     * 删除微薄评论
     * @param pid
     * @return
     */
    @RequestMapping(value = "/comment/delcomment/{blogOwner}/{pid}/{commentId}", method = RequestMethod.POST)
    public @ResponseBody boolean delComment(@PathVariable String blogOwner,
            @PathVariable String pid,@PathVariable String commentId) {

        return this.commentService.removeComment(pid,  commentId,  LoginHelper.getUserId(),  blogOwner);
    }


    /**
     * 发出的评论
     * @param uid
     * @param model
     * @return
     */
    @RequestMapping("/u/postcomments")
    public String postComments(@RequestParam(required = false,defaultValue="1") Integer page,Model model) {
        model.addAttribute("name", LoginHelper.getUserName());
        model.addAttribute("comments",this.commentService.getCommentListByPostCommentByPage(LoginHelper.getUserId(), page,pageSize));
        model.addAttribute("pagelist", "postComments");

        return "/comment/my_comment";
    }

    /**
     * 删除我发出的评论的残余
     * @param blogOwner
     * @param pid
     * @param commentId
     * @return
     */
    @RequestMapping(value = "/comment/delCommentForNoBlog/{commentId}", method = RequestMethod.POST)
    public @ResponseBody boolean delCommentForNoBlog(@PathVariable String commentId) {

        return this.commentService.delCommentForNoBlog(LoginHelper.getUserId(),  commentId);
    }

    /**
     * 发出的评论分页
     * @param uid
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "/u/postcomments/page", method = RequestMethod.GET)
    public String postCommentsPage(@RequestParam(required = false,defaultValue="1") Integer page,Model model) {

        model.addAttribute("comments",this.commentService.getCommentListByPostCommentByPage(LoginHelper.getUserId(), page,pageSize));
        return "/comment/comment_post";

    }


    /**
     * 收到的评论
     * @param uid
     * @param model
     * @return
     */
    @RequestMapping("/u/receivecomments")
    public String recceiveComments(@RequestParam(required = false,defaultValue="1") Integer page,Model model) {
        model.addAttribute("name", LoginHelper.getUserName());

        model.addAttribute("comments", commentService.getCommentListByReceiveCommentByPage(LoginHelper.getUserId(), page,this.pageSize));
        model.addAttribute("pagelist", "receiveComments");
        return "/comment/my_comment";
    }


    /**
     * 收到的评论分页
     * @param uid
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "/u/receivecomments/page", method = RequestMethod.GET)
    public String recceiveCommentsPage(@RequestParam(required = false,defaultValue="1") Integer page,Model model) {

        model.addAttribute("comments",commentService.getCommentListByReceiveCommentByPage(LoginHelper.getUserId(), page,this.pageSize));
        return "/comment/comment_receive";

    }


}
