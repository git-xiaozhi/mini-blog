package netty.server;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

public class Heartbeat extends IdleStateAwareChannelHandler {

	int i = 0;

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e)throws Exception {
		// TODO Auto-generated method stub
		super.channelIdle(ctx, e);

		if (e.getState() == IdleState.READER_IDLE)
			i++;

		if (i == 3) {
			e.getChannel().close();
			System.out.println("----------------客户端掉线了。");
		}
	}

}