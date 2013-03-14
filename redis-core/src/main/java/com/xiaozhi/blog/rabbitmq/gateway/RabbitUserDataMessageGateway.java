package com.xiaozhi.blog.rabbitmq.gateway;

import solr.index.UserIndexData;




/**
 *
 * @author RenLibin
 *
 */
public interface RabbitUserDataMessageGateway {

	public abstract void sendMessage(UserIndexData user) throws Exception;
}
