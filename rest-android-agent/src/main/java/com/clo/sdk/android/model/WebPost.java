/*
 * Copyright 2011 the original author or authors.
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
package com.clo.sdk.android.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.clo.sdk.android.util.FileUtil;
import com.clo.sdk.android.util.WebUtils;


/**
 * DTO suitable for presenting a Post through the Web UI.
 *
 * @author Costin Leau
 */
public class WebPost implements Serializable{

    private static final long serialVersionUID = -8001476266951721385L;

    private String content;
    private User user;
    private Integer pid;
    private String transmitid;//转发微薄id,不是转发则为空
    private String time;
    private String timeArg;

    private WebPost webPost;//转发微薄



    private boolean bindTianJi;//是否同步到天际网
    private Integer commentNum;//评论数
    private Integer transmitNum=0;//转发数


    private String pic;//图片地址
    private String bigpic;//大图片地址

    private Video video;//视频对象
    private Page page;//网页对象
    private List<Video> webPosts;


    public WebPost() {
    }

    public WebPost(Post post) {

        String tempTime = WebUtils.timeInWords(Long.valueOf(post.getTime()));

        int lastIndexOf = tempTime.lastIndexOf("#");
        if (lastIndexOf > 0) {
            this.time = tempTime.substring(0, lastIndexOf);
            this.timeArg = tempTime.substring(lastIndexOf + 1);
        }
        else {
            this.time = tempTime;
            this.timeArg = "";
        }
        this.content = post.getContent();
        this.commentNum = post.getCommentNum();
        this.transmitNum = post.getTransmitNum();
        this.pid = Integer.valueOf(post.getPid());
        this.transmitid = post.getTransmitid();
        this.pic = post.getPic();
        this.video = post.getVideo();
        this.page = post.getPage();
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    /**
     * Returns the content.
     *
     * @return Returns the content
     */
    public String getContent() {
    	if(content.length()>15)return content.substring(0,15)+"...";
        return content;
    }

    /**
     * @param content The content to set.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Returns the pid.
     *
     * @return Returns the pid
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * @param pid The pid to set.
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getTransmitid() {
        return transmitid;
    }

    public void setTransmitid(String transmitid) {
        this.transmitid = transmitid;
    }

    /**
     * Returns the time.
     *
     * @return Returns the time
     */
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeArg() {
        return timeArg;
    }

    public void setTimeArg(String timeArg) {
        this.timeArg = timeArg;
    }

    public boolean getBindTianJi() {
        return bindTianJi;
    }

    public void setBindTianJi(boolean bindTianJi) {
        this.bindTianJi = bindTianJi;
    }


    public Integer getCommentNum() {
        return commentNum==null?0:commentNum;
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


    public WebPost getWebPost() {
        return webPost;
    }

    public void setWebPost(WebPost webPost) {
        this.webPost = webPost;
    }



    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getBigpic() {
        return FileUtil.trimExtension(this.pic)+"_large."+FileUtil.getExtension(this.pic);
    }

    public void setBigpic(String bigpic) {
        this.bigpic = bigpic;
    }

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


    public List<Video> getWebPosts() {
        List<Video> webPosts = new ArrayList<Video>();
        webPosts.add(new Video());
        webPosts.add(new Video());
        webPosts.add(new Video());
        return webPosts;
    }

    public void setWebPosts(List<Video> webPosts) {
        this.webPosts = webPosts;
    }

    public Post asPost() {
        Post post = new Post();
        post.setContent(content);
        post.setCommentNum(0);
        post.setTransmitNum(0);
        post.setTransmitid(transmitid);
        post.setPic(pic);
        post.setVideo(video);
        post.setPage(page);
        return post;
    }


    @Override
    public String toString() {
        return "WebPost [content=" + content + ", user=" + user + ", pid="
                + pid + ", transmitid=" + transmitid + ", time=" + time
                + ", timeArg=" + timeArg + ", webPost=" + webPost
                + ", bindTianJi=" + bindTianJi + ", commentNum=" + commentNum
                + ", transmitNum=" + transmitNum + ", pic=" + pic + ", bigpic="
                + bigpic + ", video=" + video + ", page=" + page
                + ", webPosts=" + webPosts + "]";
    }





}