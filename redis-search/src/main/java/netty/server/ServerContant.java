package netty.server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import mina.remote.model.User;

import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;


public class  ServerContant extends ApplicationObjectSupport{
	
	public static final ChannelGroup allChannels = new DefaultChannelGroup("obj-server" ); ;//channel池
	
    public static final Set<User> USERS = Collections.synchronizedSet(new HashSet<User>());//用户池
    
    public static ApplicationContext CTX = null;

    @PostConstruct
	public  void setCTX() {
		CTX = this.getApplicationContext();
	}


    
    

}
