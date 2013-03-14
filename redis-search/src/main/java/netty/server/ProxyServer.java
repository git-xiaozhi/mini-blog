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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class ProxyServer {

    private final int localPort = 8000;
    private final String remoteHost = "127.0.0.1";
    private final int remotePort = 8111;

    public ProxyServer() {
    	
    }

    @PostConstruct
    public void run() {

        // Configure the bootstrap.
        Executor executor = Executors.newCachedThreadPool();
        ServerBootstrap sb = new ServerBootstrap(new NioServerSocketChannelFactory(executor, executor));

        // Set up the event pipeline factory.
        ClientSocketChannelFactory cf = new NioClientSocketChannelFactory(executor, executor);

        sb.setPipelineFactory(new ProxyServerPipelineFactory(cf, remoteHost, remotePort));
        
        sb.setOption("child.tcpNoDelay", true);
        sb.setOption("child.keepAlive", true);

        // Start up the server.
        sb.bind(new InetSocketAddress(localPort));
        
		System.out.println("###############################  : ProxyServer Listening on port "+localPort);
    }

}
