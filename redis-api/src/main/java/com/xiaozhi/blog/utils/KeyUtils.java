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
package com.xiaozhi.blog.utils;

/**
 * Simple class keeping all the key patterns to avoid the proliferation of
 * Strings through the code.
 *
 * @author Costin Leau
 */
public abstract class KeyUtils {
    static final String UID = "uid:";

    public static String following(String uid) {
        return UID + uid + ":following";
    }

    public static String followers(String uid) {
        return UID + uid + ":followers";
    }

    public static String timeline(String uid) {
        return UID + uid + ":timeline";
    }

    public static String collect(String uid) {
        return UID + uid + ":collect";
    }

    //提到我的blog
    public static String mentions(String uid) {
        return UID + uid + ":mentions";
    }

    //提到我的评论
    public static String commentmentions(String uid) {
        return UID + uid + ":commentmentions";
    }

    public static String posts(String uid) {
        return UID + uid + ":posts";
    }

    public static String auth(String uid) {
        return UID + uid + ":auth";
    }

    public static String uid(String uid) {
        return UID + uid;
    }


    public static String post(String pid) {
        return "pid:" + pid;
    }

    /**
     * 微薄对应评论
     * @param pid
     * @return
     */
    public static String commentByBlog(String pid) {
        return "pid:" + pid+":comment";
    }

    /**
     * 评论key
     * @param commentid
     * @return
     */
    public static String commentKey(String commentid) {
        return "commentid:" + commentid;
    }

    /**
     * 收到评论列表
     * @param uid
     * @return
     */
    public static String receiveComments(String uid) {
        return UID + uid + ":receiveComments";
    }

    /**
     * 发出评论列表
     * @param uid
     * @return
     */
    public static String postComments(String uid) {
        return UID + uid + ":postComments";
    }



    public static String authKey(String auth) {
        return "auth:" + auth;
    }

    public static String user(String name) {
        return "user:" + name + ":uid";
    }

    public static String userNickname(String nickname) {
        return "userNickname:" + nickname + ":uid";
    }

    public static String users() {
        return "users";
    }
    public static String tjusers() {
        return "tjusers";
    }

    public static String bookMark(String name) {
        return "bookmark:"+name;
    }

    public static String jobbookMark(String name) {
        return "jobbookmark:"+name;
    }

    public static String timeline() {
        return "timeline";
    }

    public static String globalUid() {
        return "global:uid";
    }

    public static String globalPid() {
        return "global:pid";
    }

    public static String alsoFollowed(String uid, String targetUid) {
        return UID + uid + ":also:uid:" + targetUid;
    }

    public static String commonFollowers(String uid, String targetUid) {
        return UID + uid + ":common:uid:" + targetUid;
    }
}