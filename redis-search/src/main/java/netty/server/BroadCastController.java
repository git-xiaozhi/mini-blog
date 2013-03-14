package netty.server;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class BroadCastController{

    private static Log logger = LogFactory.getLog(BroadCastController.class);
    
    @Autowired
    private Test bTest;

    /**
     * 测试广播消息
     * @param message
     * @return
     */
    @RequestMapping(value = "/broadcast", method = RequestMethod.GET)
    public String broadcast(@RequestParam(value="message",required=true) String message) {

    	try {
    		for (;;) {
    			Thread.sleep(3000);
    			bTest.broadcastMessage(message);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
}
