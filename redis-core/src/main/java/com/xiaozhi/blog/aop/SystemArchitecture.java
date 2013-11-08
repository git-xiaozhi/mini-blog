package com.xiaozhi.blog.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;



/**
 * @since 1.0
 * @author xiaozhi
 *
 */
@Aspect
public class SystemArchitecture {

	@Pointcut("execution(* com.xiaozhi.blog.service.UserService.addUser(..))")
	public void userAddAopFuction() {
	}

	@Pointcut("execution(* com.xiaozhi.blog.service.BlogService.post(..))")
	public void postBlogFuction() {
	}
	
	
	
	
	@Pointcut("execution(* com.xiaozhi.blog.service.other.sina.SinaBlogService.saveSinaPost(..))")
	public void saveSinaPostFuction() {
	}
	
	@Pointcut("execution(* com.xiaozhi.blog.service.other.sina.SinaBlogService.deleteSinapost(..))")
	public void deleteSinapostFuction() {
	}

}
