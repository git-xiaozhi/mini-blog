package solr.index;

import java.io.Serializable;

import org.apache.solr.client.solrj.beans.Field;

/**
 * @author xiaozhi
 * @version 2010-7-20 下午01:00:55
 */

public class UserIndexData implements Serializable{

    /**
	 *
	 */
	private static final long serialVersionUID = -1481198642195304042L;
	@Field
    private String id;
    @Field
    private String nickname;
    @Field
    private String school;
    @Field
    private String company;


    public String getId() {
        return id;
    }
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
    @Override
    public String toString() {
        return "UserIndexData [id=" + id + ", nickname=" + nickname
                + ", school=" + school + ", company=" + company + "]";
    }

}