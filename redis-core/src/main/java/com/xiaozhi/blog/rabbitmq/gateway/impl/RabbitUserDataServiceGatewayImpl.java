package com.xiaozhi.blog.rabbitmq.gateway.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.support.RabbitGatewaySupport;
import org.springframework.beans.factory.annotation.Value;

import solr.index.UserIndexData;

import com.xiaozhi.blog.rabbitmq.gateway.RabbitUserDataMessageGateway;




//@Service
public class RabbitUserDataServiceGatewayImpl extends RabbitGatewaySupport implements  RabbitUserDataMessageGateway{

	private static final Logger logger = LoggerFactory.getLogger(RabbitUserDataServiceGatewayImpl.class);

	//@Value("${basic.data.routing.key:user.basic.data.queue}")
	private String routingKey = "user.basic.data.key";

	protected static String EXCHANGE_NAME = "user.basic.data.exchange";


	public void sendMessage(UserIndexData user) throws Exception {

//		try{
			//getRabbitTemplate().convertAndSend(routingKey,message);
			getRabbitTemplate().convertAndSend(EXCHANGE_NAME, routingKey, user);
			if(logger.isDebugEnabled()){
				logger.debug("----------->Sending user data message is " + user.toString());
			}
			//throw new Exception();
//		}catch(Exception e){
//			logger.error(e.getMessage());
//		}
	}

}