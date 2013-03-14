package netty.server;

import netty.model.BrodcastMessage;


public class ServerManger {

	/**
	 * 广播消息
	 * @param message
	 */
	public static void broadcast(BrodcastMessage message) {
		System.out.println("channel-pool size : "+ ServerContant.allChannels.size());
		ServerContant.allChannels.write(message);
		//广播消息可以如上那么发
//		for (Channel channel : ServerContant.allChannels) {
//			if (channel.isConnected()) {
//				channel.write(message);
//			}
//		}
	}

}
