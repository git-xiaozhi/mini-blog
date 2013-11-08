package com.xiaozhi.blog.service;

import java.util.List;

import com.google.code.ssm.api.InvalidateSingleCache;
import com.google.code.ssm.api.ParameterValueKeyProvider;
import com.google.code.ssm.api.ReadThroughSingleCache;
import com.xiaozhi.blog.formbean.UserForm;
import com.xiaozhi.blog.mongo.MongoUserDao;
import com.xiaozhi.blog.redis.UserIndexDao;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import solr.index.UserIndexData;




@Service
public class UserService {

    private static Log logger = LogFactory.getLog(UserService.class);

    @Autowired
    private  MongoUserDao mongoUserDao;

    @Autowired
    private UserIndexDao userIndexDao;


    public boolean addUser(User user) {

        return this.mongoUserDao.addUser(user);

    }


    /**
     * 编辑用户信息
     * @param user
     * @param company
     * @param school
     * @return
     */
//    @InvalidateSingleCache(namespace = "_user_test_")
//    public boolean editUser(@ParameterValueKeyProvider final String uid,final UserForm userForm) {
//
//      return this.retwisRepository.editUser(uid, userForm);
//    }

    /**
     * 编辑用户信息
     * @param user
     * @param company
     * @param school
     * @return
     */
    public boolean editUser(final String uid,final UserForm userForm) {

      return this.mongoUserDao.editUser(uid, userForm);
    }



    /**
     * 保存weibo头像
     * @param url
     * @param uid
     * @return
     */
    public boolean editPortrait(String url,String uid){
        return this.mongoUserDao.editPortrait(url, uid);
    }


    /**
     * 获取用户
     * @param user
     * @return
     */
//    @ReadThroughSingleCache(namespace="_user_test_",expiration=3600)
//    public User  getUserById(@ParameterValueKeyProvider String uid) {
//    	if(logger.isDebugEnabled()){
//            logger.debug("==================================getUserById excute!!");
//        }
//        return this.retwisRepository.getUserById(uid);
//    }


    /**
     * 获取用户
     * @param user
     * @return
     */

    public User  getUserById(String uid) {
        return this.mongoUserDao.getUserById(uid);
    }

    /**
     * 获取系统所有用户列表
     * @return
     */
    public ListPage<UserIndexData> getGlobalUsers(Integer page,Integer pagesize){
        int firstResult = (page-1)*pagesize;
        int lastResult = firstResult+pagesize-1;
        int allResults = this.userIndexDao.getGlobalUsersNum();
        List<UserIndexData> blogs=this.userIndexDao.getGlobalUsers(new Range(firstResult,lastResult));
        return new ListPage<UserIndexData>(blogs, firstResult, lastResult, allResults);
    }


    /**
     * 用户名是否重复
     * @param name
     * @return
     */
    public boolean isNameDuplicate(String name){
        return this.mongoUserDao.isNameDuplicate(name);
    }

    /**
     * 用户昵称是否重复
     * @param nickname
     * @return
     */
    public boolean isNickNameDuplicate(String nickname){
        return this.mongoUserDao.isNickNameDuplicate(nickname);
    }

}
