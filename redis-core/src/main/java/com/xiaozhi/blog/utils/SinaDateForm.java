package com.xiaozhi.blog.utils;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xiaozhi.blog.utils.DateUtil;



public class SinaDateForm {

	private static Log logger = LogFactory.getLog(SinaDateForm.class);
	
	private String content;
	private String pic;
	private String date;
	private String hour;
	private String minute;
	private Date futureDate;
	
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getMinute() {
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}
	
	public Date getFutureDate() {
		return DateUtil.parseTime(date+" "+hour+":"+minute);
	}
	public void setFutureDate(Date futureDate) {
		this.futureDate = futureDate;
	}
	@Override
	public String toString() {
		return "SinaDateForm [content=" + content + ", pic=" + pic + ", date="
				+ date + ", hour=" + hour + ", minute=" + minute
				+ ", futureDate=" + futureDate + "]";
	}
	


	
	

}
