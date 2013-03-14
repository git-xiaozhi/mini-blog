package rabbitmq.gateway;

import solr.index.BlogIndexData;

public interface BlogDataHandler {

	public abstract void handleBlogDataMessage(BlogIndexData blog);

}