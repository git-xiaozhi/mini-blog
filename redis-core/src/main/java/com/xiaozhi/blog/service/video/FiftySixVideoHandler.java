package com.xiaozhi.blog.service.video;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com.xiaozhi.blog.utils.VideoUtil;
import com.xiaozhi.blog.vo.Video;

@Service("fiftySixVideoHandler")
public class FiftySixVideoHandler extends VideoHandler {

	private static Log logger = LogFactory.getLog(FiftySixVideoHandler.class);

	/**
	 * 获取56视频
	 * 
	 * @param url
	 *            视频URL
	 * @throws Exception
	 */
	@Override
	public Video getVideo(String url) {
		if (url.indexOf("56.com") != -1) {
			try {
				Document doc = VideoUtil.getURLContent(url);
				String content = doc.html();
				
				/**
				 * 获取视频标题
				 */
				String title = doc.title();

				/**
				 * 获取视频缩略图
				 */
				int begin = content.indexOf("\"img\":\"");
				content = content.substring(begin + 7, begin + 200);
				int end = content.indexOf("\"};");
				String pic = content.substring(0, end).trim();
				pic = pic.replaceAll("\\\\", "");

				/**
				 * 获取视频地址
				 */
				String flash = "http://player.56.com"
						+ url.substring(url.lastIndexOf("/"),
								url.lastIndexOf(".html")) + ".swf";

				Video video = new Video();
				video.setPic(pic);
				video.setFlash(flash);
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
