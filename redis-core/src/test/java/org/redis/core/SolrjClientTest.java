package org.redis.core;



import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import callback.CallbackService;

import com.xiaozhi.blog.service.BlogService;
import com.xiaozhi.blog.service.UserService;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.utils.SolrPage;
import com.xiaozhi.blog.vo.User;

import solr.index.BlogIndexData;
import solr.index.UserIndexData;
import solr.search.BlogData;
import solr.search.EchoService;
import solr.search.SolrQueryService;
import solr.search.UserData;
import callback.CallbackListener;




//@Ignore
public class SolrjClientTest extends ServiceTestBase implements Serializable{


    @Autowired
    @Qualifier("userSolrQueryClient")
    private SolrQueryService<UserData> userSolrQueryClient;
    @Autowired
    @Qualifier("blogSolrQueryClient")
    private SolrQueryService<BlogData> blogSolrQueryClient;

    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;

    @Autowired
    private EchoService echoService;


    @Test
    public void testUserQuery(){

        String keyword = "*";

        try {
            SolrPage<UserData> items = userSolrQueryClient.queryHightLighting("1",keyword, 1, 10,true);
            logger.debug("-------------------->"+items.toString());
            for(UserData user :items.getList()){
                logger.debug("-------------------->"+user.toString());
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
//
//    @Test
//    public void addorUpdateUserBeans(){
//
//        ListPage<UserIndexData> blogsPage= this.userService.getGlobalUsers(1,10);
//        userIndexCreater.addOrUpdateBeans(blogsPage.getList());
//        if(blogsPage.getPages()>=2){
//         for(int i=2;i<=blogsPage.getPages();i++){
//            ListPage<UserIndexData> page= this.userService.getGlobalUsers(i,10);
//            userIndexCreater.addOrUpdateBeans(page.getList());
//         }
//        }
//    }



    @Test
    public void testBlogQuery(){

        String keyword = "国家";

        try {
            SolrPage<BlogData> items = blogSolrQueryClient.queryHightLighting(keyword, 1, 10,true);
            logger.debug("-------------------->"+items.toString());
            if(items.getCounts()!=null)
            for(BlogData blogData :items.getList()){
                logger.debug("-------------------->"+blogData.toString());
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


//    @Test
//    public void addorUpdateBlogBeans(){
//
//        ListPage<BlogIndexData> usersPage= this.blogService.timeline(1,10);
//        blogIndexCreater.addOrUpdateBeans(usersPage.getList());
//        if(usersPage.getPages()>=2){
//         for(int i=2;i<=usersPage.getPages();i++){
//            ListPage<BlogIndexData> page= this.blogService.timeline(i,10);
//            blogIndexCreater.addOrUpdateBeans(page.getList());
//         }
//        }
//
//
//    }

    @Test
    public void echo(){

    	String echo = this.echoService.echo("肖治");
    	logger.debug("-------------------------------> echo :"+echo);
    }

    @Test
    public void echoList(){

    	List<User> echo = this.echoService.echoList();
    	for(User s :echo){
    	  logger.debug("-------------------------------> user :"+s.toString());
    	}
    }

    @Test
    public void echoPage(){

    	ListPage<User> echo = this.echoService.echoPage();
    	  logger.debug("-------------------------------> echoPage :"+echo.toString());
    }


}
