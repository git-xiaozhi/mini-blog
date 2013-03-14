package com.xiaozhi.blog.service.video;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.xiaozhi.blog.utils.VideoUtil;
import com.xiaozhi.blog.vo.Video;

@Service("sixVideoHandler")
public class SixVideoHandler extends VideoHandler {

	private static Log logger = LogFactory.getLog(SixVideoHandler.class);

	/**
	 * 获取6间房视频
	 * 
	 * @param url
	 *            视频URL
	 */
	public Video getVideo(String url) {
		if (url.indexOf("6.cn") != -1) {
			try {
				Document doc = VideoUtil.getURLContent(url);
                
				/**
				 * 获取视频标题
				 */
				String title = doc.title();
				/**
				 * 获取视频缩略图
				 */
				Element picEt = doc.getElementsByClass("summary").first();
				String pic = picEt.getElementsByTag("img").first().attr("src");

				/**
				 * 获取视频时长
				 */
				String time = getVideoTime(doc, url, "watchUserVideo");
				if (time == null) {
					time = getVideoTime(doc, url, "watchRelVideo");
				}

				/**
				 * 获取视频地址
				 */
				Element flashEt = doc.getElementById("video-share-code");
				doc = Jsoup.parse(flashEt.attr("value"));
				String flash = doc.select("embed").attr("src");

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

	/**
	 * 获取6间房视频时长
	 */
	private static String getVideoTime(Document doc, String url, String id) {
		String time = null;

		Element timeEt = doc.getElementById(id);
		Elements links = timeEt.select("dt > a");

		for (Element link : links) {
			String linkHref = link.attr("href");
			if (linkHref.equalsIgnoreCase(url)) {
				time = link.parent().getElementsByTag("em").first().text();
				break;
			}
		}
		return time;
	}
}
