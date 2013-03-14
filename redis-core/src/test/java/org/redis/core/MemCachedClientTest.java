package org.redis.core;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiaozhi.blog.formbean.UserForm;
import com.xiaozhi.blog.service.UserService;
import com.xiaozhi.blog.vo.User;






/**
 * HTML文本解析
 * @author xiaozhi
 *
 */
@Ignore
public class MemCachedClientTest extends ServiceTestBase{

    private static Log logger = LogFactory.getLog(MemCachedClientTest.class);

    @Autowired
    private UserService userService;

    @Test
    public void testGetUserServiceCache(){

    	User  user = userService.getUserById("1");
        logger.debug("---------------------> user :"+user);
    }

    @Test
    public void testEditUserServiceCache(){

    	UserForm userForm = new UserForm();
    	userForm.setNickname("肖治".intern());
    	userForm.setCompany("太极");
    	userForm.setSchool("北京医科大学");
    	boolean  f = userService.editUser("1", userForm);
        logger.debug("---------------------> f :"+f);
    }



}
