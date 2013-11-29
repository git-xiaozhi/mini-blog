package com.xiaozhi.blog.web.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tianji.test.core.redis.LoginHelper;
import com.xiaozhi.blog.formbean.UserGroupForm;
import com.xiaozhi.blog.service.GroupService;
import com.xiaozhi.blog.vo.UserGroup;



@Controller
@RequestMapping("/manage/group/")
public class GroupManageController {

	private static Log logger = LogFactory.getLog(GroupManageController.class);

    @Autowired
    private GroupService groupService;


    /**
     * 新增一个分组
     * @param groupName
     * @return
     */
    @RequestMapping(value = "addGroup", method = RequestMethod.POST)
    public @ResponseBody UserGroup addGroup(@RequestParam(value="groupName") String groupName) {
        String uid = LoginHelper.getUserId();
        UserGroup group = new UserGroup();
        group.setGroupId(UUID.randomUUID().toString());
        group.setGroupName(groupName);
        group.setOwnerId(uid);

        return groupService.addUserGroup(group);
    }

    /**
     * 删除一个用户分组
     * @param groupId
     * @return
     */
    @RequestMapping(value = "removeGroup/{groupId}", method = RequestMethod.POST)
    public @ResponseBody boolean removeGroup(@PathVariable String groupId) {
        return groupService.delUserGroup(groupId);
    }


    /**
     * 显示更改用户分组表单
     * @param userid
     * @param model
     * @return
     */
    @RequestMapping(value = "showMemberGroups/{userid}", method = RequestMethod.GET)
    public String showMemberGroups(@PathVariable String userid, Model model) {
    	String owerId = LoginHelper.getUserId();
    	List<UserGroup> groups = this.groupService.getMemberGroupShow(owerId, userid);

    	model.addAttribute("groups", groups);
    	model.addAttribute("userid", userid);

    	return "/manage/group/groupShow";
    }


    /**
     * 保存修改用户组操作
     * @param groupform
     * @return
     */
    @RequestMapping(value = "saveGroup", method = RequestMethod.POST)
    public String saveGroup(UserGroupForm groupform, Model model) {
    	List<UserGroup> userGroups = new ArrayList<UserGroup>();
    	String owerId = LoginHelper.getUserId();
    	List<UserGroup> groups = this.groupService.getGroupByUserId(owerId);
    	for(UserGroup group :groups){
    		this.groupService.pullMemberFromGroup(group.getGroupId(), groupform.getMemberId());
    		if(groupform.getGroupIds().contains(group.getGroupId())){
    			userGroups.add(this.groupService.pushMemberToGroup(group.getGroupId(), groupform.getMemberId()));
    		}
    	}

    	model.addAttribute("memberId", groupform.getMemberId());
    	model.addAttribute("groups", userGroups);

        return "/manage/group/usergroupslist";
    }

    /**
     * 将此人移除出此组
     * @param userid
     * @param model
     * @return
     */
    @RequestMapping(value = "pullGroups/{userid}/{groupId}", method = RequestMethod.POST)
    public @ResponseBody boolean pullGroups(@PathVariable String userid,@PathVariable String groupId) {
    	UserGroup group = this.groupService.pullMemberFromGroup(groupId, userid);
    	return group==null?false:true;
    }



}
