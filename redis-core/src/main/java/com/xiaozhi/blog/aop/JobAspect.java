package com.xiaozhi.blog.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import solr.index.BlogIndexData;
import solr.index.SolrjIndexCreater;
import solr.index.UserIndexData;

import com.xiaozhi.blog.rabbitmq.gateway.RabbitBlogDataMessageGateway;
import com.xiaozhi.blog.rabbitmq.gateway.RabbitUserDataMessageGateway;
import com.xiaozhi.blog.vo.User;
import com.xiaozhi.blog.vo.WebPost;


@Aspect
@Service("jobAspect")
public class JobAspect {

	private static Log logger = LogFactory.getLog(JobAspect.class);
	private static boolean isDebug = logger.isDebugEnabled();


	@Autowired
	private RabbitUserDataMessageGateway rabbitUserDataMessageGateway;

	@Autowired
	private RabbitBlogDataMessageGateway rabbitBlogDataMessageGateway;

	@Qualifier("blogIndexCreater")
    @Autowired
    private SolrjIndexCreater<BlogIndexData> blogIndexCreater;

	@Qualifier("userIndexCreater")
    @Autowired
    private SolrjIndexCreater<UserIndexData> userIndexCreater;


	/**
	 * 新增用户时通过MQ创建用户索引
	 * @param user
	 * @throws Exception
	 */
	@After("com.xiaozhi.blog.aop.SystemArchitecture.userAddAopFuction()&&args(user)")
	protected void creatUserIndex(User user) throws Exception{
		if(isDebug){
			logger.debug("#########后置置通知开始！！");
			logger.debug("----------------->user :"+user.toString());
		}
		UserIndexData data = new UserIndexData();
		BeanUtils.copyProperties(data, user);
		//rabbitUserDataMessageGateway.sendMessage(data);//rabbitmq实现
		this.userIndexCreater.addOrUpdateBean(data);//dubbo远程异步调用实现
	}



	/**
	 * 发布微博时通过MQ创建微博索引
	 * @param uid
	 * @param post
	 * @param isForword
	 * @param webPost
	 * @throws Exception
	 */
	@AfterReturning(pointcut="com.xiaozhi.blog.aop.SystemArchitecture.postBlogFuction()&&args(uid,post,isForword)",returning="webPost")
	protected void createBlogIndex(String uid, WebPost post ,boolean isForword,WebPost webPost) throws Exception{
		if(isDebug){
			logger.debug("#####################-gePersonById()方法被AfterReturning拦截了 ...");
			logger.debug("#########uid :"+uid);
			logger.debug("#########webPost :"+webPost.toString());
		}
		BlogIndexData data = new BlogIndexData();
		data.setContent(webPost.getContent());
		data.setForwardcontent(webPost.getWebPost()==null?null:webPost.getWebPost().getContent());
		data.setPid(webPost.getPid().toString());

		//this.rabbitBlogDataMessageGateway.sendMessage(data);//rabbitmq实现
		this.blogIndexCreater.addOrUpdateBean(data);//dubbo远程异步调用实现
	}


}

