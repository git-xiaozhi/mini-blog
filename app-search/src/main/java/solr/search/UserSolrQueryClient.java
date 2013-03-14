package solr.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.xiaozhi.blog.mongo.MongoFollowingDao;
import com.xiaozhi.blog.mongo.MongoUserDao;
import com.xiaozhi.blog.utils.Collections3;
import com.xiaozhi.blog.utils.SolrPage;
import com.xiaozhi.blog.vo.User;



//@Service("userSolrQueryClient")
public class UserSolrQueryClient extends SolrQueryImpl<UserData> implements SolrQueryService<UserData>{


    private static Log logger = LogFactory.getLog(UserSolrQueryClient.class);

    @Autowired
    @Qualifier("userSearchServer")
    private HttpSolrServer userSearchServer;
    @Autowired
    private MongoUserDao mongoUserDao;
    @Autowired
    private MongoFollowingDao mongoFollowingDao;

    @Override
    public SolrPage<UserData> query(String uid,String keyword,int page,int pageSize,boolean isFacet) throws Exception{
        int startIndex = (page-1)*pageSize;

        //设置查询字段和条件
        Map<String, String> propertyMap = new HashMap<String, String>();
        propertyMap.put("text", keyword);


        //排序有顺序,使用TreeMap,如果没排序，则是默认按照score排序
//		Map<String, String> compositorMap = new TreeMap<String, String>();
//		compositorMap.put("cul_rid", "desc");

        SolrResult<UserData> items = this.query(propertyMap, null, null,UserData.class, startIndex, pageSize,isFacet);

        return new SolrPage<UserData>(startIndex, startIndex+pageSize-1, items);
    }

    @Override
    public SolrPage<UserData> queryHightLighting(String uid,String keyword,int page,int pageSize,boolean isFacet) throws Exception{

        int startIndex = (page-1)*pageSize;
        //设置查询字段和条件
        Map<String, String> propertyMap = new HashMap<String, String>();
        propertyMap.put("text", keyword);
        //propertyMap.put("company", "公司");

        //获取关注集合
        List<String> followingList = this.mongoFollowingDao.getFollowings(uid);
        //设置高亮字段
        String[] highlightArray = new String[]{"nickname","company","school"};
        SolrResult<UserData> items = this.query(propertyMap, null, highlightArray,UserData.class, startIndex, pageSize,isFacet);
        UserData index= null;


        if(null!=items && null!=items.getDocs()){
            Map<String, Map<String, List<String>>> highlightMap = items.getHighlightMap();

            if(null!=items.getDocs()){
             int i=0;
             List<String> ids = Collections3.extractToList(items.getDocs(), "id");
             Map<String, User> userMap = mongoUserDao.getUsersByIds(ids);

             for(UserData doc:items.getDocs()){
                if(doc.getId().equals(uid)){//排除自己
                    index = doc;
                    continue;
                }
                User user = userMap.get(doc.getId());
                if(user==null)continue;
                if(followingList.contains(user.getId().toString())){//判断是否已关注
                	user.setLink(true);
                }else{
                	user.setLink(false);
                }

                BeanUtils.copyProperties(user, doc, highlightArray);
                if(highlightMap!=null && highlightMap.containsKey(doc.getId())){
                    for(String field:highlightArray){
                      List<String> values = highlightMap.get(doc.getId()).get(field);
                      if(null!=values) SolrjCommonUtil.invokSetMethod(doc,field,values.get(0));
                    }
                }
                i++;
             }
            }
        }

        if(null!=index)items.getDocs().remove(index);//排除自己
        return new SolrPage<UserData>(startIndex, startIndex+pageSize-1, items);
    }


    @PostConstruct
    public void init() {
       this.setCommonsHttpSolrServer(this.userSearchServer);
    }

    @Override
    public SolrPage<UserData> query(String keyword, int page, int pageSize,
            boolean isFacet) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SolrPage<UserData> queryHightLighting(String keyword, int page,
            int pageSize, boolean isFacet) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
