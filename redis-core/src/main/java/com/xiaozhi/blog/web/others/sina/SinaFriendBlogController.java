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

import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.mongo.MongoUserDao;
import com.xiaozhi.blog.service.other.sina.BlogTime;
import com.xiaozhi.blog.vo.SinaAccessToken;
import weibo4j.Friendships;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.model.Paging;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;



@Controller
@RequestMapping("/blog/sina/friend")
public class SinaFriendBlogController {

	private static Log logger = LogFactory.getLog(SinaFriendBlogController.class);

	@Autowired
	private Timeline timeline;

	@Autowired
	private Users users;

	@Autowired
	private Friendships friendships;

	@Autowired
	private  MongoUserDao retwis;

	@Autowired
	private BlogTime blogTime;


	/**
	 * 获取用户微博列表
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "usertimeline/{uid}", method = RequestMethod.GET)
    public String usertimeline(@RequestParam(required = false,defaultValue="1") Integer page,@PathVariable String uid,Model model) {
		SinaAccessToken accessToken = (SinaAccessToken)retwis.getAccessTokenByUser(LoginHelper.getUserId());
		timeline.client.setToken(accessToken.getAccesstoken());
		users.client.setToken(accessToken.getAccesstoken());
		friendships.client.setToken(accessToken.getAccesstoken());

		try {
			StatusWapper statusWapper = timeline.getUserTimelineByUid(uid,new Paging(page, 20),0, 0);
            User user = users.showUserById(uid);
            UserWapper followers = friendships.getFollowersById(uid, 6, 0);
            UserWapper following = friendships.getFriendsBilateral(uid,0,new Paging(page, 6));

			model.addAttribute("statusWapper",this.blogTime.getStatusWapperWithFaceImage(statusWapper,timeline));
			model.addAttribute("user",user);
			model.addAttribute("followers",followers);
			model.addAttribute("following",following);
			model.addAttribute("pagelist", "friendblogs");
			model.addAttribute("pages", getPages (statusWapper.getTotalNumber(),20));
			model.addAttribute("page",new Paging(page, 20));
			model.addAttribute("uid",uid);
		} catch (WeiboException e) {
			logger.debug("--------------------------->error :"+e.getError());
			if("expired_token".equals(e.getError())){
              return "redirect:/bind/sina?callbackUrl=/blog/sina/hometimeline";
			}else{
			  e.printStackTrace();
			}
		}
		return "/blog/sina/friendblogs";
	}


    /**
     * ajax 微博分页
     * @param name
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "usertimeline/page/{uid}", method = RequestMethod.GET)
    public String usertimelinePage(Integer page,@PathVariable String uid,Model model) {

		try {
			StatusWapper statusWapper = timeline.getUserTimelineByUid(uid,new Paging(page, 20),0, 0);
			model.addAttribute("statusWapper",this.blogTime.getStatusWapperWithFaceImage(statusWapper,timeline));
			model.addAttribute("page",new Paging(page, 20));
			model.addAttribute("pages", getPages (statusWapper.getTotalNumber(),20));
			model.addAttribute("pagelist", "friendblogs");
			model.addAttribute("uid",uid);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
        return "/blog/sina/post";

    }


  private long getPages (long totalNumber,long pagesize){
    	long pages = totalNumber % pagesize;
		if (pages == 0)
			pages = totalNumber / pagesize;
		else
			pages = totalNumber / pagesize + 1;
		return pages;
    }
}
