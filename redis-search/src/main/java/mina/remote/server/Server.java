package mina.remote.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;

import javax.annotation.PostConstruct;



import mina.remote.ssl.BogusSslContextFactory;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class Server {
	private static final int SERVER_PORT = 8111;
	
	private static boolean isSLL = true;

	public Server() {
		
	}

	
	//@PostConstruct
	public void run() throws IOException, InterruptedException, GeneralSecurityException {
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
		
		
		/**加入SSL*/
		if(isSLL){
		  SslFilter sslFilter = new SslFilter(BogusSslContextFactory.getInstance(true));
		  //设置客户连接时需要验证客户端证书
		  sslFilter.setNeedClientAuth(true);
		  acceptor.getFilterChain().addLast("sslFilter", sslFilter);
		}
		
		
		acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		
		ServerHandler handler = new ServerHandler();
		acceptor.setHandler(handler);
		acceptor.bind(new InetSocketAddress(SERVER_PORT));
		System.out.println("###############################  : Listening on port 8111");		
	}

	public static void main(String args[]) throws Throwable {
		new Server().run();
//		Thread.sleep(10000);
//		Test.broadcastMessage();
	}
}
