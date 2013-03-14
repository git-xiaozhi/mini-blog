package mina.remote.server;


import mina.remote.Reflection;
import mina.remote.model.OperatorMessage;
import mina.remote.model.User;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@SuppressWarnings("unchecked")
public class ServerHandler extends IoHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger("/mina/remote/server/ServerHandler");

	@Override
	public void sessionOpened(IoSession session) {
		// 每60秒触发sessionIdle，IdleStatus.BOTH_IDLE 包含以下两种状态
		// IdleStatus.WRITER_IDLE 表示当前session 60秒内没有发送数据
		// IdleStatus.READER_IDLE 表示当前session 60秒内没有接收数据
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
	}

	@Override
	public void messageReceived(IoSession session, Object message) {
		
		LOGGER.info("Message is: " + message.toString());
		OperatorMessage om = (OperatorMessage) message;
		
		try {			
			User user = (User) session.getAttribute("user");//获取会话中对象
			
			/**如果是用户登录消息*/
			if(om.getLogin()){
				
			  // check if the user is already used
              if (ServerContant.USERS.contains(user)) {
                  session.write(false);//登录失败,用户已经存在
              }
              
              User loginUser = (User)excMethod(session, om);

              ServerContant.SESSIONS.add(session);
              session.setAttribute("user", loginUser);//设置当前session用户              
              // Allow all users
              ServerContant.USERS.add(loginUser);
              session.write(true);//登录成功
			}else{
			  /**只有登录授权用户才能调用远程方法*/
			  if(user!=null && ServerContant.USERS.contains(user))
			   session.write(excMethod(session, om));//执行用户方法
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {
		LOGGER.info("Disconnecting the idle.");
	}

	@Override
	public void messageSent(IoSession iosession, Object message)
			throws Exception {
		System.out.println("send messages :"+ message);
	}
	
    @Override
    public void sessionClosed(IoSession session) throws Exception {
		User user = (User) session.getAttribute("user");
		ServerContant.USERS.remove(user);
		ServerContant.SESSIONS.remove(session);
    }

    @Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		LOGGER.warn("Unexpected exception.", cause);
		session.close(true);
	}
    
	
	public Object excMethod(IoSession session,OperatorMessage om) throws Exception{
		Class cls=Class.forName(om.getClassName());
        Object test = ServerContant.CTX.getBean(cls);
        Object result = Reflection.invokeMethod(test,om.getMethod(),om.getParams());
        return result;
		
	}
	
}
