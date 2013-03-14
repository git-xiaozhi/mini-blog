package org.redis.core;



import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import callback.CallbackService;

import com.xiaozhi.blog.service.BlogService;
import com.xiaozhi.blog.service.UserService;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.utils.SolrPage;
import com.xiaozhi.blog.vo.User;

import solr.index.BlogIndexData;
import solr.index.UserIndexData;
import solr.search.BlogData;
import solr.search.EchoService;
import solr.search.SolrQueryService;
import solr.search.UserData;
import callback.CallbackListener;




//@Ignore
public class CallBackTest extends ServiceTestBase implements Serializable{


//    @Autowired
//    private CallbackService callbackService;

//
//    @Test
//    public void callback() throws IOException{
//
//    	callbackService.addListener(new CallbackListener() {
//            public void changed(String msg) {
//                System.out.println("===================>callback :" + msg);
//            }
//        });
//        System.in.read();
//    }

}
