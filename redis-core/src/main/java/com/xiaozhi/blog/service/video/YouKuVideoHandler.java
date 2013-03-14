package com.xiaozhi.blog.service.video;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com.xiaozhi.blog.utils.VideoUtil;
import com.xiaozhi.blog.vo.Video;

@Service("youKuVideoHandler")
public class YouKuVideoHandler extends VideoHandler {

	private static Log logger = LogFactory.getLog(YouKuVideoHandler.class);

	/**
	 * 获取优酷视频
	 * 
	 * @param url
	 *            视频URL
	 */
	public Video getVideo(String url) {
		if (url.indexOf("v.youku.com") != -1) {
			try {
				Document doc = VideoUtil.getURLContent(url);
				
				/**
				 * 获取视频标题
				 */
				String title = doc.title();

				/**
				 * 获取视频缩略图
				 */
				String pic = VideoUtil
						.getElementAttrById(doc, "s_sina", "href");
				int local = pic.indexOf("pic=");
				pic = pic.substring(local + 4);

				/**
				 * 获取视频地址
				 */
				String flash = VideoUtil.getElementAttrById(doc, "link2",
						"value");

				/**
				 * 获取视频时间
				 */
				String time = VideoUtil.getElementAttrById(doc, "download",
						"href");
				if (time != null && !"".equals(time)) {
					String[] arrays = time.split("\\|");
					time = arrays[4];
				}

				Video video = new Video();
				video.setPic(pic);
				video.setFlash(flash);
				video.setTime("");
				video.setTitle(title);
				return video;
			} catch (Exception e) {
				logger.error("---------------->error is " + e.getMessage());
				e.printStackTrace();
			}
		}else if(this.successor!=null){
			return this.successor.getVideo(url);
		}
			return null;
	}

}
