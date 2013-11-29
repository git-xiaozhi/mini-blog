package com.xiaozhi.blog.web.manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.service.FollowerService;
import com.xiaozhi.blog.service.FollowingService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/manage/follower/")
public class FollowerManageController {

    private static Log logger = LogFactory.getLog(FollowerManageController.class);

    @Autowired
    private FollowerService followerService;
    @Autowired
    private FollowingService followingService;

    @Value(value="#{globalProperties['blog.list.pagesize']}")
    private int pageSize;


    /**
     * 粉丝列表
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "fans", method = RequestMethod.GET)
    public String blogs(@RequestParam(value="page",defaultValue="1") Integer page, Model model) {


        String uid = LoginHelper.getUserId();
        model.addAttribute("followers", followerService.getFollowers(uid));
        model.addAttribute("following", followingService.getFollowing(uid));


        model.addAttribute("pages",  followerService.getFollowersByPage(uid, page, pageSize));

        return "/manage/fans";
    }


    @RequestMapping(value = "fans/page", method = RequestMethod.GET)
    public String blogsPage(@RequestParam(value="page",defaultValue="1") Integer page, Model model) {


        String uid = LoginHelper.getUserId();
        model.addAttribute("pages",  followerService.getFollowersByPage(uid, page, pageSize));

        return "/fragments/fanslist";
    }



}
