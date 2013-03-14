package solr.search;

import java.io.Serializable;

import org.apache.solr.client.solrj.beans.Field;

import com.xiaozhi.blog.vo.WebPost;

public class BlogData  extends WebPost  implements Serializable{

    private static final long serialVersionUID = 5808395150557250417L;

    @Field(value="pid")
    public String blogid;//微博ID

    @Field(value="content")
    public String blogcontent;//搏客内容
    @Field
    public String forwardcontent;//转发微博内容
    @Field
    public float score;

    public String getBlogid() {
		return blogid;
	}
	public void setBlogid(String blogid) {
		this.blogid = blogid;
	}

    public String getBlogcontent() {
		return blogcontent;
	}
	public void setBlogcontent(String blogcontent) {
		this.blogcontent = blogcontent;
	}
	public String getForwardcontent() {
        return forwardcontent;
    }
    public void setForwardcontent(String forwardcontent) {
        if(this.getWebPost()!=null)this.getWebPost().setContent(forwardcontent);
        this.forwardcontent = forwardcontent;
    }
    public float getScore() {
        return score;
    }
    public void setScore(float score) {
        this.score = score;
    }

    @Override
	public String toString() {
		return "BlogData [blogid=" + blogid + ", blogcontent=" + blogcontent
				+ ", forwardcontent=" + forwardcontent + ", score=" + score
				+ "]";
	}


}
