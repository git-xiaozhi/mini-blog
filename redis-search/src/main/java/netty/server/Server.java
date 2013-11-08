package netty.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;




import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;



public class Server {
	
	private static final int SERVER_PORT = 8000;

	
	public Server() {
		
	}
	
	//@PostConstruct
	public void run(){
		
		
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new ServerHannelFactory());
        
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        
        bootstrap.bind(new InetSocketAddress(SERVER_PORT));
        
        
		System.out.println("###############################  : Server Listening on port "+SERVER_PORT);		
	}

}
