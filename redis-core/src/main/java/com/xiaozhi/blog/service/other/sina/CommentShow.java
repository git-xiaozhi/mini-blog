package com.xiaozhi.blog.service.other.sina;

import weibo4j.model.Comment;

public class CommentShow extends Comment {

        /**
         *
         */
        private static final long serialVersionUID = -4798078631289452348L;

        private boolean canDelete;

    public boolean getCanDelete() {
        return canDelete;
    }
    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }



}