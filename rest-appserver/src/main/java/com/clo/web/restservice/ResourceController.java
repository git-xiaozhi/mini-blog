package com.clo.web.restservice;




import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.service.BlogService;
import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.vo.WebPost;



@Controller
public class ResourceController{

	private static Log logger = LogFactory.getLog(ResourceController.class);

	@Autowired
    private BlogService blogService;
	private int pageSize = 10;


	@RequestMapping(value="/remote/hello",method=RequestMethod.GET)
	public ResponseEntity<String> echo(@RequestParam("name") String name) {
		logger.debug("------------------------------->"+name);
		return new ResponseEntity<String>("hello "+name,null,HttpStatus.OK);
	}
	
	
    /**
     * 登录用户(自己)首页
     * @param page
     * @param model
     * @return
     */
    @RequestMapping("/remote/main")
    public ResponseEntity<ListPage<WebPost>> root(@RequestParam(required = false,defaultValue="1") Integer page,Model model) {

        String targetUid = LoginHelper.getUserId();
        ListPage<WebPost> postsListPage = blogService.getTimelineByPage(targetUid, page,this.pageSize);
        return new ResponseEntity<ListPage<WebPost>>(postsListPage,null,HttpStatus.OK);

    }
	

//    /**
//     * 新增一个用户
//     * @param uservo
//     * @return
//     */
//	@RequestMapping(value="/remote/addUser",method=RequestMethod.POST)
//	public ResponseEntity<Boolean> addUser(UserVo entity) {
//		//logger.debug("------------------------------->uservo :"+uservo.toString());
//        try {
//			userService.insertUser(entity);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return new ResponseEntity<Boolean>(true,null,HttpStatus.OK);
//	}
//
//    /**
//     * 删除一个用户
//     * @param id
//     * @return
//     */
//	@RequestMapping(value="/remote/delUser/{id}",method=RequestMethod.DELETE)
//	public ResponseEntity<Boolean> delUser(@PathVariable int id) {
//        boolean result = userService.delUser(id);
//        //logger.debug("------------------------------->"+result);
//		return new ResponseEntity<Boolean>(result,null,HttpStatus.OK);
//	}
//
//    /**
//     * 修改用户名字
//     * @param username
//     * @param id
//     * @return
//     */
//	@RequestMapping(value="/remote/updateUser",method=RequestMethod.POST)
//	public ResponseEntity<Boolean> updateUser(UserVo entity) {
//        boolean result = userService.updateUser(entity.getUsername(), entity.getUserid());
//        //logger.debug("------------------------------->"+result);
//		return new ResponseEntity<Boolean>(result,null,HttpStatus.OK);
//	}
//
//
//	/**
//	 * 查找用户列表
//	 * @return
//	 */
//	@RequestMapping(value="/remote/findUsers",method=RequestMethod.GET)
//	public ResponseEntity<List<UserVo>> findUsers() {
//        List<UserVo> result = userService.findUsers();
//		return new ResponseEntity<List<UserVo>>(result,null,HttpStatus.OK);
//	}



}
