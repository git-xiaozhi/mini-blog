package com.xiaozhi.blog.quartz;


import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.xiaozhi.blog.mongo.MongoUserDao;
import com.xiaozhi.blog.vo.SinaAccessToken;
import com.xiaozhi.blog.vo.SinaPost;


@Service
public class SinaBlogSchedulerService {
	
	
	private static Log logger = LogFactory.getLog(SinaBlogSchedulerService.class);
	
	@Autowired
	private SchedulerFactoryBean quartzScheduler ;
	@Autowired
	private  MongoUserDao mongoUserDao;
	
	
	/**
	 * 增加定时任务
	 * @param post
	 */
	public void addSinaBlogScheduler(SinaPost post){
		try {
		Scheduler sched = quartzScheduler.getScheduler();
		JobDetail job = JobBuilder.newJob(MySinaBlogJob.class).withIdentity(post.getId()+"_job", post.getUid()).requestRecovery().build();
		
		job.getJobDataMap().put("post", post);//传递参数到job中
		SinaAccessToken accessToken = mongoUserDao.getAccessTokenByUser(post.getUid());
		job.getJobDataMap().put("accessToken", accessToken);
		
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(TriggerKey.triggerKey(post.getId()+"_trigger", post.getUid()))
				.startAt(post.getFutureDate())
				.build();
	    
			sched.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			logger.error("----------------------->addSinaBlogScheduler error :", e.fillInStackTrace());
		}	
	}
	
	
	/**
	 * 删除定时任务
	 * @param post
	 */
	public void removeSinaBlogScheduler(SinaPost post){	
		try {
			Scheduler sched = quartzScheduler.getScheduler();
			sched.deleteJob(JobKey.jobKey(post.getId()+"_job", post.getUid()));
			logger.debug("----------------------->filepath :"+post.getFilePath());
			//删除对应的图片
			if(post.getFilePath()!=null){
			 File file = new File(post.getFilePath());
			 if(file!=null && file.isFile())file.delete();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("----------------------->removeSinaBlogScheduler error :", e.fillInStackTrace());
		}
	}
	
}
