/*
 * Copyright 2011 the original uid or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xiaozhi.blog.vo;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Class describing a post.
 *
 * @author Costin Leau
 */

@Document(collection="blog")
public class Post  implements Serializable{

	@Id
	private String pid;

	private String content;
	private String uid;
	private String time = String.valueOf(System.currentTimeMillis());
	private String replyPid;
	private String replyUid;
	private String transmitid;


	private Integer commentNum=0;//评论数
	private Integer transmitNum=0;//转发数

	private String pic;//图片地址
	//private String videoJson;//视频对象json
	//private String pageJson;//网页对象json

    private Video video;//视频对象
    private Page page;//网页对象

	/**
	 * Returns the content.
	 *
	 * @return Returns the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}


	/**
	 * Returns the uid.
	 *
	 * @return Returns the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid The uid to set.
	 */
	public void setUid(String author) {
		this.uid = author;
	}

	/**
	 * Returns the time.
	 *
	 * @return Returns the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time The time to set.
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * Returns the replyPid.
	 *
	 * @return Returns the replyPid
	 */
	public String getReplyPid() {
		return replyPid;
	}

	/**
	 * @param replyPid The replyPid to set.
	 */
	public void setReplyPid(String replyPid) {
		this.replyPid = replyPid;
	}

	/**
	 * Returns the replyUid.
	 *
	 * @return Returns the replyUid
	 */
	public String getReplyUid() {
		return replyUid;
	}

	/**
	 * @param replyUid The replyUid to set.
	 */
	public void setReplyUid(String replyUid) {
		this.replyUid = replyUid;
	}


	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTransmitid() {
		return transmitid;
	}

	public void setTransmitid(String transmitid) {
		this.transmitid = transmitid;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Integer getTransmitNum() {
		return transmitNum;
	}

	public void setTransmitNum(Integer transmitNum) {
		this.transmitNum = transmitNum;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}


//	public String getVideoJson() {
//		return videoJson;
//	}
//
//	public void setVideoJson(String videoJson) {
//		this.videoJson = videoJson;
//	}
//
//
//	public String getPageJson() {
//		return pageJson;
//	}
//
//	public void setPageJson(String pageJson) {
//		this.pageJson = pageJson;
//	}


	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@Override
	public String toString() {
		return "Post [pid=" + pid + ", content=" + content + ", uid=" + uid
				+ ", time=" + time + ", replyPid=" + replyPid + ", replyUid="
				+ replyUid + ", transmitid=" + transmitid + ", commentNum="
				+ commentNum + ", transmitNum=" + transmitNum + ", pic=" + pic
				+ ", video=" + video + ", page=" + page + "]";
	}




}