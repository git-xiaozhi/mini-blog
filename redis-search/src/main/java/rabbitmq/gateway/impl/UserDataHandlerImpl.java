package rabbitmq.gateway.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import rabbitmq.gateway.UserDataHandler;
import solr.index.SolrjIndexCreater;
import solr.index.UserIndexData;



@Service("userDataHandler")
public class UserDataHandlerImpl implements UserDataHandler {

	private static final Logger logger = LoggerFactory.getLogger(UserDataHandlerImpl.class);

	@Qualifier("userIndexCreater")
    @Autowired
    private SolrjIndexCreater<UserIndexData> userIndexCreater;

	/* (non-Javadoc)
	 * @see com.xiaozhi.rabbitmq.gateway.impl.UserDataHandler#handleUserDataMessage(com.xiaozhi.rabbitmq.User)
	 */
	public void handleUserDataMessage(UserIndexData user) {

		logger.debug("------------------------> recive user :"+user.toString());
		this.userIndexCreater.addOrUpdateBean(user);

	}

}
