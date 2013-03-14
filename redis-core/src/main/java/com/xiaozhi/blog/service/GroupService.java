package com.xiaozhi.blog.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ArrayListMultimap;
import com.xiaozhi.blog.mongo.MongoUserGroupDao;
import com.xiaozhi.blog.utils.UserGroupHelper;
import com.xiaozhi.blog.vo.User;
import com.xiaozhi.blog.vo.UserGroup;

/**
 *
 * @author xiaozhi
 *
 */
@Service
public class GroupService {

	private static Log logger = LogFactory.getLog(GroupService.class);

	@Autowired
	private MongoUserGroupDao mongoUserGroupDao;

	 /**
     * 新增用户组
     * @param group
     * @return
     */
	public UserGroup addUserGroup(UserGroup group) {
		return this.mongoUserGroupDao.addUserGroup(group);
	}

	/**
	 * 删除用户组
	 * @param groupId
	 * @return
	 */
	public boolean delUserGroup(String groupId){

		return this.mongoUserGroupDao.delUserGroup(groupId);
	}

    /**
     * 更新用户组名称
     * @param groupId
     * @param groupName
     * @return
     */
	public UserGroup updateUserGroupName(String groupId,String groupName){

		return this.mongoUserGroupDao.updateUserGroupName(groupId, groupName);
	}

    /**
     * 加入一个用户到用户组
     * @param groupId
     * @param uid
     * @return
     */
	public UserGroup pushMemberToGroup(String groupId,String memberId){

		return this.mongoUserGroupDao.pushMemberToGroup(groupId, memberId);
	}

	/**
	 * 从用户组剔除一个用户
	 * @param groupId
	 * @param uid
	 * @return
	 */
	public UserGroup pullMemberFromGroup(String groupId,String memberId){

		return this.mongoUserGroupDao.pullMemberFromGroup(groupId, memberId);
	}

	/**
	 * 将一个用户从一个用户组转移到另一个用户组
	 * @param fromGroupId
	 * @param toGroupId
	 * @param uid
	 * @return
	 */
	public boolean moveUserToGroup(String fromGroupId,String toGroupId,String uid){

		return moveUserToGroup(fromGroupId, toGroupId, uid);
	}


	/**
	 * 通过用户组所有者Id获得用户组集合
	 * @param uid
	 * @return
	 */
	public List<UserGroup> getGroupByUserId(String uid){

		return this.mongoUserGroupDao.getGroupByUserId(uid);
	}

	/**
	 * 通过组Id获取该组的用户集合
	 * @param groupId
	 * @return
	 */
	public List<User> getMembersByGroupId(String groupId){

		return this.mongoUserGroupDao.getMembersByGroupId(groupId);
	}


    /**
     * 显示关注列表中一个用户的分组情况
     * @param owerId
     * @param userid
     * @return
     */
	public List<UserGroup> getMemberGroupShow(String owerId,String userid){

    	List<UserGroup> groups = this.mongoUserGroupDao.getGroupByUserId(owerId);
    	ArrayListMultimap<String,UserGroup> gMultimap = UserGroupHelper.getUserGroupsMulitMap(groups);

    	for(UserGroup group :groups){
    		if(gMultimap.get(userid).contains(group)) group.setSelected(true);
    	}
       return groups;
	}






}
