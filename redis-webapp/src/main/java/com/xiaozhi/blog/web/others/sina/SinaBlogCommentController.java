package com.xiaozhi.blog.web.others.sina;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import weibo4j.Comments;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Paging;
import weibo4j.model.WeiboException;

import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.mongo.MongoUserDao;
import com.xiaozhi.blog.service.other.sina.BlogTime;
import com.xiaozhi.blog.vo.SinaAccessToken;


@Controller
@RequestMapping("/blog/sina/comment")
public class SinaBlogCommentController {

	private static Log logger = LogFactory.getLog(SinaBlogCommentController.class);

	@Autowired
	private Comments comments;

	@Autowired
	private  MongoUserDao retwis;

	@Autowired
	private BlogTime  blogTime;

    /**
     * 通过微博id获取微博评论
     * @param page
     * @param id
     * @param request
     * @param model
     * @return
     */
	@RequestMapping(value = "getCommentsById/{id}", method = RequestMethod.GET)
    public String getCommentsById(@RequestParam(required = false,defaultValue="1") Integer page,@PathVariable String id,Model model) {
		SinaAccessToken accessToken = (SinaAccessToken)retwis.getAccessTokenByUser(LoginHelper.getUserId());
		comments.client.setToken(accessToken.getAccesstoken());
		try {
			CommentWapper commentWapper = comments.getCommentById(id, new Paging(page, 10),0);
			model.addAttribute("comments",this.blogTime.getCommentWapperWithFaceImage(commentWapper, accessToken));
			model.addAttribute("pid", id);
		} catch (WeiboException e) {
			logger.debug("--------------------------->error :"+e.getError());
			e.printStackTrace();
		}
		return "/blog/sina/comments";
	}

    /**
     * 新增评论
     * @param id
     * @param content
     * @param model
     * @return
     */
	@RequestMapping(value = "postcomment/{id}", method = RequestMethod.POST)
    public String  postComment(@PathVariable String id,@RequestParam String content,Model model) {
		SinaAccessToken accessToken = (SinaAccessToken)retwis.getAccessTokenByUser(LoginHelper.getUserId());
		comments.client.setToken(accessToken.getAccesstoken());
		try {
			Comment comment = comments.createComment(content,id);
			logger.debug("--------------------------->comment :"+comment.getUser().getProfileImageUrl());
			model.addAttribute("c", this.blogTime.getCommentWithFaceImage(comment, accessToken));
		} catch (WeiboException e) {
			logger.debug("--------------------------->error :"+e.getError());
			e.printStackTrace();
		}
		return "/blog/sina/comment";
	}

	/**
	 * 删除一条微博评论
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delcomment/{id}", method = RequestMethod.POST)
    public @ResponseBody boolean delcomment(@PathVariable String id,Model model) {
		SinaAccessToken accessToken = (SinaAccessToken)retwis.getAccessTokenByUser(LoginHelper.getUserId());
		comments.client.setToken(accessToken.getAccesstoken());
		try {
			Comment comment = comments.destroyComment(id);
		} catch (WeiboException e) {
			logger.debug("--------------------------->error :"+e.getError());
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
