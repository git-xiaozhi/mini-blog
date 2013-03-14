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
package com.xiaozhi.blog.vo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.xiaozhi.blog.utils.FileUtil;


/**
 * Class describing a user.
 *
 * @author Costin Leau
 */
@Document(collection="user")
public class User implements Serializable{
	@Id
    private String id;
    private String name;
    private String nickname;
    private String pass;
    private String school;
    private String company;



    private boolean isLink;//是否相互关注
    private Integer followerNum=0;//粉丝数
    private Integer followingNum=0;//关注数
    private Integer blogNum=0;//微博数

    private String portraitUrl;//小头像url
    private String bigPortraitUrl;//大头像url

    private String roles;


    private SinaAccessToken accessToken;//第三方开放平台access_token

    @Transient
    private List<UserGroup> groups;

    public User(){}



    public User(String name, String nickname, String pass, String school,
			String company) {
		super();
		this.name = name;
		this.nickname = nickname;
		this.pass = pass;
		this.school = school;
		this.company = company;
	}




    /**
     * Returns the id.
     *
     * @return Returns the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the name.
     *
     * @return Returns the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Returns the pass.
     *
     * @return Returns the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass The pass to set.
     */
    public void setPass(String password) {
        this.pass = password;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean getLink() {
        return isLink;
    }

    public void setLink(boolean isLink) {
        this.isLink = isLink;
    }

    public Integer getFollowerNum() {
        return followerNum;
    }

    public void setFollowerNum(Integer followerNum) {
        this.followerNum = followerNum;
    }

    public Integer getFollowingNum() {
        return followingNum;
    }

    public void setFollowingNum(Integer followingNum) {
        this.followingNum = followingNum;
    }

    public Integer getBlogNum() {
        return blogNum;
    }

    public void setBlogNum(Integer blogNum) {
        this.blogNum = blogNum;
    }

    public String getPortraitUrl() {
        if(this.portraitUrl==null || "".equals(this.portraitUrl)){
            return "/static/images/noportrait_small.jpg";
        }
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public String getBigPortraitUrl() {
        if(this.portraitUrl==null || "".equals(this.portraitUrl)){
            return "/static/images/noportrait.jpg";
        }
        return FileUtil.trimExtension(this.portraitUrl)+"_large."+FileUtil.getExtension(this.portraitUrl);
    }

    public void setBigPortraitUrl(String bigPortraitUrl) {
        this.bigPortraitUrl = bigPortraitUrl;
    }

    public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}


	public SinaAccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(SinaAccessToken accessToken) {
		this.accessToken = accessToken;
	}

	public List<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<UserGroup> groups) {
		this.groups = groups;
	}



	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", nickname=" + nickname
				+ ", pass=" + pass + ", school=" + school + ", company="
				+ company + ", isLink=" + isLink + ", followerNum="
				+ followerNum + ", followingNum=" + followingNum + ", blogNum="
				+ blogNum + ", portraitUrl=" + portraitUrl
				+ ", bigPortraitUrl=" + bigPortraitUrl + ", roles=" + roles
				+ ", accessToken=" + accessToken + ", groups=" + groups + "]";
	}



}