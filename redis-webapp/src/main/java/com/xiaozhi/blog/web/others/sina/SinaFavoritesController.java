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

import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.mongo.MongoUserDao;
import com.xiaozhi.blog.service.other.sina.BlogTime;
import com.xiaozhi.blog.vo.SinaAccessToken;
import weibo4j.Favorite;
import weibo4j.Timeline;
import weibo4j.model.Favorites;
import weibo4j.model.FavoritesWapper;
import weibo4j.model.Paging;
import weibo4j.model.WeiboException;



@Controller
@RequestMapping("/blog/sina/favorites")
public class SinaFavoritesController {

	private static Log logger = LogFactory.getLog(SinaFavoritesController.class);

	@Autowired
	private Favorite favorite;

	@Autowired
	private  MongoUserDao retwis;

	@Autowired
	private BlogTime blogTime;

	@Autowired
	private Timeline timeline;


	/**
	 * 获取用户收藏列表
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "favoritetimeline", method = RequestMethod.GET)
    public String favorites(@RequestParam(required = false,defaultValue="1") Integer page,Model model) {
		SinaAccessToken accessToken = (SinaAccessToken)retwis.getAccessTokenByUser(LoginHelper.getUserId());
		favorite.client.setToken(accessToken.getAccesstoken());
		timeline.client.setToken(accessToken.getAccesstoken());
		try {
			FavoritesWapper favoritesWapper = this.favorite.getFavorites(new Paging(page, 20));
			if(logger.isDebugEnabled()){
				logger.debug("--------------------------->"+favoritesWapper.getFavoriteslist().size());
			}
			model.addAttribute("favoritesWapper",this.blogTime.getFavoritesWapperWithFaceImage(favoritesWapper,timeline));
			model.addAttribute("pages", getPages(favoritesWapper.getTotalNumber(),20));
			model.addAttribute("page",new Paging(page, 20));
		} catch (WeiboException e) {
			logger.debug("--------------------------->error :"+e.getError());
			if("expired_token".equals(e.getError())){
              return "redirect:/bind/sina?callbackUrl=/blog/sina/hometimeline";
			}else{
			  e.printStackTrace();
			}
		}
		return "/blog/sina/favoritetimeline";
	}

    /**
     * ajax 微博收藏分页
     * @param name
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "favorites/page", method = RequestMethod.GET)
    public String favoritesPage(Integer page,Model model) {

		try {
			SinaAccessToken accessToken = (SinaAccessToken)retwis.getAccessTokenByUser(LoginHelper.getUserId());
			favorite.client.setToken(accessToken.getAccesstoken());
			timeline.client.setToken(accessToken.getAccesstoken());
			FavoritesWapper favoritesWapper = this.favorite.getFavorites(new Paging(page, 20));
			model.addAttribute("favoritesWapper",this.blogTime.getFavoritesWapperWithFaceImage(favoritesWapper,timeline));
			model.addAttribute("pages", getPages (favoritesWapper.getTotalNumber(),20));
			model.addAttribute("page",new Paging(page, 20));
		} catch (WeiboException e) {
			e.printStackTrace();
		}
        return "/blog/sina/favoritespage";

    }


    /**
     * 收藏一条微博信息
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "createFavorite/{id}", method = RequestMethod.POST)
    public @ResponseBody boolean createFavorite(@PathVariable String id,Model model){
		try {
			SinaAccessToken accessToken = (SinaAccessToken)retwis.getAccessTokenByUser(LoginHelper.getUserId());
			favorite.client.setToken(accessToken.getAccesstoken());
			Favorites favorites = this.favorite.createFavorites(id);
			return true;
		} catch (WeiboException e) {
			e.printStackTrace();
		}

        return false;
    }

/**
 * 删除我的微博收藏
 * @param id
 * @param model
 * @return
 */
    @RequestMapping(value = "removeFavorite/{id}", method = RequestMethod.POST)
    public @ResponseBody boolean removeFavorite(@PathVariable String id,Model model){
		try {
			SinaAccessToken accessToken = (SinaAccessToken)retwis.getAccessTokenByUser(LoginHelper.getUserId());
			favorite.client.setToken(accessToken.getAccesstoken());
			Favorites favorites = this.favorite.destroyFavorites(id);
			return true;
		} catch (WeiboException e) {
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
