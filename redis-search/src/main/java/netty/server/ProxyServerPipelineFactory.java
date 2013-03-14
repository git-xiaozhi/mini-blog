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

import static org.jboss.netty.channel.Channels.*;

import javax.net.ssl.SSLEngine;

import netty.ssl.BogusSslContextFactory;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.logging.InternalLogLevel;
import org.jboss.netty.util.HashedWheelTimer;

public class ProxyServerPipelineFactory implements ChannelPipelineFactory {

    private final ClientSocketChannelFactory cf;
    private final String remoteHost;
    private final int remotePort;
    
    private final boolean isSLL = true;
    
    static HashedWheelTimer hashedWheelTimer = new HashedWheelTimer();//单例的引用，防止每一次都创建一个实例
    
    /**为了提高并发数，一般通过ExecutionHandler线程池来异步处理ChannelHandler链*/
    static OrderedMemoryAwareThreadPoolExecutor e = new OrderedMemoryAwareThreadPoolExecutor(16, 0, 0);  
    static ExecutionHandler executionHandler = new ExecutionHandler(e);

    public ProxyServerPipelineFactory(
            ClientSocketChannelFactory cf, String remoteHost, int remotePort) {
        this.cf = cf;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline p = pipeline(); // Note the static import.
        
        if(isSLL){
            SSLEngine engine = BogusSslContextFactory.getInstance(true).createSSLEngine();
            engine.setUseClientMode(false);
            p.addLast("ssl", new SslHandler(engine));
          }
          
          p.addLast("encode", new ObjectEncoder());
          p.addLast("decode", new ObjectDecoder());
          //10秒没有数据读取，则Timeout
          //pipleline.addLast("timeout",new ReadTimeoutHandler(new HashedWheelTimer(),10));
          
          p.addLast("executor", executionHandler);
          //此两项为添加心跳机制 10秒查看一次在线的客户端channel是否空闲，IdleStateHandler为netty jar包中提供的类
          p.addLast("timeout", new IdleStateHandler(hashedWheelTimer, 10, 10, 0));
          p.addLast("hearbeat", new Heartbeat());//此类 实现了IdleStateAwareChannelHandler接口
          
          p.addLast("log",new LoggingHandler(InternalLogLevel.INFO));
          p.addLast("handler", new ProxyInServerboundHandler(cf, remoteHost, remotePort));
          
          
        return p;
    }
}
