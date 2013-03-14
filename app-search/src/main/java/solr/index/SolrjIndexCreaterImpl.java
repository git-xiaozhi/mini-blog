package solr.index;

import java.io.IOException;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author xiaozhi
 * @version 2010-7-20 下午02:57:04
 * @param <T>
 */



@Service("solrjIndexCreater")
public class SolrjIndexCreaterImpl<T> implements SolrjIndexCreater<T>{

    private static Log logger = LogFactory.getLog(SolrjIndexCreaterImpl.class);

    @Value(value = "#{globalProperties['solr.soTimeOut']}")
    private int soTimeOut;
    @Value(value = "#{globalProperties['solr.connectionTimeOut']}")
    private int connectionTimeOut;
    @Value(value = "#{globalProperties['solr.maxConnectionsPerHost']}")
    private int maxConnectionsPerHost;
    @Value(value = "#{globalProperties['solr.maxTotalConnections']}")
    private int maxTotalConnections;
    @Value(value = "#{globalProperties['solr.maxRetries']}")
    private int maxRetries;



    //@Autowired
    private ConcurrentUpdateSolrServer concurrentUpdateSolrServer;



    /* (non-Javadoc)
     * @see com.solr.test.SolrjIndexCreater#addOrUpdateBeans(java.util.List)
     */
    @Override
    public void addOrUpdateBeans(List<T> items)  {
        try {
        	concurrentUpdateSolrServer.addBeans(items);
        	concurrentUpdateSolrServer.commit();
        } catch (SolrServerException e) {
            errorHandler(e);
        } catch (IOException e) {
            errorHandler(e);
        }
    }

    @Override
    public void addOrUpdateBean(T item)  {
        try {
        	concurrentUpdateSolrServer.addBean(item);
        	concurrentUpdateSolrServer.commit();
        } catch (SolrServerException e) {
            errorHandler(e);
        } catch (IOException e) {
            errorHandler(e);
        }
    }

    @Override
    public void optimize()  {
        try {
        	concurrentUpdateSolrServer.optimize();
        } catch (SolrServerException e) {
            errorHandler(e);
        } catch (IOException e) {
            errorHandler(e);
        }
    }


    /* (non-Javadoc)
     * @see com.solr.test.SolrjIndexCreater#deleteDocById(java.lang.String)
     */
    @Override
    public void deleteDocById(String id) {
    	concurrentUpdateSolrServer.setRequestWriter(new BinaryRequestWriter());
        try {
        	concurrentUpdateSolrServer.deleteByQuery("id:*"+id);
        	concurrentUpdateSolrServer.commit();
        } catch (SolrServerException e) {
            errorHandler(e);
        } catch (IOException e) {
            errorHandler(e);
        }
    }


    /* (non-Javadoc)
     * @see com.solr.test.SolrjIndexCreater#clear()
     */
    @Override
    public void clear() {
    	concurrentUpdateSolrServer.setRequestWriter(new BinaryRequestWriter());
        try {
        	concurrentUpdateSolrServer.deleteByQuery("*:*");
        	concurrentUpdateSolrServer.commit();
        } catch (SolrServerException e) {
            errorHandler(e);
        } catch (IOException e) {
            errorHandler(e);
        }
    }



    private void errorHandler(Throwable cause) {
        cause.printStackTrace();
    }

	public void setConcurrentUpdateSolrServer(ConcurrentUpdateSolrServer concurrentUpdateSolrServer) {
		this.concurrentUpdateSolrServer = concurrentUpdateSolrServer;
	}


}