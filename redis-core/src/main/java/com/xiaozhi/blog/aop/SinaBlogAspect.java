package com.xiaozhi.blog.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.xiaozhi.blog.quartz.SinaBlogSchedulerService;
import com.xiaozhi.blog.vo.SinaPost;


@Aspect
@Service("sinaBlogAspect")
public class SinaBlogAspect {

	private static Log logger = LogFactory.getLog(SinaBlogAspect.class);
	private static boolean isDebug = logger.isDebugEnabled();
	
	@Autowired
	private SinaBlogSchedulerService sinaBlogSchedulerService;
	
	
	/**
	 * 定时微博保存
	 * @param post
	 * @param returnPost
	 * @throws Exception
	 */
	@AfterReturning(pointcut="com.xiaozhi.blog.aop.SystemArchitecture.saveSinaPostFuction()&&args(post)",returning="returnPost")
	protected void saveSinaPost(SinaPost post,SinaPost returnPost) throws Exception{
		if(isDebug){
			logger.debug("#########saveSinaPost开始！！");
		}
        //设定定时任务
		if(returnPost!=null){
			sinaBlogSchedulerService.addSinaBlogScheduler(returnPost);
		}
	}
	
	/**
	 * 删除定时微博
	 * @param id
	 * @param result
	 * @throws Exception
	 */
	@AfterReturning(pointcut="com.xiaozhi.blog.aop.SystemArchitecture.deleteSinapostFuction()&&args(id)",returning="returnPost")
	protected void deleteSinaPost(long id,SinaPost returnPost) throws Exception{
		
		if(isDebug){
			logger.debug("#########deleteSinaPost开始！！");
		}
		//有图片删除图片，删除对应的定时job
		if(returnPost!=null){
			sinaBlogSchedulerService.removeSinaBlogScheduler(returnPost);
		}
	}
	
}
