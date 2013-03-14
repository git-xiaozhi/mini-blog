package netty.server;


import java.util.Map;

import netty.model.User;
import netty.server.coder.XLRequest;


public class ProxyService {
	
	
	
	public XLRequest helloUser(User user){
		/**这里需要将message 转化成同被代理服务器通讯的数据格式*/
		XLRequest xlRequest = new XLRequest();
		xlRequest.setEncode((byte)0);
		xlRequest.setCommand(1);
		xlRequest.setValue("name", user.getName());
		xlRequest.setValue("password", user.getPassword());
		
		return xlRequest;
	}
	
	
	public User helloUser(Map<String, Object> map){
		
		return null;
	}

}
