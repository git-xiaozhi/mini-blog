package com.xiaozhi.blog.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import com.tianji.test.core.redis.LoginHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import solr.search.BlogData;
import solr.search.SolrQueryService;
import solr.search.UserData;

import com.xiaozhi.blog.utils.SolrPage;


@Controller
public class SearchController {

    private static Log logger = LogFactory.getLog(SearchController.class);

    @Autowired
    @Qualifier("userSolrQueryClient")
    private SolrQueryService<UserData> userSolrQueryClient;
    @Autowired
    @Qualifier("blogSolrQueryClient")
    private SolrQueryService<BlogData> blogSolrQueryClient;

    @Value(value="#{globalProperties['blog.list.pagesize']}")
    private Integer pageSize;


    /**
     * 用户检索
     * @param page
     * @param key
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/search", method = RequestMethod.POST)
    public String userSearch(@RequestParam(required = false,defaultValue="1") Integer page,
            @RequestParam(required = true,defaultValue="abcdefghijk") String keyword, Model model) throws Exception {
        model.addAttribute("pages",this.userSolrQueryClient.queryHightLighting(LoginHelper.getUserId(),
        		"".equals(keyword)==true?"*":keyword, page, pageSize, true));
        model.addAttribute("keyword",keyword);
        return "/search/userlist";

    }

    /**
     * 微博检索
     * @param page
     * @param key
     * @param model
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/blog/search", method = RequestMethod.POST)
    public String blogSearch(@RequestParam(required = false,defaultValue="1") Integer page,
            @RequestParam(required = true,defaultValue="abcdefghijk") String keyword, Model model) throws Exception {

    	SolrPage<BlogData> posts = this.blogSolrQueryClient.queryHightLighting("".equals(keyword)==true?"*":keyword, page, pageSize, true);

        model.addAttribute("posts",posts);
        model.addAttribute("keyword",keyword);

        return "/search/bloglist";
    }



}
