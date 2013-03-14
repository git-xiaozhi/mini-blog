package com.xiaozhi.blog.service.page;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.xiaozhi.blog.utils.VideoUtil;
import com.xiaozhi.blog.vo.Page;

public class HTMLParser {


	private static Log logger = LogFactory.getLog(HTMLParser.class);
	
	

	public static Page getHtmlInfo(String url) {

		try {
			if(!url.startsWith("http://"))url = "http://"+url;
			Document doc = VideoUtil.getURLContent(url);

			String title = doc.title();

			Elements elements = doc.getElementsByTag("img");
			if (elements != null && !elements.isEmpty()) {
				List<String> imgs = new ArrayList<String>(elements.size());

				for (int i = 0; i <= elements.size() - 1; i++) {
					url = elements.get(i).attr("src");
					if(!"".equals(url) && url.indexOf("http://")!=-1){
					  imgs.add(url);
					}
				}

				Page page = new Page();
				page.setTitle(title);
				page.setUrl(url);
				page.setImgs(imgs);

				return page;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	
	public static int getSize(String url){
		BufferedInputStream in;
		try {
			URL resUrl = new URL(url);
		    in = new BufferedInputStream(resUrl.openStream()); 
		     int t;
		     int i=0;
		     while ((t = in.read()) != -1) { 
		       i+=t;
		     }
		     logger.debug("------------------------------>"+i); 
		    in.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
		return 0;
	}
	

}
