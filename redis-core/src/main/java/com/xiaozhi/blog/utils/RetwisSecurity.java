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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import com.tianji.test.core.redis.AuthUser;
import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.mongo.MongoUserDao;


/**
 * 注册完用此方法直接登录
 * @author xiaozhi
 *
 */

@Service
public class RetwisSecurity {

	private static Log logger = LogFactory.getLog(RetwisSecurity.class);

	@Autowired
	private  AuthenticationManager authenticationManager;

	@Autowired
	private MongoUserDao mongoUserDao;


	/**
	 * 注册完用此方法直接登录
	 * @param name
	 * @param pass
	 * @param request
	 */
	public void authenticateUserAndSetSession(String name, String pass,HttpServletRequest request) {
	    try {

		    AuthUser authUser = mongoUserDao.getAuthUserByName(name);
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(name, pass,authUser.getAuthorities());

			// generate session if one doesn't exist
			request.getSession();

			token.setDetails(new WebAuthenticationDetails(request));

			Authentication authenticatedUser = authenticationManager.authenticate(token);

			SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

			AuthUser u = (AuthUser) LoginHelper.getUserDetails();

	     } catch (BadCredentialsException e) {

		    logger.error("BadCredentialsException: " + e.getLocalizedMessage());
	     }
	}

}
