package com.xiaozhi.blog.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.code.ssm.api.InvalidateSingleCache;
import com.google.code.ssm.api.ParameterValueKeyProvider;
import com.google.code.ssm.api.ReadThroughSingleCache;
import com.xiaozhi.blog.formbean.UserForm;
import com.xiaozhi.blog.mongo.MongoUserDao;
import com.xiaozhi.blog.redis.UserIndexDao;
import com.xiaozhi.blog.utils.FileUtil;
import com.xiaozhi.blog.utils.Im4javaUitl;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import solr.index.UserIndexData;




@Service
public class UserService {

    private static Log logger = LogFactory.getLog(UserService.class);

    @Autowired
    private  MongoUserDao mongoUserDao;

    @Autowired
    private UserIndexDao userIndexDao;


    /**
     * 原始头像上传
     * @param a
     * @param filePath
     * @param uid
     * @param filename
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String uploadPortraitHandle(byte[] a,String filePath,String uid,String filename) throws IOException, InterruptedException{
        if(logger.isDebugEnabled()){

            logger.debug("------------------------->"+filePath+File.separator+uid);
        }
        File dir=new File(filePath+File.separator+uid);
        if(!dir.exists())dir.mkdir();
        long time = System.currentTimeMillis();

        String fileName = time+"_temp."+FileUtil.getExtension(filename);
        String path=filePath+File.separator+uid+File.separator+fileName;
        FileCopyUtils.copy(a, new File(path));

        return uid+"/"+fileName;
    }




    /**
     * 头像裁剪，并生成大小两个图片
     * @param a
     * @param filePath
     * @param uid
     * @param filename
     * @param width
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String ProHandle(String filePath,String uid,int width,int heigth,int x,int y) throws IOException, InterruptedException{
        if(logger.isDebugEnabled()){
            logger.debug("------------------------->"+filePath);
        }
        String dirPath = filePath.substring(0,filePath.lastIndexOf(File.separator));

        File dir=new File(dirPath);
        if(!dir.exists())dir.mkdir();

        byte[] a = FileCopyUtils.copyToByteArray(new File(filePath));

        /**头像裁剪没有选择域则不裁剪直接压缩*/
        if(width!=0 || heigth!=0)a = Im4javaUitl.cropImage(width, heigth,x,y,a);


        /**头像压缩成大小2张图片*/
        //生成大图片
        int bigWidth=width;
        if(width>150)bigWidth=150;
        String returnfileName = uid+"_large."+FileUtil.getExtension(filePath);
        String path=dirPath+File.separator+returnfileName;

        byte[] big = Im4javaUitl.resiizeImage(bigWidth, null, a);//按宽度等比压缩
        FileCopyUtils.copy(big, new File(path));

        //生成小图片
        int smallWidth=width;
        if(width>50)smallWidth=50;
        String smallfileName = uid+"."+FileUtil.getExtension(filePath);
        String smallpath=dirPath+File.separator+smallfileName;
        byte[] small = Im4javaUitl.resiizeImage(smallWidth, null, a);//按宽度等比压缩
        FileCopyUtils.copy(small, new File(smallpath));

        return smallfileName;
    }




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
