package com.xiaozhi.blog.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.xiaozhi.blog.formbean.UserForm;
import com.xiaozhi.blog.mongo.MongoUserDao;


@Service
public class UserFormValidator {
    private  Logger logger = LoggerFactory.getLogger(UserFormValidator.class);

    @Autowired
    private MongoUserDao mongoUserDao;

    public void validate(UserForm userForm, Errors errors) {

        if (mongoUserDao.isNameDuplicate(userForm.getName())) {
            errors.rejectValue("name", "Error.userForm.duplicatename");
         }

        if (mongoUserDao.isNickNameDuplicate(userForm.getNickname())) {
            errors.rejectValue("name", "Error.userForm.duplicatenickname");
         }

        if (!StringUtils.hasText(userForm.getPass()) || !StringUtils.hasText(userForm.getPass2()) ||
              !userForm.getPass().equals(userForm.getPass2())) {
            errors.rejectValue("pass", "Error.userForm.password");
        }

    }

}
