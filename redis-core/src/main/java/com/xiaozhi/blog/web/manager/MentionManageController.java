
package com.xiaozhi.blog.web.manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.mongo.MongoUserDao;
import com.xiaozhi.blog.service.BlogService;
import com.xiaozhi.blog.service.CommentService;
import com.xiaozhi.blog.service.FollowerService;
import com.xiaozhi.blog.service.FollowingService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 处理微薄提及和评论提及
 * @author xiaozhi
 *
 */
@Controller
public class MentionManageController {

    private static Log logger = LogFactory.getLog(MentionManageController.class);

    @Autowired
    private final MongoUserDao retwis;
    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;
    @Autowired
    public MentionManageController(MongoUserDao twitter) {
        this.retwis = twitter;
    }

    @Autowired
    private FollowerService followerService;
    @Autowired
    private FollowingService followingService;

    @Value(value="#{globalProperties['blog.list.pagesize']}")
    private int pageSize;

    /**
     * 微博提到我的
     * @param uid
     * @param model
     * @return
     */
    @RequestMapping("/u/mentions")
    public String mentions(@RequestParam(required = false,defaultValue="1") Integer page,Model model) {
        model.addAttribute("name", LoginHelper.getUserName());
        String targetUid = LoginHelper.getUserId();

        model.addAttribute("posts", blogService.getMentions(targetUid, page,this.pageSize));


        model.addAttribute("followers", followerService.getFollowers(targetUid));
        model.addAttribute("following", followingService.getFollowing(targetUid));
        model.addAttribute("user", retwis.getUserById(targetUid));

        if (!targetUid.equals(LoginHelper.getUserId())) {
            model.addAttribute("also_followed", followerService.alsoFollowed(LoginHelper.getUserId(), targetUid,1,6));
            model.addAttribute("common_followers", followerService.commonFollowers(LoginHelper.getUserId(), targetUid,1,6));
            model.addAttribute("follows", followingService.isFollowing(LoginHelper.getUserId(), targetUid));
        }

        model.addAttribute("pagelist", "mention");

        return "/mention/mentions_blog";
    }

    /**
     * 微博提到我的分页
     * @param uid
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "/u/mentions/page", method = RequestMethod.GET)
    public String mentionsPage(@RequestParam(required = false,defaultValue="1") Integer page,Model model) {
        model.addAttribute("posts", blogService.getMentions(LoginHelper.getUserId(), page,this.pageSize));
        model.addAttribute("pagelist", "mention");
        return "/fragments/post";

    }


    /**
     * 评论提到我的
     * @param uid
     * @param model
     * @return
     */
    @RequestMapping("/u/commentmentions")
    public String commentMentions(@RequestParam(required = false,defaultValue="1") Integer page,Model model) {
        model.addAttribute("name", LoginHelper.getUserName());

        model.addAttribute("comments", commentService.getCommentListByMentionByPage(LoginHelper.getUserId(), page,this.pageSize));

        return "/mention/mentions_comment";
    }


    /**
     * 评论提到我的分页
     * @param uid
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "/u/commentmentions/page", method = RequestMethod.GET)
    public String commentMentionsPage(@RequestParam(required = false,defaultValue="1") Integer page,Model model) {

        model.addAttribute("posts", commentService.getCommentListByMentionByPage(LoginHelper.getUserId(), page,this.pageSize));

        return "/mention/comment_m";

    }
}