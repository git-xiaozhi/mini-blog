package com.clo.sdk.android.model;

import java.util.Date;

public class UserVo {

	private Integer userid;
	private String username;
	private String pwd;
	private Date registertime;
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public Date getRegistertime() {
		return registertime;
	}
	public void setRegistertime(Date registertime) {
		this.registertime = registertime;
	}
	@Override
	public String toString() {
		return "UserVo [userid=" + userid + ", username=" + username + ", pwd="
				+ pwd + ", registertime=" + registertime + "]";
	}



}
