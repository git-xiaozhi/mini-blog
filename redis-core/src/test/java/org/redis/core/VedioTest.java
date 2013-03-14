package org.redis.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import com.xiaozhi.blog.service.UserService;
import com.xiaozhi.blog.service.VideoService;
import com.xiaozhi.blog.service.page.HTMLParser;
import com.xiaozhi.blog.utils.Im4javaUitl;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.vo.Page;
import com.xiaozhi.blog.vo.User;
import com.xiaozhi.blog.vo.Video;
import com.xiaozhi.blog.vo.WebPost;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;





/**
 * HTML文本解析
 * @author xiaozhi
 *
 */
//@Ignore
public class VedioTest extends ServiceTestBase{

    private static Log logger = LogFactory.getLog(VedioTest.class);

    @Autowired
    private VideoService videoService;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;


    @Test
    public void testaddUserAop(){

    	User  user = userService.getUserById("1");
    	boolean rescult = userService.addUser(user);
        logger.debug("---------------------> rescult :"+rescult);
    }

    @Test
    public void testUserServiceCache(){

    	User  user = userService.getUserById("1");
        logger.debug("---------------------> user :"+user);
    }

    @Test
    public void VideoTest(){
         //String url = "http://v.youku.com/v_show/id_XMjU0MjI2NzY0.html";
         String url = "http://www.tudou.com/programs/view/pVploWOtCQM/";
        //String url ="http://v.ku6.com/special/show_4024167/9t7p64bisV2A31Hz.html";
         //String url = "http://v.ku6.com/show/BpP5LeyVwvikbT1F.html";
         //String url = "http://6.cn/watch/14757577.html";
        //String url = "http://www.56.com/u64/v_NTkzMDEzMTc.html";
        //Video video = getVideoInfo(url);
//		System.out.println("视频缩略图：" + video.getPic());
//		System.out.println("视频地址：" + video.getFlash());
//		System.out.println("视频时长：" + video.getTime());

        Video video = videoService.getVideoInfo(url);
        logger.debug("---------------------->"+video);
    }

    @Test
    public void testHtmlParser(){

        Page page=HTMLParser.getHtmlInfo("http://www.360buy.com/");
        logger.debug("--------------------->"+page.toString());
    }


    @Test
    public void testIm4java(){


        /** 直接对文件压缩 */
        //boolean result = this.imageOperationService.resiizeImage(40, 50,"/home/xiaozhi/87054.jpg", "/home/xiaozhi/zip.jpg");

        try {
            File f = ResourceUtils.getFile("file:D:\\DSCF0126.jpg");
            /** 返回压缩后的byte数组 */
            byte[] a = Im4javaUitl.resiizeImage(40, 50,new FileInputStream(f));
            FileCopyUtils.copy(a, new File("D:\\s.jpg"));




        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void testMessageSource(){

        String result = this.messageSource.getMessage("[可爱]", null, null);
        logger.debug("#########################"+result);

    }

    @Test
    public void testPicSize(){

        HTMLParser.getSize("http://demo.tianji.com:8888/blog/upload/1/1322547433902.jpg");
    }

    @Test
    public void testPages(){

        ListPage<WebPost> page = this.restTemplate.getForObject("http://miniblog.com:8888/manage/collect/collects/1/1", ListPage.class);
        List<WebPost> items = page.getList();
        for(WebPost webPost:items){
            for(Video video :webPost.getWebPosts()){
                logger.debug("#########################"+video.toString());
            }

        }
    }



    @Test
    public void replaceReplies() {
        String content = "@肖治://@肖治:是啊//@肖治://@肖治:是啊";
        //final Pattern MENTION_REGEX = Pattern.compile("@[\\w]+");
        //final Pattern MENTION_REGEX = Pattern.compile("@([\\w]|[^x00-xff])+");
        final Pattern MENTION_REGEX = Pattern.compile("@([-_a-zA-Z0-9\u4e00-\u9fa5]{2,20}+)");

        //final Pattern MENTION_REGEX = Pattern.compile("@[^\\s]+");
        //$([^a-z0-9_]|^)([@|＠])([^A-Za-z0-9\u4E00-\u9FA5]{1,20})(^\w\u4E00-\u9FA5.-]{0,79})?$iu

        //^.{0,2}$|.{21,}|^[^A-Za-z0-9\u4E00-\u9FA5]|[^\w\u4E00-\u9FA5.-]|([_.-])\1

        Matcher regexMatcher = MENTION_REGEX.matcher(content);
        while (regexMatcher.find()) {
            String match = regexMatcher.group();
            int start = regexMatcher.start();
            int stop = regexMatcher.end();
            String uName = match.substring(1);
            logger.debug("==================== match :"+match);
            //logger.debug("==================== uName :"+uName);
        }
    }


    @Test
    public void testFaceImage() {
        String content = "[微笑]哈哈哈你不行 [话题]你可以的[懒得理你]";
        final Pattern MENTION_REGEX = Pattern.compile("\\[([^\\]\\[\\/ ]+)\\]");


        Matcher regexMatcher = MENTION_REGEX.matcher(content);
        String temp="";
        while (regexMatcher.find()) {
            String match = regexMatcher.group();
            logger.debug("==================== match :"+match);
            content = content.replace(match, "ww");

        }

        logger.debug("==================== content :"+content);
    }


    @Test
    public void url() {

        String aa = restTemplate.getForObject("http://demo.tianji.com:8888/blog/getVideoInfo", String.class);
        logger.debug("#################"+aa);
    }



}
