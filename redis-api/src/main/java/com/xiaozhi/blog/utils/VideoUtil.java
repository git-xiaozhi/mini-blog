package com.xiaozhi.blog.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * 视频工具类
 * @author Administrator
 *
 */

public class VideoUtil {

	private static Log logger = LogFactory.getLog(VideoUtil.class);
	
	
	/**
	 * 获取script某个变量的值
	 * 
	 * @param name
	 *            变量名称
	 * @return 返回获取的值
	 */
	public static String getScriptVarByName(String name, String content) {
		String script = content;

		int begin = script.indexOf(name);

		script = script.substring(begin + name.length() + 2);

		int end = script.indexOf(",");

		script = script.substring(0, end);

		String result = script.replaceAll("'", "");
		result = result.trim();

		return result;
	}

	/**
	 * 根据HTML的ID键及属于名，获取属于值
	 * 
	 * @param id HTML的ID键
	 * @param attrName 属于名
	 * @return 返回属性值
	 */
	public static String getElementAttrById(Document doc, String id,String attrName) throws Exception {
		Element et = doc.getElementById(id);
		String attrValue = et.attr(attrName);

		return attrValue;
	}

	/**
	 * 获取网页的内容
	 */
	public static Document getURLContent(String url) throws Exception {
		Document doc = Jsoup.connect(url).userAgent("Mozilla").cookie("auth", "token").timeout(6000).get();
		return doc;
	}

}
