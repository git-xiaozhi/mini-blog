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
package com.xiaozhi.blog.web;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.formbean.UserForm;
import com.xiaozhi.blog.service.BlogService;
import com.xiaozhi.blog.service.FollowerService;
import com.xiaozhi.blog.service.FollowingService;
import com.xiaozhi.blog.service.UserService;
import com.xiaozhi.blog.utils.RetwisSecurity;
import com.xiaozhi.blog.utils.UserFormValidator;
import com.xiaozhi.blog.vo.Post;
import com.xiaozhi.blog.vo.User;
import com.xiaozhi.blog.vo.WebPost;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * Annotation-driven controller for Retwis.
 *
 * @author Costin Leau
 */
@Controller
public class RetwisController {

    private static Log logger = LogFactory.getLog(RetwisController.class);

    @Autowired
    private final UserService retwis;
    @Autowired
    private BlogService blogService;

    @Autowired
    public RetwisController(UserService twitter) {
        this.retwis = twitter;
    }

    @Autowired
    private FollowerService followerService;
    @Autowired
    private FollowingService followingService;

    @Autowired
    private RetwisSecurity retwisSecurity;

    @Value(value="#{globalProperties['blog.list.pagesize']}")
    private int pageSize;

    @Autowired
    private UserFormValidator userFormValidator;

    @Autowired
    private UserService userService;




    /**
     * 登录用户(自己)首页
     * @param page
     * @param model
     * @return
     */
    @RequestMapping("/main")
    public String root(@RequestParam(required = false,defaultValue="1") Integer page, HttpServletRequest request,Model model) {

        String targetUid = LoginHelper.getUserId();
        model.addAttribute("post", new Post());
        model.addAttribute("targetUid", targetUid);
        model.addAttribute("followers", followerService.getFollowers(targetUid));
        model.addAttribute("following", followingService.getFollowing(targetUid));
        model.addAttribute("user", retwis.getUserById(targetUid));

        if (!targetUid.equals(LoginHelper.getUserId())) {
          model.addAttribute("also_followed", followerService.alsoFollowed(LoginHelper.getUserId(), targetUid,1,6));
          model.addAttribute("common_followers", followerService.commonFollowers(LoginHelper.getUserId(), targetUid,1,6));
          model.addAttribute("follows", followingService.isFollowing(LoginHelper.getUserId(), targetUid));
        }

        model.addAttribute("posts", (LoginHelper.isUserSignedIn(targetUid) ? blogService.getTimelineByPage(targetUid, page,this.pageSize)
                : blogService.getPostsByPage(targetUid, page,this.pageSize)));
        model.addAttribute("sessionId", request.getSession().getId());
        return "home";

    }

    /**
     * 注册
     * @param userForm
     * @param vresult
     * @param model
     * @param request
     * @return
     */

    @RequestMapping("/signUp")
    public String signUp(@Valid UserForm userForm,  BindingResult vresult,Model model, HttpServletRequest request) {

        this.userFormValidator.validate(userForm, vresult);
        if(vresult.hasErrors()){
            List<FieldError> errors = vresult.getFieldErrors();
            for(FieldError error : errors){
                logger.debug("error--->"+error.getDefaultMessage()+"--"+error.getCode());
                logger.debug("error--->"+error.getCode());
                logger.debug("error--->"+error.getRejectedValue());
                logger.debug("error--->"+error.getField());
            }

            return "signIn";
        }


        boolean result = retwis.addUser(new User(userForm.getName(), userForm.getNickname(),userForm.getPass()
                ,userForm.getSchool(),userForm.getCompany()));

        /**注册成功后直接登录*/
        if(result)retwisSecurity.authenticateUserAndSetSession(userForm.getName(), userForm.getPass(), request);

        return "redirect:/u/" + LoginHelper.getUserId();
    }


    /**
     * 登录页面
     * @param name
     * @param pass
     * @param model
     * @param response
     * @return
     */
    @RequestMapping("/signIn")
    public String signIn(Model model){
        //int a = Integer.getInteger("xiaozhi");
        model.addAttribute("userForm", new UserForm());
        return "signin";
    }


