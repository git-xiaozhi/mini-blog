package com.xiaozhi.blog.web.others.sina;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.mongo.MongoUserDao;
import com.xiaozhi.blog.vo.SinaAccessToken;
import weibo4j.Friendships;
import weibo4j.model.Paging;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;


@Controller
@RequestMapping("/blog/sina/follow")
public class SinaFollowersController {

	private static Log logger = LogFactory.getLog(SinaFollowersController.class);

	@Autowired
	private Friendships friendships;

	@Autowired
	private  MongoUserDao retwis;

	/**
	 * 粉丝管理列表首页
	 * @param page
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "followers", method = RequestMethod.GET)
    public String followers(@RequestParam(required = false,defaultValue="1") Integer page,
    		HttpServletRequest request,Model model) {
		SinaAccessToken accessToken = (SinaAccessToken)retwis.getAccessTokenByUser(LoginHelper.getUserId());
		friendships.client.setToken(accessToken.getAccesstoken());
		try {
			UserWapper users = friendships.getFollowersById(accessToken.getUserid(), 10, (page-1)*10);
			List<String> uids = Arrays.asList(friendships.getFriendsBilateralIds(accessToken.getUserid()));//获取互粉用户id
			for(User user : users.getUsers()){
				if(uids.contains(user.getId())){
					user.setFollowMe(true);//借用此属性做为互粉标志
				}else{
					user.setFollowMe(false);
				}
			}
			model.addAttribute("users",users);
			model.addAttribute("pages", getPages (users.getTotalNumber(),10));
			model.addAttribute("page",new Paging(page, 10));
		} catch (WeiboException e) {
			logger.debug("--------------------------->error :"+e.getError());
			if("expired_token".equals(e.getError())){
              return "redirect:/bind/sina?callbackUrl=/blog/sina/hometimeline";
			}else{
			  e.printStackTrace();
			}
		}
		return "/blog/sina/fans";
	}

	/**
	 * 分页
	 * @param page
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "followers/page", method = RequestMethod.GET)
    public String followersPage(@RequestParam(required = false,defaultValue="1") Integer page,Model model) {
		SinaAccessToken accessToken = (SinaAccessToken)retwis.getAccessTokenByUser(LoginHelper.getUserId());
		friendships.client.setToken(accessToken.getAccesstoken());
		try {
			UserWapper users = friendships.getFollowersById(accessToken.getUserid(), 10, (page-1)*10);
			List<String> uids = Arrays.asList(friendships.getFriendsBilateralIds(accessToken.getUserid()));//获取互粉用户id
			for(User user : users.getUsers()){
				if(uids.contains(user.getId())){
					user.setFollowMe(true);//借用此属性做为互粉标志
				}else{
					user.setFollowMe(false);
				}
			}
			model.addAttribute("users",users);
			model.addAttribute("pages", getPages (users.getTotalNumber(),10));
			model.addAttribute("page",new Paging(page, 10));
		} catch (WeiboException e) {
			logger.debug("--------------------------->error :"+e.getError());
			if("expired_token".equals(e.getError())){
              return "redirect:/bind/sina?callbackUrl=/blog/sina/hometimeline";
			}else{
			  e.printStackTrace();
			}
		}
		return "/blog/sina/fanslist";
	}



	/**
	 * 关注管理列表首页
	 * @param page
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "followings", method = RequestMethod.GET)
    public String followings(@RequestParam(required = false,defaultValue="1") Integer page,
    		HttpServletRequest request,Model model) {
		SinaAccessToken accessToken = (SinaAccessToken)retwis.getAccessTokenByUser(LoginHelper.getUserId());
		friendships.client.setToken(accessToken.getAccesstoken());
		try {
			UserWapper users = friendships.getFriendsByID(accessToken.getUserid(), 10, (page-1)*10);
			List<String> uids = Arrays.asList(friendships.getFriendsBilateralIds(accessToken.getUserid()));//获取互粉用户id
			for(User user : users.getUsers()){
				if(uids.contains(user.getId())){
					user.setFollowMe(true);//借用此属性做为互粉标志
				}else{
					user.setFollowMe(false);
				}
			}
			model.addAttribute("users",users);
			model.addAttribute("pages", getPages (users.getTotalNumber(),10));
			model.addAttribute("page",new Paging(page, 10));
		} catch (WeiboException e) {
			logger.debug("--------------------------->error :"+e.getError());
			if("expired_token".equals(e.getError())){
              return "redirect:/bind/sina?callbackUrl=/blog/sina/hometimeline";
			}else{
			  e.printStackTrace();
			}
		}
		return "/blog/sina/followings";
	}

	/**
	 * 分页
	 * @param page
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "followings/page", method = RequestMethod.GET)
    public String followingsPage(@RequestParam(required = false,defaultValue="1") Integer page,Model model) {
		SinaAccessToken accessToken = (SinaAccessToken)retwis.getAccessTokenByUser(LoginHelper.getUserId());
		friendships.client.setToken(accessToken.getAccesstoken());
		try {
			UserWapper users = friendships.getFriendsByID(accessToken.getUserid(), 10, (page-1)*10);
			List<String> uids = Arrays.asList(friendships.getFriendsBilateralIds(accessToken.getUserid()));//获取互粉用户id
			for(User user : users.getUsers()){
				if(uids.contains(user.getId())){
					user.setFollowMe(true);//借用此属性做为互粉标志
				}else{
					user.setFollowMe(false);
				}
			}
			model.addAttribute("users",users);
			model.addAttribute("pages", getPages (users.getTotalNumber(),10));
			model.addAttribute("page",new Paging(page, 10));
		} catch (WeiboException e) {
			logger.debug("--------------------------->error :"+e.getError());
			if("expired_token".equals(e.getError())){
              return "redirect:/bind/sina?callbackUrl=/blog/sina/hometimeline";
			}else{
			  e.printStackTrace();
			}
		}
		return "/blog/sina/followinglist";
	}


    /**
     * 取消关注
     * @param id
     * @param model
     * @return
     */
	@RequestMapping(value = "stopfollowing/{id}", method = RequestMethod.GET)
    public @ResponseBody boolean stopfollowing(@PathVariable String id,Model model) {
		SinaAccessToken accessToken = (SinaAccessToken)retwis.getAccessTokenByUser(LoginHelper.getUserId());
		friendships.client.setToken(accessToken.getAccesstoken());
		try {
			User user = friendships.destroyFriendshipsDestroyById(id);
			return true;
		} catch (WeiboException e) {
			logger.debug("--------------------------->error :"+e.getError());
			e.printStackTrace();
		}
		return false;
	}

    /**
     * 加关注
     * @param id
     * @param model
     * @return
     */
	@RequestMapping(value = "following/{id}", method = RequestMethod.GET)
    public @ResponseBody boolean following(@PathVariable String id,Model model) {
		SinaAccessToken accessToken = (SinaAccessToken)retwis.getAccessTokenByUser(LoginHelper.getUserId());
		friendships.client.setToken(accessToken.getAccesstoken());
		try {
			User user = friendships.createFriendshipsById(id);
			return true;
		} catch (WeiboException e) {
			logger.debug("--------------------------->error :"+e.getError());
			e.printStackTrace();
		}
		return false;
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
