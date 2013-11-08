package com.xiaozhi.blog.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="sinaPosts")
public class SinaPost implements Serializable{
	
	private static final long serialVersionUID = 8555722975081820815L;
	
	
	@Id
	private long id;
	private String uid;
	
	private String content;
	private String filePath;
	private String url;
	private Date futureDate;
	
	public SinaPost(){};
	

	public SinaPost(long id, String uid, String content,Date futureDate) {
		super();
		this.id = id;
		this.uid = uid;
		this.content = content;
		this.futureDate = futureDate;
	}


	public SinaPost(long id, String uid, String content, String filePath,
			String url,Date futureDate) {
		super();
		this.id = id;
		this.uid = uid;
		this.content = content;
		this.filePath = filePath;
		this.url = url;
		this.futureDate = futureDate;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public Date getFutureDate() {
		return futureDate;
	}

	public void setFutureDate(Date futureDate) {
		this.futureDate = futureDate;
	}


	@Override
	public String toString() {
		return "SinaPost [id=" + id + ", uid=" + uid + ", content=" + content
				+ ", filePath=" + filePath + ", url=" + url + ", futureDate="
				+ futureDate + "]";
	}
	
	

}
