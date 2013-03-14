package com.xiaozhi.blog.service.video;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.xiaozhi.blog.utils.VideoUtil;
import com.xiaozhi.blog.vo.Video;

@Service("ku6VideoHandler")
public class Ku6VideoHandler extends VideoHandler {

	private static Log logger = LogFactory.getLog(Ku6VideoHandler.class);

	/**
	 * 获取酷6视频
	 * 
	 * @param url
	 *            视频URL
	 */
	public Video getVideo(String url) {
		if (url.indexOf("v.ku6.com") != -1) {
			try {
				Document doc = VideoUtil.getURLContent(url);
				String content = doc.html();
				/**
				 * 获取视频标题
				 */
				String title = doc.title();
				
				/**
				 * 获取视频地址
				 */
				Elements flashEt = doc.getElementsByClass("text_A");

				String flash = null;
				if (flashEt != null) {
					flash = flashEt.attr("value");
				}

				/**
				 * 获取视频缩略图
				 */
				int beginLocal = content.indexOf("A.VideoInfo =");
				int endLocal = content.indexOf("\", data: {");
				content = content.substring(beginLocal, endLocal);
				String pic = content.substring(content.lastIndexOf("http://"),
						content.length());

				Video video = new Video();
				video.setPic(pic);
				video.setFlash(flash);
				video.setTitle(title);
				// video.setTime(time);

				return video;
			} catch (Exception e) {
				logger.error("---------------->error is " + e.getMessage());
				e.printStackTrace();
			}
		}
		return this.successor.getVideo(url);

	}
}
