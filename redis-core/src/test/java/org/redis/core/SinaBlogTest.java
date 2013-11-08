package org.redis.core;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import weibo4j.model.WeiboException;

import com.xiaozhi.blog.service.other.sina.SinaBlogService;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.vo.SinaPost;

public class SinaBlogTest extends ServiceTestBase{
	
	@Autowired
	private SinaBlogService sinaBlogService;
	

	@Test
	public void testSave(){
		//SinaPost post = new SinaPost(3, "1", "test3", 113);
		//SinaPost result = this.sinaBlogService.saveSinaPost(post);
		//logger.debug("----------------------->result :"+result.toString());
		
	}
	
	@Test
	public void testFindPage() throws WeiboException{
		
		ListPage<SinaPost> posts = this.sinaBlogService.getSinaBlogsByPage("1", 1, 2);
		List<SinaPost> list= posts.getList();
		for(SinaPost post :list){
			logger.debug("####################################post :"+post.toString());
		}
	}
	
	
	@Test
	public void testFindById(){
		
		SinaPost post = this.sinaBlogService.getSinaPostById(1);
	   logger.debug("####################################post :"+post.toString());

	}
	
	

}
