package netty.server;

import netty.model.BrodcastMessage;
import netty.model.User;


public class Test {


	public String hello(String name){
		return name;
	}

	public User helloUser(User user){

		return user;
	}
	
	public User validateUser(User user){
		
		return user;
	}
	
	
	
    public static void broadcastMessage(String message) throws InterruptedException{
		//ServerManger.broadcast(new BrodcastMessage("---------------> brodcast "+message+" from Server"));
	}

}
