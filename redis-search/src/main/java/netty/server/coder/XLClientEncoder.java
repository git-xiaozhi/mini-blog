package netty.server.coder;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 *  @author hankchen
 *  2012-2-3 上午10:48:15
 */
 
/**
 * 客户端编码器
 */
public class XLClientEncoder extends OneToOneEncoder {
    Logger logger=LoggerFactory.getLogger(XLClientEncoder.class);
     
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		
		if(!(msg instanceof XLRequest)) {
			return msg;
		}
		
    	XLRequest request=(XLRequest)msg;
    	ChannelBuffer totalBuffer=ChannelBuffers.dynamicBuffer();
        /**
         * 先组织报文头
         */
    	totalBuffer.writeByte(request.getEncode());
    	totalBuffer.writeByte(request.getEncrypt());
    	totalBuffer.writeByte(request.getExtend1());
    	totalBuffer.writeByte(request.getExtend2());
    	totalBuffer.writeInt(request.getSessionid());
    	totalBuffer.writeInt(request.getCommand());
         
        /**
         * 组织报文的数据部分
         */
        ChannelBuffer dataBuffer=ProtocolUtil.encode(request.getEncode(),request.getParams()); 
        int length=dataBuffer.readableBytes();
        totalBuffer.writeInt(length);
        totalBuffer.writeBytes(dataBuffer);

		return totalBuffer;
	}
 
}