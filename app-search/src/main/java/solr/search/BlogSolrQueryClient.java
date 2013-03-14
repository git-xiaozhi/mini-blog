package solr.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.xiaozhi.blog.mongo.MongoBlogDao;
import com.xiaozhi.blog.utils.Collections3;
import com.xiaozhi.blog.utils.SolrPage;
import com.xiaozhi.blog.vo.WebPost;



//@Service("blogSolrQueryClient")
public class BlogSolrQueryClient extends SolrQueryImpl<BlogData> implements SolrQueryService<BlogData>{


    private static Log logger = LogFactory.getLog(BlogSolrQueryClient.class);

    @Autowired
    @Qualifier("blogSearchServer")
    private HttpSolrServer blogSearchServer;
    @Autowired
    private MongoBlogDao mongoBlogDao;

    @Override
    public SolrPage<BlogData> query(String keyword,int page,int pageSize,boolean isFacet) throws Exception{
        int startIndex = (page-1)*pageSize;
        //设置查询字段和条件
        Map<String, String> propertyMap = new ConcurrentHashMap<String, String>();
        propertyMap.put("text", keyword);
        //排序有顺序,使用TreeMap,如果没排序，则是默认按照score排序
//		Map<String, String> compositorMap = new TreeMap<String, String>();
//		compositorMap.put("cul_rid", "desc");

        SolrResult<BlogData> items = this.query(propertyMap, null, null,BlogData.class, startIndex, pageSize,isFacet);

        return new SolrPage<BlogData>(startIndex, startIndex+pageSize-1, items);
    }

    @Override
    public SolrPage<BlogData> queryHightLighting(String keyword,int page,int pageSize,boolean isFacet) throws Exception{
        int startIndex = (page-1)*pageSize;
        //设置查询字段和条件
        Map<String, String> propertyMap = new ConcurrentHashMap<String, String>();
        propertyMap.put("text", keyword);

        //设置高亮字段
        String[] highlightArray = new String[]{"content","forwardcontent"};
        String[] fields = new String[]{"blogcontent","forwardcontent"};

        SolrResult<BlogData> items = this.query(propertyMap, null, highlightArray,BlogData.class, startIndex, pageSize,isFacet);
        if(null!=items && null!=items.getDocs()){
            Map<String, Map<String, List<String>>> highlightMap = items.getHighlightMap();
            List<String> idList= new ArrayList<String>();//转发微博id集合
            List<String> keys = Collections3.extractToList(items.getDocs(), "blogid");//微博id集合
            Map<String,WebPost> postMap = mongoBlogDao.convertrelayPidsToPosts(keys);//获取当前页微博集合
            for(BlogData doc:items.getDocs()){
            	WebPost webPost = postMap.get(doc.getBlogid());
            	if(webPost==null)continue;
            	if(webPost.getTransmitid()!=null)idList.add(webPost.getTransmitid());
            	BeanUtils.copyProperties(webPost, doc, fields);

                if(highlightMap!=null && highlightMap.containsKey(doc.getBlogid())){
                    for(String field:fields){

                      List<String> values = highlightMap.get(doc.getBlogid()).get(field.equals("blogcontent")==true?"content":field);

                      if(null!=values) {
                    	  SolrjCommonUtil.invokSetMethod(doc,field,values.get(0));
                      }
                    }
                }
            }
            Map<String,WebPost> transmitMap = mongoBlogDao.convertrelayPidsToPosts(idList);//获取当前页转发微博转发内容
            for(BlogData doc:items.getDocs()){
            	if(doc.getTransmitid()!=null)doc.setWebPost(transmitMap.get(doc.getTransmitid()));
            }

        }

        SolrPage<BlogData> solrPage = new SolrPage<BlogData>(startIndex, startIndex+pageSize-1, items);
        //if(logger.isDebugEnabled())logger.debug("-------------------->solrPage :"+solrPage);
        return solrPage;
    }


    @PostConstruct
    public void init() {
       this.setCommonsHttpSolrServer(this.blogSearchServer);
    }

    @Override
    public SolrPage<BlogData> query(String uid, String keyword, int page,
            int pageSize, boolean isFacet) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SolrPage<BlogData> queryHightLighting(String uid, String keyword,
            int page, int pageSize, boolean isFacet) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