    /**
     * 某用户首页
     * @param name
     * @param replyto
     * @param replypid
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "/u/{uid}", method = RequestMethod.GET)
    public String posts(@PathVariable String uid,
            @RequestParam(required = false,defaultValue="1") Integer page,
            HttpServletRequest request,
            Model model) {


        String targetUid = uid;
        model.addAttribute("post", new Post());
        model.addAttribute("targetUid", targetUid);
        model.addAttribute("followers", followerService.getFollowers(targetUid));
        model.addAttribute("following", followingService.getFollowing(targetUid));
        model.addAttribute("user", retwis.getUserById(targetUid));

        if (!targetUid.equals(LoginHelper.getUserId())) {
          model.addAttribute("also_followed", followerService.alsoFollowed(LoginHelper.getUserId(), targetUid,1,6));
          model.addAttribute("common_followers", followerService.commonFollowers(LoginHelper.getUserId(), targetUid,1,6));
          model.addAttribute("follows", followingService.isFollowing(LoginHelper.getUserId(), targetUid));
        }

        model.addAttribute("posts", (LoginHelper.isUserSignedIn(uid) ? blogService.getTimelineByPage(targetUid, page,this.pageSize)
                : blogService.getPostsByPage(targetUid, page,this.pageSize)));
        model.addAttribute("sessionId", request.getSession().getId());
        return "home";
    }



    /**
     * ajax 微薄分页
     * @param name
     * @param page
     * @param model
     * @return
     */
    @RequestMapping(value = "/u/{uid}/page", method = RequestMethod.GET)
    public String postsPage(@PathVariable String uid,@RequestParam(required = false,defaultValue="1") Integer page,Model model) {
        String targetUid = uid;
        model.addAttribute("posts", (LoginHelper.isUserSignedIn(uid) ? blogService.getTimelineByPage(targetUid, page,this.pageSize)
                : blogService.getPostsByPage(targetUid, page,this.pageSize)));
        return "/fragments/post";

    }

    /**
     * 提交微薄
     * @param name
     * @param post
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/u/{uid}/post", method = RequestMethod.POST)
    public String posts(WebPost post, Model model) {
        if(logger.isDebugEnabled())logger.debug("#########################"+post.toString());
        WebPost returnPost=blogService.post(LoginHelper.getUserId(), post,false);
        //if(post.getBindTianJi())restClient.postStatus(name, post.getContent());//同步天际status
        model.addAttribute("p",returnPost);
        return "/fragments/newpost";
    }

    /**
     * 关注他
     * @param name
     * @return
     */
    @RequestMapping("/u/{uid}/follow")
    public String follow(@PathVariable String uid) {
        followingService.follow(uid,LoginHelper.getUserId());
        return "redirect:/u/" + uid;
    }

    /**
     * 取消关注
     * @param name
     * @return
     */
    @RequestMapping("/u/{uid}/stopfollowing")
    public String stopFollowing(@PathVariable String uid) {
        followingService.stopFollowing(uid,LoginHelper.getUserId());
        return "redirect:/u/" + uid;
    }

    /**
     * 所有的微薄
     * @param page
     * @param model
     * @return
     */

    @RequestMapping("/timeline")
    public String timeline(@RequestParam(required = false) Integer page, Model model) {
        // sanitize page attribute
        //page = (page != null ? Math.abs(page) : 1);
       // model.addAttribute("page", page + 1);
        //Range range = new Range(page);
       // model.addAttribute("posts", blogService.timeline(range));
        //model.addAttribute("users", retwis.newUsers(new Range()));
    	if(logger.isDebugEnabled())logger.debug("#########################UserName :"+LoginHelper.getUserName());
        model.addAttribute("user", LoginHelper.getUserName());

        return "timeline";
    }


    /**
     * 用户名是否重复
     * @param name
     * @return
     */
    @RequestMapping("/isNameDuplicate")
    public @ResponseBody boolean isNameDuplicate(@RequestParam(required = true) String name) {

        return this.userService.isNameDuplicate(name);
    }

    /**
     * 用户昵称是否重复
     * @param nickname
     * @return
     */
    @RequestMapping("/isNickNameDuplicate")
    public @ResponseBody boolean isNickNameDuplicate(@RequestParam(required = true) String nickname) {

        return this.userService.isNickNameDuplicate(nickname);
    }


}