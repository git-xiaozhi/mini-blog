package com.xiaozhi.blog.rabbitmq.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author XiaoZhi
 *
 */
//@Configuration
public abstract class AbstractUserDataRabbitConfiguration {

	@Value("${ampq.hostName:localhost}")
	private String hostName;
	@Value("${amqp.port:5672}")
    private int port = 5672;
	@Value("${ampq.userName:guest}")
	private String userName;
	@Value("${ampa.password:guest}")
	private String password;

//	@Value("${basic.data.routing.key:user.basic.data.queue}")
//    private String routingKey;

	protected static String routingKey = "user.basic.data.queue.key";
	protected static String EXCHANGE_NAME = "user.basic.data.exchange";

	protected   void configureRabbitTemplate(RabbitTemplate rabbitTemplate){

		//rabbitTemplate.setRoutingKey(routingKey);
	}



	@Bean
    public ConnectionFactory connectionFactory() {
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory(hostName);
            connectionFactory.setUsername(userName);
            connectionFactory.setPassword(password);
            connectionFactory.setPort(port);
            return connectionFactory;
    }

	@Bean
    public RabbitTemplate rabbitTemplate() {
            RabbitTemplate template = new RabbitTemplate(connectionFactory());
            template.setMessageConverter(jsonMessageConverter());
            //template.setChannelTransacted(true);
            configureRabbitTemplate(template);
            return template;
    }

	@Bean
    public MessageConverter jsonMessageConverter() {
            return new JsonMessageConverter();
    }

//	@Bean
//	public TopicExchange marketDataExchange() {
//		return new TopicExchange(EXCHANGE_NAME);
//	}

	@Bean
	public DirectExchange marketDataExchange() {
		return new DirectExchange(EXCHANGE_NAME);
	}

	/**
     * @return the admin bean that can declare queues etc.
     */
    @Bean
    public AmqpAdmin amqpAdmin() {
            RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
            return rabbitAdmin ;
    }
}
