package netty.server.coder;
 
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
 
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
 
/**
 *  @author hankchen
 *  2012-2-4 下午01:57:33
 */
public class ProtocolUtil {
     
    /**
     * 编码报文的数据部分
     * @param encode
     * @param values
     * @return
     */
    public static ChannelBuffer encode(int encode,Map<String,String> values){
        ChannelBuffer totalBuffer=null;
        if (values!=null && values.size()>0) {
            totalBuffer=ChannelBuffers.dynamicBuffer();
            int length=0,index=0;
            ChannelBuffer [] channelBuffers=new ChannelBuffer[values.size()];
            ///Charset charset=XLCharSetFactory.getCharset(encode);
            Charset charset= Charset.forName("UTF-8");
            for(Entry<String,String> entry:values.entrySet()){
                String key=entry.getKey();
                String value=entry.getValue();
                ChannelBuffer buffer=ChannelBuffers.dynamicBuffer();
                buffer.writeInt(key.length());
                buffer.writeBytes(key.getBytes(charset));
                buffer.writeInt(value.length());
                buffer.writeBytes(value.getBytes(charset));
                channelBuffers[index++]=buffer;
                length+=buffer.readableBytes();
            }
             
            for (int i = 0; i < channelBuffers.length; i++) {
                totalBuffer.writeBytes(channelBuffers[i]);
            }
        }
        return totalBuffer;
    }
     
    /**
     * 解码报文的数据部分
     * @param encode
     * @param dataBuffer
     * @return
     */
    public static Map<String,String> decode(int encode,ChannelBuffer dataBuffer){
    	ConcurrentMap<String,String> dataMap=new ConcurrentHashMap<String, String>();
        if (dataBuffer!=null && dataBuffer.readableBytes()>0) {
            int processIndex=0,length=dataBuffer.readableBytes();
            //Charset charset=XLCharSetFactory.getCharset(encode);
            Charset charset= Charset.forName("UTF-8");
            while(processIndex<length){
                /**
                 * 获取Key
                 */
                int size=dataBuffer.readInt();
                byte [] contents=new byte [size];
                dataBuffer.readBytes(contents);
                String key=new String(contents, charset);
                processIndex=processIndex+size+4;
                /**
                 * 获取Value
                 */
                size=dataBuffer.readInt();
                contents=new byte [size];
                dataBuffer.readBytes(contents);
                String value=new String(contents, charset);
                dataMap.putIfAbsent(key, value);
                processIndex=processIndex+size+4;
            }
        }
        return dataMap;
    }
     
    /**
     * 获取客户端IP
     * @param channel
     * @return
     */
    public static String getClientIp(Channel channel){
        /**
         * 获取客户端IP
         */
        SocketAddress address = channel.getRemoteAddress();
        String ip = "";
        if (address != null) {
            ip = address.toString().trim();
            int index = ip.lastIndexOf(':');
            if (index < 1) {
                index = ip.length();
            }
            ip = ip.substring(1, index);
        }
        if (ip.length() > 15) {
            ip = ip.substring(Math.max(ip.indexOf("/") + 1, ip.length() - 15));
        }
        return ip;
    }
}