package netty.server;

import static org.jboss.netty.channel.Channels.pipeline;

import javax.net.ssl.SSLEngine;

import netty.ssl.BogusSslContextFactory;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.logging.InternalLogLevel;
import org.jboss.netty.util.HashedWheelTimer;

public class ServerHannelFactory implements ChannelPipelineFactory{

	private boolean isSLL = false;
	
    @Override
    public ChannelPipeline getPipeline() throws Exception
    {
        ChannelPipeline pipleline = pipeline();
        
        if(isSLL){
          SSLEngine engine = BogusSslContextFactory.getInstance(true).createSSLEngine();
          engine.setUseClientMode(false);
          pipleline.addLast("ssl", new SslHandler(engine));
        }
        
        pipleline.addLast("encode", new ObjectEncoder());
        pipleline.addLast("decode", new ObjectDecoder());
        //10秒没有数据读取，则Timeout
        //pipleline.addLast("timeout",new ReadTimeoutHandler(new HashedWheelTimer(),10));
        
        //此两项为添加心跳机制 10秒查看一次在线的客户端channel是否空闲，IdleStateHandler为netty jar包中提供的类
        //pipleline.addLast("timeout", new IdleStateHandler(new HashedWheelTimer(), 10, 10, 0));
        //pipleline.addLast("hearbeat", new Heartbeat());//此类 实现了IdleStateAwareChannelHandler接口
        
        pipleline.addLast("handler", new ServerHandler());
        pipleline.addLast("log",new LoggingHandler(InternalLogLevel.INFO));
        return pipleline;
    }
}
