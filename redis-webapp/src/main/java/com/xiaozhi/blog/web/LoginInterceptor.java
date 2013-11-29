package com.xiaozhi.blog.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.tianji.test.core.redis.LoginHelper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;



public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static Log logger = LogFactory.getLog(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

        if(modelAndView!=null){
//		 modelAndView.getModel().put("loggedIn", LoginHelper.getUserId()==null?false:true);
//		 modelAndView.getModel().put("loggedUser", LoginHelper.getUserId());
//		 modelAndView.getModel().put("isBindTianJi", LoginHelper.isBindTianJi());
         //logger.debug("################## loggedUser :"+  LoginHelper.getUserAuthUser());
         //modelAndView.getModel().put("loggedUser", LoginHelper.getUserAuthUser());
        }

        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub
        super.afterCompletion(request, response, handler, ex);
    }

}
