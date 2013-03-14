package rabbitmq.config.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

//@Component
public class MyErrorHandler implements ErrorHandler{

	private static final Logger logger = LoggerFactory.getLogger(MyErrorHandler.class);


	@Autowired
	Binding marketDataBinding;
	@Autowired
	SimpleMessageListenerContainer messageListenerContainer;

	@Override
	public void handleError(Throwable t) {

		//messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.NONE);
		//amqpAdmin.removeBinding(marketDataBinding);

//		try {
//
//			messageListenerContainer.stop();
//			Thread.sleep(15000);
//			messageListenerContainer.start();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		logger.debug("-------------------------->error :"+t.toString());

	}

}
