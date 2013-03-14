package com.xiaozhi.blog.rabbitmq.gateway;

import solr.index.BlogIndexData;

public interface RabbitBlogDataMessageGateway {

	public abstract void sendMessage(BlogIndexData blog) throws Exception;

}