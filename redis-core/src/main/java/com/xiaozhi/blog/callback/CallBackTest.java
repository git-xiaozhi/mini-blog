package com.xiaozhi.blog.callback;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import callback.CallbackListener;
import callback.CallbackService;


@Service
public class CallBackTest{

	private static Log logger = LogFactory.getLog(CallBackTest.class);

//    @Autowired
//    private CallbackService callbackService;
//
//
//    @PostConstruct
//    public void callback() throws IOException{
//    	callbackService.addListener("test.key",new CallbackListener() {
//            public void changed(String msg) {
//                logger.debug("===================>callback :" + msg);
//            }
//        });
//    }
}