package com.xiaozhi.blog.vo;

import java.io.Serializable;


public class SinaAccessToken implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -7458003347913330583L;
	private String accesstoken;
	private String userid;


	public SinaAccessToken(String accesstoken, String userid) {
		super();
		this.accesstoken = accesstoken;
		this.userid = userid;
	}
	public String getAccesstoken() {
		return accesstoken;
	}
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	@Override
	public String toString() {
		return "SinaAccessToken [accesstoken=" + accesstoken + ", userid="
				+ userid + "]";
	}




}
