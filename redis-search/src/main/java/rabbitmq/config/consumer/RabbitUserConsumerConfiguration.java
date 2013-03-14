package rabbitmq.config.consumer;


import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import rabbitmq.config.AbstractDataRabbitConfiguration;
import rabbitmq.gateway.UserDataHandler;




/**
 *
 * @author XiaoZhi
 *
 */
//@Configuration
public class RabbitUserConsumerConfiguration extends AbstractDataRabbitConfiguration{


	protected static String routingKey = "user.basic.data.key";
	protected static String EXCHANGE_NAME = "user.basic.data.exchange";

	//@Value("${basic.data.routing.key:user.basic.data.queue}")
	//private String routingKey = "user.basic.data.key";

	@Autowired
	private UserDataHandler userDataHandler;

	@Autowired
	private MyErrorHandler myErrorHandler;

//	@Autowired
//	private RabbitTransactionManager rabbitTransactionManager;

//	@Override
//	protected void configureRabbitTemplate(RabbitTemplate rabbitTemplate) {
//		rabbitTemplate.setRoutingKey(routingKey);
//	}


//	@Bean
//	public Binding marketDataBinding() {
//		return BindingBuilder.bind(dataQueue()).to(marketDataExchange());
//	}

	@Bean
	public DirectExchange marketDataExchange() {
		return new DirectExchange(EXCHANGE_NAME);
	}


	@Bean
	public Binding marketDataBinding() {
		return BindingBuilder.bind(dataQueue()).to(marketDataExchange()).with(routingKey);
	}

	@Bean
	public Queue dataQueue() {
		return new Queue("user.data.queue",true, false, false);
	}



	@Bean
	public SimpleMessageListenerContainer messageListenerContainer() {
	        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());

	        //container.setTransactionManager(rabbitTransactionManager);
	        container.setQueues(dataQueue());
	        //container.setChannelTransacted(true);
	        container.setErrorHandler(this.myErrorHandler);
	        //container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
	        container.setMessageListener(messageListenerAdapter());
	        //container.setDefaultRequeueRejected(false);
	        //container.setConcurrentConsumers(5);//消息顺序将不被保障
	        return container;

	}

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
    	MessageListenerAdapter mAdapter = new MessageListenerAdapter(userDataHandler, jsonMessageConverter());
    	mAdapter.setDefaultListenerMethod("handleUserDataMessage");
        return mAdapter;
    }

}
