package org.redis.core;

import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiaozhi.blog.rabbitmq.gateway.RabbitUserDataMessageGateway;
import com.xiaozhi.blog.vo.User;


public class RabbitMqTest extends ServiceTestBase{

	@Autowired
	private RabbitUserDataMessageGateway rabbitUserDataMessageGateway;

	@Test
	public void userMessagTest() throws Exception{
		int i=0;
		while(true){
		  i++;
		  User message = new User();
		  message.setId(Integer.toString(i));
		  message.setName("xiaozhi"+i);
		  //rabbitUserDataMessageGateway.sendMessage(message);
		  Thread.sleep(5000);
		}

	}

}
