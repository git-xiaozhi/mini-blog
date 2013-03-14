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
package com.xiaozhi.blog.redis;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.stereotype.Service;

import org.springframework.data.redis.core.RedisCallback;

import com.tianji.test.core.redis.AuthUser;
import com.xiaozhi.blog.formbean.UserForm;
import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.utils.MD5;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.User;



/**
 * Twitter-clone on top of Redis.
 *
 * @author Costin Leau
 */
@Service
public class RetwisRepository {

    private static Log logger = LogFactory.getLog(RetwisRepository.class);

    private static final Pattern MENTION_REGEX = Pattern.compile("@([-_a-zA-Z0-9\u4e00-\u9fa5]{2,20}+)");

    @Autowired
    private  StringRedisTemplate template;

    //private  ValueOperations<String, String> valueOps;

    private  RedisAtomicLong userIdCounter;

    // global users
    //private RedisList<String> users;
    // global users
    //private RedisList<String> tjusers;//在天际网的用户
    // global timeline
    //private RedisList<String> timeline;

    //private final HashMapper<User, String, String> userMapper = new DecoratingStringHashMapper<User>(new JacksonHashMapper<User>(User.class));


    @PostConstruct
    public void init () {
        //valueOps = this.template.opsForValue();
        //users = new DefaultRedisList<String>(KeyUtils.users(), template);
        //tjusers = new DefaultRedisList<String>(KeyUtils.tjusers(), template);
        //timeline = new DefaultRedisList<String>(KeyUtils.timeline(), template);
        userIdCounter = new RedisAtomicLong(KeyUtils.globalUid(), template.getConnectionFactory());
    }

    /**
     * 用户名是否重复
     * @param name
     * @return
     */
    public boolean isNameDuplicate(String name){
        return this.template.hasKey(KeyUtils.user(name));
    }

    /**
     * 用户昵称是否重复
     * @param nickname
     * @return
     */
    public boolean isNickNameDuplicate(String nickname){
        return this.template.hasKey(KeyUtils.userNickname(nickname));
    }

    /**
     * 新注册用户
     * @param name
     * @param password
     * @param company
     * @param school
     * @return
     */
    public boolean addUser(final String name, final String nickname,String password,String company, String school) {
        final String uid = String.valueOf(userIdCounter.incrementAndGet());

        /**将用户信息作为hash存储*/
        final Map<byte[], byte[]> user= new HashMap<byte[], byte[]>();
        user.put("name".getBytes(), name.getBytes(Charset.forName("UTF-8")));
        user.put("nickname".getBytes(), nickname.getBytes(Charset.forName("UTF-8")));
        user.put("pass".getBytes(), MD5.calcMD5(password).getBytes());
        user.put("company".getBytes(), company.getBytes(Charset.forName("UTF-8")));
        user.put("school".getBytes(), school.getBytes(Charset.forName("UTF-8")));
        user.put("roles".getBytes(),"[\"ROLE_USER\"]".getBytes());/**给新注册用户增加用户权限*/

        boolean result = (Boolean) template.execute(new RedisCallback<Object>() {
             @Override
             public Object doInRedis(RedisConnection connection)throws DataAccessException {
                 try {
                     /**将用户信息作为hash存储*/
                     connection.multi();//事务开启
                     /**存储"user:" + name + ":uid"值*/
                     connection.set(KeyUtils.user(name).getBytes(Charset.forName("UTF-8")), uid.getBytes());
                     connection.set(KeyUtils.userNickname(nickname).getBytes(Charset.forName("UTF-8")), uid.getBytes());
                     /**将用户信息作为hash存储*/
                     connection.hMSet(KeyUtils.uid(uid).getBytes(), user);
                     /**将用户ID加入全部用户列表作为list数据存储*/
                     connection.lPush(KeyUtils.users().getBytes(), uid.getBytes());
                     connection.exec();//执行提交
                     return true;
                } catch (Exception e) {
                    logger.error("==================> editUser error :"+e.toString());
                }finally{
                    connection.close();
                }

                return false;
              }
            });
           return result;

    }


