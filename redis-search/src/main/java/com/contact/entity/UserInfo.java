package com.contact.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {

	private static final long serialVersionUID = -8129180180449258925L;
	
	public int id;
	public int imageid;
	public String username;
	public String userPhone;
	public int getId() {
		return id;
	}
	
	
	
	public void setId(int id) {
		this.id = id;
	}
	public int getImageId() {
		return imageid;
	}
	public void setImageId(int imageId) {
		this.imageid = imageId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}



	@Override
	public String toString() {
		return "User [id=" + id + ", imageId=" + imageid + ", username="
				+ username + ", userPhone=" + userPhone + "]";
	}

	
}
