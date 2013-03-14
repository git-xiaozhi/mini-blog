package rabbitmq.gateway.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import rabbitmq.gateway.BlogDataHandler;
import solr.index.BlogIndexData;
import solr.index.SolrjIndexCreater;



@Service("blogDataHandler")
public class BlogDataHandlerImpl implements BlogDataHandler {

	private static final Logger logger = LoggerFactory.getLogger(BlogDataHandlerImpl.class);

	@Qualifier("blogIndexCreater")
    @Autowired
    private SolrjIndexCreater<BlogIndexData> blogIndexCreater;


	/* (non-Javadoc)
	 * @see rabbitmq.gateway.impl.BlogDataHandler#handleBlogDataMessage(solr.index.BlogIndexData)
	 */
	@Override
	public void handleBlogDataMessage(BlogIndexData blog) {

		logger.debug("------------------------> recive blog :"+blog.toString());
		this.blogIndexCreater.addOrUpdateBean(blog);

	}

}
