package solr.index;

import java.io.Serializable;

import org.apache.solr.client.solrj.beans.Field;



public class BlogIndexData implements Serializable{
    /**
	 *
	 */
	private static final long serialVersionUID = -4839818979896662832L;
	@Field
    private String pid;//微博ID
    @Field
    private String content;//搏客内容
    @Field
    private String forwardcontent;//转发微博内容

    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getForwardcontent() {
        return forwardcontent;
    }
    public void setForwardcontent(String forwardcontent) {
        this.forwardcontent = forwardcontent;
    }


    @Override
    public String toString() {
        return "BlogIndexData [pid=" + pid + ", content=" + content
                + ", forwardcontent=" + forwardcontent + "]";
    }


}