    /**
     * 编辑用户信息
     * @param user
     * @param company
     * @param school
     * @return
     */
    public boolean editUser(final String uid,final UserForm userForm) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("nickname", userForm.getNickname());
            map.put("company", new String(userForm.getCompany().getBytes(), "UTF-8"));
            map.put("school", new String(userForm.getSchool().getBytes(), "UTF-8"));
            template.opsForHash().putAll(KeyUtils.uid(uid),map);
            return true;
        } catch (Exception e) {
            logger.error("==================> editUser error :" + e.toString());
            return false;
        }

      }



    /**
     * 保存weibo头像
     * @param url
     * @param uid
     * @return
     */
    public boolean editPortrait(String url,String uid){
        try {
            template.opsForHash().put(KeyUtils.uid(uid), "portraitUrl", url);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("----------------------->error:"+e.toString());
        }
        return false;
    }


    /**
     * 获取用户
     * @param user
     * @return
     */
    public User  getUserById(final String uid) {

        User result = (User) template.execute(new RedisCallback<User>() {
            @Override
            public User doInRedis(RedisConnection connection)throws DataAccessException {
                try {
                    Map<byte[], byte[]> result = connection.hGetAll(KeyUtils.uid(uid).getBytes());
                    User user = new User();
                    user.setId(uid);
                    user.setName(new String(result.get("name".getBytes())));
                    user.setNickname(result.get("nickname".getBytes())==null?"":new String(result.get("nickname".getBytes())));
                    user.setCompany(new String(result.get("company".getBytes())));
                    user.setSchool(new String(result.get("school".getBytes())));
                    user.setFollowerNum(Integer.valueOf(result.get("followerNum".getBytes())==null?"0":new String(result.get("followerNum".getBytes()))));
                    user.setFollowingNum(Integer.valueOf(result.get("followingNum".getBytes())==null?"0":new String(result.get("followingNum".getBytes()))));
                    user.setBlogNum(Integer.valueOf(result.get("blogNum".getBytes())==null?"0":new String(result.get("blogNum".getBytes()))));
                    user.setPortraitUrl(result.get("portraitUrl".getBytes())==null?"":new String(result.get("portraitUrl".getBytes())));
                    return user;
               } catch (Exception e) {
                   logger.error("==================> getUserById error :"+e.toString());
               }finally{
                   connection.close();
               }
               return null;
             }
           });

        return result;
    }


    /**
     * 通过登录用户名拿到登录用户 权限列表
     * @param user
     * @return
     */
    public AuthUser getAuthUserByName(String name) {
        String uid = findUid(name);
        BoundHashOperations<String, String, String> userOps = template.boundHashOps(KeyUtils.uid(uid));
        String password = userOps.get("pass");
        String roles = userOps.get("roles");
        if(roles==null)return null;

        List<String> rolList = new ArrayList<String>();
        try {
            rolList = new ObjectMapper().readValue(roles, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new AuthUser(name,uid, password,rolList);
    }


    /**
     * 保存accessToken
     * @param user 用户名
     * @param token
     */
    public void saveAccessToken(String uid,String token,String sinaUid){
        template.opsForHash().put(KeyUtils.uid(uid), "token", token);
        template.opsForHash().put(KeyUtils.uid(uid), "sinaUid", sinaUid);
    }

    /**
     * 获取用户accesstoken
     * @param user
     * @return
     */
    public String getAccessTokenByUser(String uid){
        return (String) template.opsForHash().get(KeyUtils.uid(uid),"token");
    }

    /**
     * 获取第三方用户id
     * @param uid
     * @return
     */
    public String getUidByUser(String uid){
        return (String) template.opsForHash().get(KeyUtils.uid(uid),"sinaUid");
    }


    public Collection<String> newUsers(Range range) {
        RedisList<String> users = new DefaultRedisList<String>(KeyUtils.users(), template);
        return users.range(range.being, range.end);
    }

    public List<String> newTjUsers() {
        return template.opsForList().range(KeyUtils.tjusers(),0, 100);
    }


    public String findUid(String name) {
        return template.opsForValue().get(KeyUtils.user(name));
    }

    public String findUidByNickname(String nickname) {
        return template.opsForValue().get(KeyUtils.userNickname(nickname));
    }


    public static Collection<String> findMentions(String content) {
        Matcher regexMatcher = MENTION_REGEX.matcher(content);
        List<String> mentions = new ArrayList<String>();

        while (regexMatcher.find()) {
            String name = regexMatcher.group().substring(1);
            if(!mentions.contains(name))mentions.add(name);
        }

        return mentions;
    }

}