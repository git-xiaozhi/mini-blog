package solr.search;

import java.io.Serializable;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Transient;

import com.xiaozhi.blog.utils.FileUtil;
import com.xiaozhi.blog.vo.SinaAccessToken;
import com.xiaozhi.blog.vo.UserGroup;

/**
 *
 * 这里没有集成User是因为用dubbo治理后Hessian2序列话当父子属性名相同时会出现属性丢失问题。子类属性被父类属性覆盖
 * @author xiaozhi
 * @version 2010-7-20 下午01:00:55
 */

public class UserData implements Serializable{

    /**
	 *
	 */
	private static final long serialVersionUID = 5043173200600976967L;
	@Field
    public String id;
    @Field
    public String nickname;
    @Field
    public String school;
    @Field
    public String company;
    @Field
    public float score;


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

    public UserData(){}



    public UserData(String nickname, String pass, String school,
			String company) {
		super();
		this.nickname = nickname;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
		return "UserData [id=" + id + ", nickname=" + nickname + ", school="
				+ school + ", company=" + company + ", score=" + score
				+ ", isLink=" + isLink + ", followerNum=" + followerNum
				+ ", followingNum=" + followingNum + ", blogNum=" + blogNum
				+ ", portraitUrl=" + portraitUrl + ", bigPortraitUrl="
				+ bigPortraitUrl + ", roles=" + roles + ", accessToken="
				+ accessToken + ", groups=" + groups + "]";
	}


}