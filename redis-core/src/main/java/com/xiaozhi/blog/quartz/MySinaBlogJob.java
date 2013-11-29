package com.xiaozhi.blog.quartz;



import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import weibo4j.Timeline;
import weibo4j.http.ImageItem;

import com.xiaozhi.blog.mongo.SinaBlogDao;
import com.xiaozhi.blog.utils.FileUtil;
import com.xiaozhi.blog.vo.SinaAccessToken;
import com.xiaozhi.blog.vo.SinaPost;


@PersistJobDataAfterExecution//保存JobDataMap传递的参数,有状态
@DisallowConcurrentExecution//不允许并发执行,这样可以防止在前一个任务和后一个时间任务并行执行
public class MySinaBlogJob implements Job {
	
	private static Log logger = LogFactory.getLog(MySinaBlogJob.class);
	
	@Override
	public void execute(JobExecutionContext cxt) throws JobExecutionException {
		
		SinaPost post = (SinaPost)cxt.getJobDetail().getJobDataMap().get("post");
		SinaAccessToken accessToken = (SinaAccessToken)cxt.getJobDetail().getJobDataMap().get("accessToken");//获取accessToken
        if(logger.isDebugEnabled())logger.debug("------------------------------->accessToken :"+accessToken.toString());
		try {
			Timeline timeline = (Timeline)cxt.getScheduler().getContext().get("timeline");
			
			timeline.client.setToken(accessToken.getAccesstoken());
			if(post.getFilePath()==null || "".equals(post.getFilePath())){
			  timeline.UpdateStatus(post.getContent());
			}else{
			  byte[] picbyte = FileUtil.readFileImage(post.getFilePath());
			  ImageItem imageItem = new ImageItem("pic", picbyte);
			  timeline.UploadStatus(java.net.URLEncoder.encode(post.getContent(), "utf-8"), imageItem);
		   }
			
			SinaBlogDao sinaBlogDao = (SinaBlogDao)cxt.getScheduler().getContext().get("sinaBlogDao");
			sinaBlogDao.removeSinaPostById(post.getId());
			//删除对应的图片
			if(post.getFilePath()!=null){
			 File file = new File(post.getFilePath());
			 if(file!=null && file.isFile())file.delete();
			}
			
		} catch (Exception e) {
			logger.error("----------------------->execute error :", e.fillInStackTrace());
		}
		
		
	}

}
