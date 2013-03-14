package com.xiaozhi.blog.web.manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.service.BlogService;
import com.xiaozhi.blog.service.FollowerService;
import com.xiaozhi.blog.service.FollowingService;
import com.xiaozhi.blog.service.UserService;


@Controller
@RequestMapping("/manage/blog/")
public class BlogManageController {

    private static Log logger = LogFactory.getLog(BlogManageController.class);

    @Autowired
    private  BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowerService followerService;
    @Autowired
    private FollowingService followingService;

    @Value(value="#{globalProperties['blog.list.pagesize']}")
    private int pageSize;


    /**
     * 自己发布的微薄列表
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "blogs", method = RequestMethod.GET)
    public String blogs(@RequestParam(required = false,defaultValue="1") Integer page, Model model) {


        String targetUid = LoginHelper.getUserId();

        model.addAttribute("user", userService.getUserById(targetUid));
        model.addAttribute("followers", followerService.getFollowers(targetUid));
        model.addAttribute("following", followingService.getFollowing(targetUid));

        model.addAttribute("posts",  blogService.getPostsByPage(targetUid, page,this.pageSize));
        model.addAttribute("pagelist", "manage");

        return "/manage/blogs";
    }



    /**
     * 自己发布的微薄列表分页
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "blogs/page", method = RequestMethod.GET)
    public String blogsPage(@RequestParam(required = false,defaultValue="1") Integer page, Model model) {


        String targetUid = LoginHelper.getUserId();
        model.addAttribute("posts",  blogService.getPostsByPage(targetUid, page,this.pageSize));
        model.addAttribute("pagelist", "manage");

        return "/fragments/post";
    }


    /**
     * 删除自己评论
     * @param pid
     * @return
     */
    @RequestMapping(value = "removeBlogByMe/{pid}", method = RequestMethod.POST)
    public @ResponseBody boolean removeBlogByMe(@PathVariable String pid) {
        return this.blogService.removeBlogByMe(LoginHelper.getUserId(), pid);
    }


    /**
     * 别人删除自己评论(自己已删除,但留下残余pid列表)
     * @param pid
     * @return
     */
    @RequestMapping(value = "removeBlogByOther/{pid}", method = RequestMethod.POST)
    public @ResponseBody boolean removeBlogByOther(@PathVariable String pid) {
        return this.blogService.removeBlogByOther(LoginHelper.getUserId(), pid);
    }

    /**
     * 删除提及我的微博的残余
     * @param pid
     * @return
     */
    @RequestMapping(value = "removeMention/{pid}", method = RequestMethod.POST)
    public @ResponseBody boolean removeMention(@PathVariable String pid) {
        return this.blogService.removeMention(LoginHelper.getUserId(), pid);
    }


}
