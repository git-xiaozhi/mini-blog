package netty.server.coder;
 
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
 
/**
 *  @author hankchen
 *  2012-2-3 上午10:47:54
 */
 
/**
 * 客户端解码器
 */
public class XLClientDecoder extends LengthFieldBasedFrameDecoder {
 
    public XLClientDecoder() {
    	super(1048576, 12, 4);
		// TODO Auto-generated constructor stub
	}

	@Override
    protected Object decode(ChannelHandlerContext context, Channel channel, ChannelBuffer buffer1) throws Exception {
		
		ChannelBuffer frame = (ChannelBuffer) super.decode(context, channel,buffer1);
		if (frame == null) {
			return null;
		}
		
        byte encode=frame.readByte();
        byte encrypt=frame.readByte();
        byte extend1=frame.readByte();
        byte extend2=frame.readByte();
        int sessionid=frame.readInt();
        int result=frame.readInt();
        int length=frame.readInt(); // 数据包长

        ChannelBuffer dataBuffer=ChannelBuffers.buffer(length);
        frame.readBytes(dataBuffer, length);
         
        XLResponse response=new XLResponse();
        response.setEncode(encode);
        response.setEncrypt(encrypt);
        response.setExtend1(extend1);
        response.setExtend2(extend2);
        response.setSessionid(sessionid);
        response.setResult(result);
        response.setLength(length);
        response.setValues(ProtocolUtil.decode(encode, dataBuffer));
        response.setIp(ProtocolUtil.getClientIp(channel));
        return response;
    }
 
}