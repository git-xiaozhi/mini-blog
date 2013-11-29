package com.xiaozhi.blog.web.manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.service.CollectBlogService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/manage/collect/")
public class CollectBlogManageController {


	private static Log logger = LogFactory.getLog(CollectBlogManageController.class);

	@Autowired
	private CollectBlogService collectBlogService;

	@Value(value="#{globalProperties['blog.list.pagesize']}")
    private int pageSize;


	/**
	 * 收藏列表
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "blogs", method = RequestMethod.GET)
	public String blogs(@RequestParam(value="page",defaultValue="1") Integer page, Model model) {


		String uid = LoginHelper.getUserId();

		model.addAttribute("pages",  collectBlogService.getCollectsByPage(uid, page, this.pageSize));

		return "/manage/collects";
	}


	@RequestMapping(value = "blogs/page", method = RequestMethod.GET)
	public String getCollects(@RequestParam(value="page",defaultValue="1") Integer page , Model model) {
		model.addAttribute("pages",  collectBlogService.getCollectsByPage(LoginHelper.getUserId(), page, this.pageSize));
		return "/fragments/collects";
	}


	/**
	 * 加入收藏
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "collectblog/{pid}", method = RequestMethod.GET)
	public @ResponseBody boolean follow(@PathVariable String pid, Model model) {

		return collectBlogService.collectBlog(LoginHelper.getUserId(), pid);
	}


	/**
	 * 取消收藏
	 * @param targetUser
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "removecllect/{pid}", method = RequestMethod.GET)
	public @ResponseBody boolean stopFollowing(@PathVariable String pid, Model model) {
		return collectBlogService.removeCollectBlog(LoginHelper.getUserId(), pid);
	}

}
