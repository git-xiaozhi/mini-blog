package com.xiaozhi.blog.service.other.sina;

public class Icon{

	private String url;

	public Icon(String url, String title) {
		super();
		this.url = url;
		this.title = title;
	}

	private String title;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Icon [url=" + url + ", title=" + title + "]";
	}

}
