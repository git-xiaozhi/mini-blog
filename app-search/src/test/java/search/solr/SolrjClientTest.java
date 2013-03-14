package search.solr;



import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.utils.SolrPage;
import com.xiaozhi.blog.vo.User;

import solr.index.BlogIndexData;
import solr.index.SolrjIndexCreater;
import solr.index.UserIndexData;
import solr.search.BlogData;
import solr.search.EchoService;
import solr.search.SolrQueryService;
import solr.search.UserData;




//@Ignore
public class SolrjClientTest extends ServiceTestBase{


    @Autowired
    @Qualifier("userSolrQueryClient")
    private SolrQueryService<UserData> userSolrQueryClient;
    @Autowired
    @Qualifier("blogSolrQueryClient")
    private SolrQueryService<BlogData> blogSolrQueryClient;

    @Qualifier("userIndexCreater")
    @Autowired
    private SolrjIndexCreater<UserIndexData> userIndexCreater;
    @Qualifier("blogIndexCreater")
    @Autowired
    private SolrjIndexCreater<BlogIndexData> blogIndexCreater;


    @Autowired
    private EchoService echoService;


    /**
     * 清除所有用户信息索引数据
     */
    @Test
    public void testUserClearAll() {
        userIndexCreater.clear();
    }


    /**
     * 清除所有搏客信息索引数据
     */
    @Test
    public void testBlogClearAll() {
        blogIndexCreater.clear();
    }


    /**
     * 索引优化
     */
    @Test
    public void testUserOptimize(){
        userIndexCreater.optimize();
    }


    @Test
    public void testBlogOptimize(){
        blogIndexCreater.optimize();
    }

    /**
     * 根据主键删除doc(主键在schema.xml配置中配置)
     */
    @Test
    public void deleteDocById() {
        userIndexCreater.deleteDocById("102");
    }


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
