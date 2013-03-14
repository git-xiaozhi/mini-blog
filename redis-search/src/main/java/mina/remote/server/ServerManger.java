package mina.remote.server;

import mina.remote.model.BrodcastMessage;

import org.apache.mina.core.session.IoSession;

public class ServerManger {
	
	//广播方法
    public static void broadcast(BrodcastMessage message) {
        synchronized (ServerContant.SESSIONS) {
        	System.out.println("sessions size : "+ServerContant.SESSIONS.size());
            for (IoSession session : ServerContant.SESSIONS) {
                if (session.isConnected()) {
                    session.write(message);
                }
            }
        }
    }
	
}
