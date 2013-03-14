package solr.index;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import solr.search.BlogData;

@Service("blogIndexCreater")
public class BlogIndexCreater extends SolrjIndexCreaterImpl<BlogData> implements SolrjIndexCreater<BlogData> {

     private static Log logger = LogFactory.getLog(UserIndexCreater.class);

        @Autowired
        private ConcurrentUpdateSolrServer blogIndexServer;



        @PostConstruct
        public void init() {
           this.setConcurrentUpdateSolrServer(this.blogIndexServer);
        }

}
