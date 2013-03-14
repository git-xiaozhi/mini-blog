package com.xiaozhi.blog.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="comment")
public class Comment {

    private String uid;
    @Id
    private String commentId;
    private String targetBlogId;//评论对应微薄id
    private String targetCommentId;//回复对应评论id
    private String time = String.valueOf(System.currentTimeMillis());
    private String content;


    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCommentId() {
        return commentId;
    }
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }


    public String getTargetBlogId() {
        return targetBlogId;
    }
    public void setTargetBlogId(String targetBlogId) {
        this.targetBlogId = targetBlogId;
    }
    public String getTargetCommentId() {
        return targetCommentId;
    }
    public void setTargetCommentId(String targetCommentId) {
        this.targetCommentId = targetCommentId;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }



    @Override
    public String toString() {
        return "Comment [uid=" + uid + ", commentId=" + commentId
                + ", targetBlogId=" + targetBlogId + ", targetCommentId="
                + targetCommentId + ", time=" + time + ", content=" + content
                + "]";
    }

}
