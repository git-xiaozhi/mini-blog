package com.xiaozhi.blog.web.manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.service.FollowerService;
import com.xiaozhi.blog.service.FollowingService;
import com.xiaozhi.blog.service.GroupService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
*   类说明
*
*   @creator            your name
*   @create-time     2011-11-13   下午10:22:26
*   @revision          $Id
*/
@Controller
@RequestMapping("/manage/following/")
public class FollowingManageController {


    private static Log logger = LogFactory.getLog(FollowingManageController.class);

    @Autowired
    private FollowerService followerService;
    @Autowired
    private FollowingService followingService;
    @Autowired
    private GroupService groupService;

    @Value(value="#{globalProperties['blog.list.pagesize']}")
    private int pageSize;

    /**
     * 关注列表
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "followings", method = RequestMethod.GET)
    public String flowing(@RequestParam(value="page",defaultValue="1") Integer page, Model model) {


        String uid = LoginHelper.getUserId();
        model.addAttribute("followers", followerService.getFollowers(uid));
        model.addAttribute("following", followingService.getFollowing(uid));
        model.addAttribute("groups", this.groupService.getGroupByUserId(uid));


        model.addAttribute("pages",  followingService.getFollowingsByPage(uid, page, pageSize));

        return "/manage/followings";
    }

    @RequestMapping(value = "followings/page", method = RequestMethod.GET)
    public String flowingPage(@RequestParam(value="page",defaultValue="1") Integer page, Model model) {

        String uid = LoginHelper.getUserId();
        model.addAttribute("pages",  followingService.getFollowingsByPage(uid, page, pageSize));

        return "/fragments/followinglist";
    }


    /**
     * 获得分组关注列表
     * @param groupId
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "followinggroup/{groupId}", method = RequestMethod.GET)
    public String flowingGroup(@PathVariable String groupId,
    		@RequestParam(value="page",defaultValue="1") Integer page, Model model) {

        String uid = LoginHelper.getUserId();
        model.addAttribute("following", this.groupService.getMembersByGroupId(groupId));

        return "/fragments/followinggroup";
    }


    /**
     * 加关注
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "follow/{targetUid}", method = RequestMethod.GET)
    public @ResponseBody boolean follow(@PathVariable String targetUid, Model model) {

        return followingService.follow(targetUid,LoginHelper.getUserId());
    }


    /**
     * 取消关注
     * @param targetUser
     * @param model
     * @return
     */
    @RequestMapping(value = "stopfollowing/{targetUid}", method = RequestMethod.GET)
    public @ResponseBody boolean stopFollowing(@PathVariable String targetUid, Model model) {
        return followingService.stopFollowing(targetUid,LoginHelper.getUserId());
    }

}
