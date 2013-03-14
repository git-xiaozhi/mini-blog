package rabbitmq.gateway;

import solr.index.UserIndexData;






public interface UserDataHandler {

	public abstract void handleUserDataMessage(UserIndexData user) throws Exception;

}