package com.xiaozhi.blog.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.img.ImageService;
import com.xiaozhi.blog.service.BlogService;
import com.xiaozhi.blog.service.VideoService;
import com.xiaozhi.blog.service.page.HTMLParser;
import com.xiaozhi.blog.utils.FileUtil;
import com.xiaozhi.blog.vo.Page;
import com.xiaozhi.blog.vo.Video;
import com.xiaozhi.blog.vo.WebPost;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;




@Controller
@RequestMapping("/blog/")
public class BlogController{

    private static Log logger = LogFactory.getLog(BlogController.class);

    @Autowired
    private BlogService blogService;
    
    @Autowired
    private ImageService imageService;


    @Autowired
    private VideoService videoService;



    /**
     * 转发微薄
     * @param pid
     * @param post
     * @param model
     * @return
     */
    @RequestMapping(value = "forwardBlog/{pid}", method = RequestMethod.POST)
    public String forwardBlog(@PathVariable String pid, WebPost post, Model model) {

        if(logger.isDebugEnabled())logger.debug("#########################"+post.toString());
        //if(post.getBindTianJi())restClient.postStatus(LoginHelper.getUserName(), post.getContent());//同步天际status
        post.setTransmitid(pid);
        WebPost returnPost=blogService.post(LoginHelper.getUserId(), post,true);
        model.addAttribute("p",returnPost);
        return "/fragments/newpost";
    }


    /**
     * 显示转发微薄表单
     * @param pid
     * @param model
     * @return
     */
    @RequestMapping(value = "showForwardForm/{pid}", method = RequestMethod.GET)
    public  String show(@PathVariable String pid, Model model) {
        WebPost blog = blogService.getBlogById(pid);
        if(null!=blog.getTransmitid()){//转发微薄
            blog.setWebPost(blogService.getBlogById(blog.getTransmitid()));
        }
        model.addAttribute("blog",  blog);

        return "/fragments/forwardform";
    }


    /**
     * 根据视频播放页面获取视频信息
     * @param url
     * @return
     */
    @RequestMapping(value = "getVideoInfo", method = RequestMethod.POST)
    public @ResponseBody Video getVideoInfo(@RequestParam(value="url",required=true) String url) {

        if(logger.isDebugEnabled())logger.debug("#########################"+url);
        return this.videoService.getVideoInfo(url);
    }



    /**
     * 根据页面获取页面信息，图片和title
     */
    @RequestMapping(value = "getPageInfo", method = RequestMethod.POST)
    public @ResponseBody Page getPageInfo(@RequestParam(value="url",required=true) String url) {

        if(logger.isDebugEnabled())logger.debug("#########################"+url);
        return HTMLParser.getHtmlInfo(url);
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
            int originalwidth = FileUtil.getImageWidth(logo.getInputStream());
            String url=this.imageService.uploadFileHandle(logo.getBytes(), LoginHelper.getUserId(), logo.getOriginalFilename(),originalwidth);

            return url;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "null";
    }


//	@RequestMapping("/photos")
//	public String photos(ModelMap model) throws Exception {
//		String message = this.oauth2AccesService.getPhotoListJson();
//		if(logger.isDebugEnabled())logger.debug("---------------------------> message :"+message);
//		model.addAttribute("message", message);
//		return "message";
//	}


}
