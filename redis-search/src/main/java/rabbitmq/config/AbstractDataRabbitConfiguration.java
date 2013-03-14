package rabbitmq.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author XiaoZhi
 *
 */
//@Configuration
public abstract class AbstractDataRabbitConfiguration {

	//@Value("${ampq.hostName:localhost}")
	private String hostName = "localhost";
	//@Value("${amqp.port:5672}")
    private int port = 5672;
	//@Value("${ampq.userName:guest}")
	private String userName = "guest";
	//@Value("${ampa.password:guest}")
	private String password = "guest";


//	protected  void configureRabbitTemplate(RabbitTemplate rabbitTemplate){
//		rabbitTemplate.setRoutingKey(routingKey);
//	}



	@Bean
    public ConnectionFactory connectionFactory() {
            //TODO make it possible to customize in subclasses.
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory(hostName);
            connectionFactory.setUsername(userName);
            connectionFactory.setPassword(password);
            connectionFactory.setPort(port);
            return connectionFactory;
    }

//	@Bean
//    public RabbitTemplate rabbitTemplate() {
//            RabbitTemplate template = new RabbitTemplate(connectionFactory());
//            template.setMessageConverter(jsonMessageConverter());
//            configureRabbitTemplate(template);
//            return template;
//    }

	@Bean
    public MessageConverter jsonMessageConverter() {
            return new JsonMessageConverter();
    }

//	@Bean
//	public FanoutExchange marketDataExchange() {
//		return new FanoutExchange(EXCHANGE_NAME);
//	}

	/**
     * @return the admin bean that can declare queues etc.
     */
    @Bean
    public AmqpAdmin amqpAdmin() {
            RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
            return rabbitAdmin ;
    }
}
