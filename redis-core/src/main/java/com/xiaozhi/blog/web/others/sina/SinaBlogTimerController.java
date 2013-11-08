package com.xiaozhi.blog.web.others.sina;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import weibo4j.model.WeiboException;



import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.service.other.sina.SinaBlogService;
import com.xiaozhi.blog.utils.FileUtil;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.utils.SinaDateForm;
import com.xiaozhi.blog.vo.SinaPost;


/**
 * 微博定时发送
 * @author xiaozhi
 *
 */
@Controller
@RequestMapping("/blog/sina")
public class SinaBlogTimerController {
	
	private static Log logger = LogFactory.getLog(SinaBlogTimerController.class);
	
	@Value(value="#{globalProperties['blog.list.pagesize']}")
    private int pageSize;
	
	@Autowired
	private SinaBlogService sinaBlogService;

    /**
     * 图片上传
     * @param model
     * @param logo
     * @param resquest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value="timer")
    public @ResponseBody String upload(ModelMap model,@RequestParam("filename") MultipartFile logo,HttpServletRequest  resquest ) {

        try {
            byte[] a = logo.getBytes();
            String filePath = resquest.getRealPath(resquest.getServletPath());
            String name=FileUtil.sinaUploadFile(a, filePath, LoginHelper.getUserId(), logo.getOriginalFilename());
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
     * 产生定时发布微博
     * @param content
     * @param pic
     * @param delaySeconds
     * @param resquest
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "saveSinaBlog", method = RequestMethod.POST)
    public String updateStatus(SinaDateForm dateForm,HttpServletRequest  resquest,Model model) throws IOException {
    	SinaPost post = null;
    	long time = System.currentTimeMillis();
		try {
			if(dateForm.getPic()==null || "".equals(dateForm.getPic())){
			   post = new SinaPost(time, LoginHelper.getUserId(), dateForm.getContent(), dateForm.getFutureDate());			   
			}else{
    	      String filePath = resquest.getRealPath(dateForm.getPic());
    	       post = new SinaPost(time, LoginHelper.getUserId(), dateForm.getContent(), filePath, dateForm.getPic(), dateForm.getFutureDate());
		   }
			post = this.sinaBlogService.saveSinaPost(post);
			model.addAttribute("p",post);
            return "/blog/sina/timer/newpost";
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
    
    
    /**
     * 删除定时微博
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "removeSinaBlog/{id}", method = RequestMethod.POST)
    public @ResponseBody boolean removeSinaBlog(@PathVariable long id,Model model){
		try {
            this.sinaBlogService.deleteSinapost(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

        return false;
    }
    
    
    
    @RequestMapping(value = "getSinaBlogs", method = RequestMethod.GET)
    public String getAllSinaBlogs(@RequestParam(required = false,defaultValue="1") Integer page,HttpServletRequest request,Model model) throws IOException, WeiboException {
      
    	ListPage<SinaPost> pagelist = this.sinaBlogService.getSinaBlogsByPage(LoginHelper.getUserId(), page, pageSize);
    	model.addAttribute("pagelist", pagelist);
    	model.addAttribute("sessionId", request.getSession().getId());

        return "/blog/sina/timer/hometimeline";
    }
    
    /**
     * 分页链接
     * @param page
     * @param model
     * @return
     * @throws IOException
     * @throws WeiboException 
     */
    @RequestMapping(value = "getSinaBlogsPage", method = RequestMethod.GET)
    public String getSinaBlogsByPage(@RequestParam(required = false,defaultValue="1") Integer page,Model model) throws IOException, WeiboException {
      
    	ListPage<SinaPost> pagelist = this.sinaBlogService.getSinaBlogsByPage(LoginHelper.getUserId(), page, pageSize);
    	model.addAttribute("pagelist", pagelist);

        return "/blog/sina/timer/post";
    }

    
    
    
}
