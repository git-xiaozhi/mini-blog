package netty.server;


import netty.Reflection;
import netty.model.OperatorMessage;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.WriteCompletionEvent;
import org.jboss.netty.handler.ssl.SslHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ServerHandler extends SimpleChannelUpstreamHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger("/netty/server/ServerHandler");


	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		// Get the SslHandler in the current pipeline.
        // We added it in SecureChatPipelineFactory.
        final SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);

        // Get notified when SSL handshake is done.
        ChannelFuture handshakeFuture = sslHandler.handshake();
        handshakeFuture.addListener(new SslLister(sslHandler));
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		LOGGER.info("-----------Server channelDisconnected-----------");
		//closed channel会自动从group中移除所以不许要手工删除Channel
//		if(ServerContant.allChannels.contains(ctx.getChannel())){
//		  ServerContant.allChannels.remove(ctx.getChannel());
//		}
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		
		System.err.println("Server has a error,Error cause:"+e.getCause());
        e.getChannel().close();
	}


	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Object message = e.getMessage();
		if(message instanceof OperatorMessage){
		  System.out.println("Server --------------Message is: " + message.toString());		
		  OperatorMessage om = (OperatorMessage) message;
		  //e.getChannel().write(excMethod(om));//执行用户方法
		  e.getChannel().write("TEST is over!");
		}

	}


	public Object excMethod(OperatorMessage om) throws Exception{
		Class cls=Class.forName(om.getClassName());
        Object bean = ServerContant.CTX.getBean(cls);
        Object result = Reflection.invokeMethod(bean,om.getMethod(),om.getParams());
        return result;
		
	}
	
	
	
	
    private static final class SslLister implements ChannelFutureListener {

        private final SslHandler sslHandler;

        SslLister(SslHandler sslHandler) {
            this.sslHandler = sslHandler;
        }

        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                // Once session is secured, send a greeting.
//                future.getChannel().write(
//                        "Welcome to " + InetAddress.getLocalHost().getHostName() +
//                        " secure chat service!\n");
//                future.getChannel().write(
//                        "Your session is protected by " +
//                        sslHandler.getEngine().getSession().getCipherSuite() +
//                        " cipher suite.\n");

                // Register the channel to the global channel list
                // so the channel received the messages from others.
                ServerContant.allChannels.add(future.getChannel());//将接入的连接加入ChannelGroup
            } else {
                future.getChannel().close();
            }
        }
    }
	
}
