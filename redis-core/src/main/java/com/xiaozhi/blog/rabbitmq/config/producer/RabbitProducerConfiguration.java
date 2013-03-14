package com.xiaozhi.blog.rabbitmq.config.producer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xiaozhi.blog.rabbitmq.config.AbstractUserDataRabbitConfiguration;



/**
 *
 * @author RenLibin
 *
 */
//@Configuration
public class RabbitProducerConfiguration extends AbstractUserDataRabbitConfiguration{

	@Override
	protected void configureRabbitTemplate(RabbitTemplate rabbitTemplate) {
		//rabbitTemplate.setExchange(EXCHANGE_NAME);
	}


	@Bean
	public Binding marketDataBinding() {
		return BindingBuilder.bind(dataQueue()).to(marketDataExchange()).with(routingKey);
	}

	@Bean
	public Queue dataQueue() {
		return new Queue("user.data.queue",true, false, false);
	}
}
