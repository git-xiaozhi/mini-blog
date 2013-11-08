/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package netty.server;

import java.net.InetSocketAddress;

import javassist.expr.NewArray;


import netty.Reflection;
import netty.model.BrodcastMessage;
import netty.model.OperatorMessage;
import netty.server.coder.XLClientDecoder;
import netty.server.coder.XLResponse;
import netty.server.coder.XLClientEncoder;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.handler.ssl.SslHandler;



public class ProxyInServerboundHandler extends SimpleChannelUpstreamHandler {

    private final ClientSocketChannelFactory cf;
    private final String remoteHost;
    private final int remotePort;

    // This lock guards against the race condition that overrides the
    // OP_READ flag incorrectly.
    // See the related discussion: http://markmail.org/message/x7jc6mqx6ripynqf
    final Object trafficLock = new Object();

    private volatile Channel outboundChannel;
    

    public ProxyInServerboundHandler(
            ClientSocketChannelFactory cf, String remoteHost, int remotePort) {
        this.cf = cf;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    
    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        // Suspend incoming traffic until connected to the remote host.
    	final Channel inboundChannel = e.getChannel();
        inboundChannel.setReadable(false);

        // Start the connection attempt.
        ClientBootstrap cb = new ClientBootstrap(cf);
        
        /**自定义编码解码器*/
        cb.getPipeline().addLast("encode", new XLClientEncoder());
        cb.getPipeline().addLast("decode", new XLClientDecoder());
        
        cb.getPipeline().addLast("handler", new OutboundHandler(e.getChannel()));
        
        ChannelFuture f = cb.connect(new InetSocketAddress(remoteHost, remotePort));

        outboundChannel = f.getChannel();
        f.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    // Connection attempt succeeded:
                    // Begin to accept incoming traffic.
                    inboundChannel.setReadable(true);
                } else {
                    // Close the connection if the connection attempt has failed.
                    inboundChannel.close();
                }
            }
        });
    }
    
    
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
    public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e)
            throws Exception {
    	
		Object message = e.getMessage();
		if(message instanceof OperatorMessage){
		  OperatorMessage om = (OperatorMessage) message;
		  /**本代理服务器直接处理请求*/
		  if(om.getLogin()){
		   e.getChannel().write(excMethod(om));//执行用户方法
		   /**请求被代理服务器*/
		  }else{
	        synchronized (trafficLock) {
	        	outboundChannel.write(excMethod(om));		        	
	            // If outboundChannel is saturated, do not read until notified in
	            // OutboundHandler.channelInterestChanged().
	            if (!outboundChannel.isWritable()) {
	                e.getChannel().setReadable(false);
	            }
	        }
			  
		  }
		}
    	

    }

    @Override
    public void channelInterestChanged(ChannelHandlerContext ctx,
            ChannelStateEvent e) throws Exception {
        // If inboundChannel is not saturated anymore, continue accepting
        // the incoming traffic from the outboundChannel.
        synchronized (trafficLock) {
            if (e.getChannel().isWritable()) {
                if (outboundChannel != null) {
                    outboundChannel.setReadable(true);
                }
            }
        }
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        if (outboundChannel != null) {
            closeOnFlush(outboundChannel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        e.getCause().printStackTrace();
        closeOnFlush(e.getChannel());
    }

    /**
     * 被代理服务器消息处理
     * @author xiaozhi
     *
     */
    private class OutboundHandler extends SimpleChannelUpstreamHandler {
    	
        private final Channel inboundChannel;

        OutboundHandler(Channel inboundChannel) {
            this.inboundChannel = inboundChannel;
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e)
                throws Exception {
            //ChannelBuffer msg = (ChannelBuffer) e.getMessage();
            
            synchronized (trafficLock) {
            	//这里需要转换数据类型写入客户端通道中msg--->返回对象
		        XLResponse xLResponse = (XLResponse)e.getMessage();
		        if(xLResponse.getResult()!=0){
	        	    inboundChannel.write(xLResponse.getValues());//普通方法调用回应
		        }else{
		        	System.out.println("------------------------------------------>"+xLResponse.getValue("time"));
		        	//inboundChannel.write(new BrodcastMessage(xLResponse.getValue("time")));//系统广播
		        	inboundChannel.write(new BrodcastMessage(xLResponse.getValue("time")));
		        }
		        
                // If inboundChannel is saturated, do not read until notified in
                // HexDumpProxyInboundHandler.channelInterestChanged().
                if (!inboundChannel.isWritable()) {
                    e.getChannel().setReadable(false);
                }
            }
        }

        @Override
        public void channelInterestChanged(ChannelHandlerContext ctx,
                ChannelStateEvent e) throws Exception {
            // If outboundChannel is not saturated anymore, continue accepting
            // the incoming traffic from the inboundChannel.
            synchronized (trafficLock) {
                if (e.getChannel().isWritable()) {
                    inboundChannel.setReadable(true);
                }
            }
        }

        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            closeOnFlush(inboundChannel);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
                throws Exception {
            e.getCause().printStackTrace();
            closeOnFlush(e.getChannel());
        }
    }

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        if (ch.isConnected()) {
        	ch.close();
            //ch.write(ChannelBuffers.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
    
	private Object excMethod(OperatorMessage om) throws Exception{
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
        
       @Override
       public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                ServerContant.allChannels.add(future.getChannel());//将接入的连接加入ChannelGroup
            } else {
                future.getChannel().close();
            }
        }
    }
}
