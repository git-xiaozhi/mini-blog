package com.xiaozhi.blog.formbean;

import java.util.List;

public class UserGroupForm {

	private String memberId;
	private List<String> groupIds;
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public List<String> getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(List<String> groupIds) {
		this.groupIds = groupIds;
	}

	@Override
	public String toString() {
		return "UserGroupForm [memberId=" + memberId + ", groupIds=" + groupIds
				+ "]";
	}



}
