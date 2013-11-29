package com.clo.sdk.android.model;

import java.io.Serializable;
import java.util.List;

public class Page  implements Serializable{


	/**
	 *
	 */
	private static final long serialVersionUID = 3925407693832804615L;

	private String title;

	private String url;

	private String img;//选中图片

	private List<String> imgs;//页面所有图片列表

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public List<String> getImgs() {
		return imgs;
	}

	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}

	@Override
	public String toString() {
		return "Page [title=" + title + ", url=" + url + ", img=" + img
				+ ", imgs=" + imgs + "]";
	}


}
