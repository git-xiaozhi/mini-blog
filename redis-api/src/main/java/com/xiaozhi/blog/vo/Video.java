package com.xiaozhi.blog.vo;

import java.io.Serializable;

public class Video implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 5101770350225386350L;
	private String title;
	private String flash;//视频文件地址
	private String pic;//视频缩略图地址
	private String htmlpath;//视频播放页地址
	private String time;//播放时间



	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFlash() {
		return flash;
	}
	public void setFlash(String flash) {
		this.flash = flash;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getHtmlpath() {
		return htmlpath;
	}
	public void setHtmlpath(String htmlpath) {
		this.htmlpath = htmlpath;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Video [title=" + title + ", flash=" + flash + ", pic=" + pic
				+ ", htmlpath=" + htmlpath + ", time=" + time + "]";
	}



}
