package com.xiaozhi.blog.service.video;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com.xiaozhi.blog.utils.VideoUtil;
import com.xiaozhi.blog.vo.Video;

@Service("tudouVideoHandler")
public class TudouVideoHandler extends VideoHandler {
	
	private static Log logger = LogFactory.getLog(TudouVideoHandler.class);
	/**
	 * 获取土豆视频
	 * 
	 * @param url
	 *            视频URL
	 */
	public Video getVideo(String url) {
		if (url.indexOf("tudou.com") != -1) {
			try {
				Document doc = VideoUtil.getURLContent(url);
				String content = doc.html();
				
				/**
				 * 获取视频标题
				 */
				String title = doc.title();
				
				int beginLocal = content.indexOf("<script>document.domain");
				int endLocal = content.indexOf("</script>");
				content = content.substring(beginLocal, endLocal);

				/**
				 * 获取视频地址
				 */
				String flash = VideoUtil.getScriptVarByName("iid_code = icode", content);
				flash = "http://www.tudou.com/v/" + flash + "/v.swf";

				/**
				 * 获取视频缩略图
				 */
				String pic = VideoUtil.getScriptVarByName("thumbnail = pic", content);

				/**
				 * 获取视频时间
				 */
				String time = VideoUtil.getScriptVarByName("time", content);

				Video video = new Video();
				video.setPic(pic);
				video.setFlash(flash);
				video.setTime(time);
				video.setTitle(title);

				return video;
			} catch (Exception e) {
				logger.error("---------------->error is " + e.getMessage());
				e.printStackTrace();
			}
		}
		return this.successor.getVideo(url);
	}
}
