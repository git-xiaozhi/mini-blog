package com.xiaozhi.blog.rabbitmq.gateway.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.support.RabbitGatewaySupport;
import org.springframework.beans.factory.annotation.Value;

import com.xiaozhi.blog.rabbitmq.gateway.RabbitBlogDataMessageGateway;

import solr.index.BlogIndexData;





public class RabbitBlogDataServiceGatewayImpl extends RabbitGatewaySupport implements RabbitBlogDataMessageGateway{

	private static final Logger logger = LoggerFactory.getLogger(RabbitBlogDataServiceGatewayImpl.class);

	//@Value("${basic.data.routing.key:user.basic.data.queue}")
	private String routingKey = "blog.basic.data.key";

	protected static String EXCHANGE_NAME = "blog.basic.data.exchange";


	/* (non-Javadoc)
	 * @see com.xiaozhi.blog.rabbitmq.gateway.impl.RabbitBlogDataMessageGateway#sendMessage(solr.index.BlogIndexData)
	 */
	@Override
	public void sendMessage(BlogIndexData blog) throws Exception {

//		try{
			//getRabbitTemplate().convertAndSend(routingKey,message);
			getRabbitTemplate().convertAndSend(EXCHANGE_NAME, routingKey, blog);
			if(logger.isDebugEnabled()){
				logger.debug("----------->Sending blog data message is " + blog.toString());
			}
			//throw new Exception();
//		}catch(Exception e){
//			logger.error(e.getMessage());
//		}
	}

}