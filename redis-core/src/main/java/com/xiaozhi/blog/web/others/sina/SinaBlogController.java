package com.xiaozhi.blog.web.others.sina;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.mongo.MongoUserDao;
import com.xiaozhi.blog.vo.SinaAccessToken;
import com.xiaozhi.blog.service.other.sina.BlogTime;
import com.xiaozhi.blog.service.other.sina.Faces;
import com.xiaozhi.blog.utils.FileUtil;

import weibo4j.Friendships;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.http.ImageItem;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;



@Controller
@RequestMapping("/blog/sina")
public class SinaBlogController {

	private static Log logger = LogFactory.getLog(SinaBlogController.class);

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
	 * 获取用户首页微博列表(包括关注人的微博)
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "hometimeline", method = RequestMethod.GET)
    public String hometimeline(@RequestParam(required = false,defaultValue="1") Integer page,
    		HttpServletRequest request,Model model) {
		SinaAccessToken accessToken = retwis.getAccessTokenByUser(LoginHelper.getUserId());
		if(accessToken==null)return "redirect:/bind/sina?callbackUrl=/blog/sina/hometimeline";

		timeline.client.setToken(accessToken.getAccesstoken());
		try {
			StatusWapper statusWapper = timeline.getHomeTimeline(0, 0, new Paging(page, 20));

			model.addAttribute("statusWapper",this.blogTime.getStatusWapperWithFaceImage(statusWapper,timeline));
			model.addAttribute("pages", getPages (statusWapper.getTotalNumber(),20));
			model.addAttribute("page",new Paging(page, 20));
			model.addAttribute("sessionId", request.getSession().getId());
		} catch (WeiboException e) {
			logger.debug("--------------------------->error :"+e.getError());
			if("expired_token".equals(e.getError())){
              return "redirect:/bind/sina?callbackUrl=/blog/sina/hometimeline";
			}else{
			  e.printStackTrace();
			}
		}
		return "/blog/sina/hometimeline";
	}

    /**
     * ajax 微薄分页
     * @param name
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "timeLinepage/page", method = RequestMethod.GET)
    public String homeTimeLinePage(Integer page,Model model) {

		try {
			SinaAccessToken accessToken = retwis.getAccessTokenByUser(LoginHelper.getUserId());
			timeline.client.setToken(accessToken.getAccesstoken());
			StatusWapper statusWapper = timeline.getHomeTimeline(0, 0, new Paging(page, 20));
			model.addAttribute("statusWapper",this.blogTime.getStatusWapperWithFaceImage(statusWapper,timeline));
			model.addAttribute("pages", getPages (statusWapper.getTotalNumber(),20));
			model.addAttribute("page",new Paging(page, 20));
		} catch (WeiboException e) {
			e.printStackTrace();
		}
        return "/blog/sina/post";

    }

    /**
     * 图片上传
     * @param model
     * @param logo
     * @param resquest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value="upload")
    public @ResponseBody String upload(ModelMap model,@RequestParam("filename") MultipartFile logo,HttpServletRequest  resquest ) {

        try {
            byte[] a = logo.getBytes();
            String filePath = resquest.getRealPath(resquest.getServletPath());
            String name=FileUtil.sinaUploadFileHandle(a, filePath, LoginHelper.getUserId(), logo.getOriginalFilename());

            return resquest.getServletPath()+"/"+name;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "true";
    }


    /**
     * 提交新浪微博
     * @param content
     * @param pic
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    public String updateStatus(String content,String pic,HttpServletRequest  resquest,Model model) throws IOException {
    	Status status = null;
		try {
			SinaAccessToken accessToken = retwis.getAccessTokenByUser(LoginHelper.getUserId());
			timeline.client.setToken(accessToken.getAccesstoken());
			if(pic==null || "".equals(pic)){
			  status = timeline.UpdateStatus(content);
			}else{
    	      String filePath = resquest.getRealPath(pic);
			  byte[] picbyte = FileUtil.readFileImage(filePath);
			  ImageItem imageItem = new ImageItem("pic", picbyte);
			  status = timeline.UploadStatus(java.net.URLEncoder.encode(content, "utf-8"), imageItem);
		   }
			model.addAttribute("p",status);
            return "/blog/sina/newpost";
		} catch (WeiboException e) {
			e.printStackTrace();
		}
        return null;
    }



    @RequestMapping(value = "showRepostForm/{id}", method = RequestMethod.GET)
    public String repostFormShow(String content,@PathVariable String id,Model model) throws IOException {
    	Status status = null;
		try {
			SinaAccessToken accessToken = retwis.getAccessTokenByUser(LoginHelper.getUserId());
			timeline.client.setToken(accessToken.getAccesstoken());
			status = timeline.showStatus(id);
			model.addAttribute("p",status);
            return "/blog/sina/repostform";
		} catch (WeiboException e) {
			e.printStackTrace();
		}
        return null;
    }

    /**
     * 转发一条微博
     * @param content
     * @param id
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "repostStatus/{id}", method = RequestMethod.POST)
    public String repost(String content,@PathVariable String id,Model model) throws IOException {
    	Status status = null;
		try {
			SinaAccessToken accessToken = retwis.getAccessTokenByUser(LoginHelper.getUserId());
			timeline.client.setToken(accessToken.getAccesstoken());
			status = timeline.Repost(id, content, 0);
			model.addAttribute("p",status);
            return "/blog/sina/newpost";
		} catch (WeiboException e) {
			e.printStackTrace();
		}
        return null;
    }

/**
 * 删除我的微博
 * @param id
 * @param model
 * @return
 */
    @RequestMapping(value = "removeBlogByMe/{id}", method = RequestMethod.POST)
    public @ResponseBody boolean removeBlogByMe(@PathVariable String id,Model model){
		try {
			SinaAccessToken accessToken = retwis.getAccessTokenByUser(LoginHelper.getUserId());
			timeline.client.setToken(accessToken.getAccesstoken());
			Status status = this.timeline.Destroy(id);
			return true;
		} catch (WeiboException e) {
			e.printStackTrace();
		}

        return false;
    }



	/**
	 * 获取用户微博列表
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "usertimeline", method = RequestMethod.GET)
    public String usertimeline(@RequestParam(required = false,defaultValue="1") Integer page,Model model) {
		SinaAccessToken accessToken = retwis.getAccessTokenByUser(LoginHelper.getUserId());
		timeline.client.setToken(accessToken.getAccesstoken());
		users.client.setToken(accessToken.getAccesstoken());
		friendships.client.setToken(accessToken.getAccesstoken());

		try {
			StatusWapper statusWapper = timeline.getUserTimelineByUid(accessToken.getUserid(),new Paging(page, 20),0, 0);
            User user = users.showUserById(accessToken.getUserid());
            UserWapper followers = friendships.getFollowersById(accessToken.getUserid(), 6, 0);
            UserWapper following = friendships.getFriendsBilateral(accessToken.getUserid(),0,new Paging(page, 6));

			model.addAttribute("statusWapper",this.blogTime.getStatusWapperWithFaceImage(statusWapper,timeline));
			model.addAttribute("user",user);
			model.addAttribute("followers",followers);
			model.addAttribute("following",following);
			model.addAttribute("pagelist", "manage");
			model.addAttribute("pages", getPages (statusWapper.getTotalNumber(),20));
			model.addAttribute("page",new Paging(page, 20));
		} catch (WeiboException e) {
			logger.debug("--------------------------->error :"+e.getError());
			if("expired_token".equals(e.getError())){
              return "redirect:/bind/sina?callbackUrl=/blog/sina/hometimeline";
			}else{
			  e.printStackTrace();
			}
		}
		return "/blog/sina/userblogs";
	}


    /**
     * ajax 微薄分页
     * @param name
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "usertimeline/page", method = RequestMethod.GET)
    public String usertimelinePage(Integer page,Model model) {

		try {
			SinaAccessToken accessToken = retwis.getAccessTokenByUser(LoginHelper.getUserId());
			StatusWapper statusWapper = timeline.getUserTimelineByUid(accessToken.getUserid(),new Paging(page, 20),0, 0);
			model.addAttribute("statusWapper",this.blogTime.getStatusWapperWithFaceImage(statusWapper,timeline));
			model.addAttribute("page",new Paging(page, 20));
			model.addAttribute("pages", getPages (statusWapper.getTotalNumber(),20));
			model.addAttribute("pagelist", "manage");
		} catch (WeiboException e) {
			e.printStackTrace();
		}
        return "/blog/sina/post";

    }

    /**
     * 获取远程新浪表情数据
     * @param model
     * @return
     * @throws WeiboException
     */
    @RequestMapping(value = "faces", method = RequestMethod.GET)
    public @ResponseBody List<Faces> getFaces(Model model) throws WeiboException{
    	SinaAccessToken accessToken = retwis.getAccessTokenByUser(LoginHelper.getUserId());
    	return this.blogTime.getRemoteExp(accessToken.getAccesstoken());
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
