package org.redis.core;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import weibo4j.model.WeiboException;

import com.xiaozhi.blog.service.BlogService;
import com.xiaozhi.blog.service.other.sina.BlogTime;
import com.xiaozhi.blog.service.other.sina.Faces;
import com.xiaozhi.blog.vo.WebPost;

public class BlogTest extends ServiceTestBase {

	private static Log logger = LogFactory.getLog(BlogTest.class);

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogTime blogTime;


	@Test
	public void testBlogAddAop(){

		WebPost post = new WebPost();
		post.setContent("This is test Content");
		WebPost result = this.blogService.post("1", post, true);
		logger.debug("---------------------------->"+result.toString());
	}

	@Test
	public void testFaces() throws WeiboException{

		List<Faces> faces = blogTime.getRemoteExp("2.00H7wFHC06u731a00fbe55f40tyzGV");
		for(Faces f:faces){
			logger.debug("---------------------------->"+f.getName());
		}
	}


}
