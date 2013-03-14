package com.xiaozhi.blog.vo;


import com.xiaozhi.blog.utils.WebUtils;


public class CommentShow {

    private Integer commentId;
    private String time;
    private String timeArg;
    private String content;
    private boolean canDelete;
    private User user;
    private WebPost webPost;
    private String uid;
    private String targetBlogId;//评论对应微薄id



    public void initTimeShow(){
        String tempTime = WebUtils.timeInWords(Long.valueOf(time));
        int lastIndexOf = tempTime.lastIndexOf("#");
        if (lastIndexOf > 0) {
            this.time = tempTime.substring(0, lastIndexOf);
            this.timeArg = tempTime.substring(lastIndexOf + 1);
        }
        else {
            this.time = tempTime;
            this.timeArg = "";
        }

    }


    public Integer getCommentId() {
        return commentId;
    }
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }
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
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public boolean getCanDelete() {
        return canDelete;
    }
    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }


    public WebPost getWebPost() {
        return webPost;
    }


    public void setWebPost(WebPost webPost) {
        this.webPost = webPost;
    }


    public String getUid() {
		return uid;
	}


	public void setUid(String uid) {
		this.uid = uid;
	}


	public String getTargetBlogId() {
		return targetBlogId;
	}


	public void setTargetBlogId(String targetBlogId) {
		this.targetBlogId = targetBlogId;
	}


	@Override
	public String toString() {
		return "CommentShow [commentId=" + commentId + ", time=" + time
				+ ", timeArg=" + timeArg + ", content=" + content
				+ ", canDelete=" + canDelete + ", user=" + user + ", webPost="
				+ webPost + ", uid=" + uid + ", targetBlogId=" + targetBlogId
				+ "]";
	}






}
