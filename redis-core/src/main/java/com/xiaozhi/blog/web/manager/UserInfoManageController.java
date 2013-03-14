package com.xiaozhi.blog.web.manager;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.formbean.UserForm;
import com.xiaozhi.blog.service.UserService;
import com.xiaozhi.blog.utils.FileUtil;
import com.xiaozhi.blog.vo.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;



@Controller
@RequestMapping("/user/")
public class UserInfoManageController {

    private static Log logger = LogFactory.getLog(UserInfoManageController.class);


    @Autowired
    private UserService userService;

    /**
     * 编辑用户信息表单
     * @param model
     * @return
     */
    @RequestMapping("showedituser")
    public String showeditUser(Model model) {
        User user =userService.getUserById(LoginHelper.getUserId());
        if(logger.isDebugEnabled())logger.debug("#########################"+user.toString());
        model.addAttribute("user", user);
        return "/manage/showedituser";
    }

    /**
     * 用户信息提交
     * @param company
     * @param school
     * @param model
     * @return
     */
    @RequestMapping("edituser")
    public String editUser(UserForm userForm, Model model) {
        if(logger.isDebugEnabled())logger.debug("=====================>userForm :"+userForm.toString());

        String uid =LoginHelper.getUserId();
        userService.editUser(uid, userForm);
        User user = userService.getUserById(uid);
        model.addAttribute("user", user);
        return "/manage/showedituser";
    }



    @RequestMapping("userPortrait")
    public String userPortrait( HttpServletRequest request,Model model) {
        String uid =LoginHelper.getUserId();

        model.addAttribute("user", userService.getUserById(uid));
        model.addAttribute("sessionId", request.getSession().getId());

        return "/manage/portrait";
    }



    /**
     * 头像上传原始图片
     * @param model
     * @param logo
     * @param resquest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value="upload")
    public @ResponseBody String uploadOriginalImage(ModelMap model,@RequestParam("filename") MultipartFile logo,HttpServletRequest  resquest ) {

        try {
            if(logger.isDebugEnabled())logger.debug("#########################"+logo.getOriginalFilename());
            byte[] a = logo.getBytes();
            String filePath = resquest.getRealPath(resquest.getServletPath());

            int originalwidth = FileUtil.getImageWidth(logo.getInputStream());
            String name=FileUtil.uploadOriginalFileHandle(a, filePath, LoginHelper.getUserId(), logo.getOriginalFilename(),originalwidth);
            return resquest.getContextPath()+resquest.getServletPath()+"/"+name;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "null";
    }


    /**
     * 头像裁剪并压缩生成大小两张头像图片
     * @param model
     * @param logo
     * @param resquest
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value="portraithandle")
    public String portraitHandle(
            @RequestParam(value="width",required=false,defaultValue="0") int width,
            @RequestParam(value="heigth",required=false,defaultValue="0") int heigth,
            @RequestParam(value="x",required=false,defaultValue="0") int x,
            @RequestParam(value="y",required=false,defaultValue="0") int y,
            @RequestParam(value="url",required=false) String url,HttpServletRequest  resquest) {

        try {
            String uid=LoginHelper.getUserId();
            String filePath = resquest.getRealPath(url);
            String name=this.userService.ProHandle(filePath, uid, width, heigth, x, y);
            if(name!=null)this.userService.editPortrait(url.substring(0,url.lastIndexOf("/")+1)+name, uid);

            return "redirect:/user/userPortrait";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "null";
    }



}
