package solr.index;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import solr.search.UserData;


@Service("userIndexCreater")
public class UserIndexCreater extends SolrjIndexCreaterImpl<UserData> implements SolrjIndexCreater<UserData>{


    private static Log logger = LogFactory.getLog(UserIndexCreater.class);

    @Autowired
    private ConcurrentUpdateSolrServer userIndexServer;



    @PostConstruct
    public void init() {
       this.setConcurrentUpdateSolrServer(this.userIndexServer);
    }

}
