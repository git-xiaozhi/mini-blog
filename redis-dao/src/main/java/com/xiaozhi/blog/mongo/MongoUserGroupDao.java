package com.xiaozhi.blog.mongo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.xiaozhi.blog.vo.User;
import com.xiaozhi.blog.vo.UserGroup;

@Repository
public class MongoUserGroupDao {

	private static Log logger = LogFactory.getLog(MongoUserGroupDao.class);

	@Autowired
	private MongoOperations mongoTemplate;

	@Autowired
	private MongoUserDao mongoUserDao;


    /**
     * 新增用户组
     * @param group
     * @return
     */
	public UserGroup addUserGroup(UserGroup group) {
		try {
			this.mongoTemplate.save(group);
			return group;
		} catch (Exception e) {
			logger.error("-----------------> addUserGroup error :"+e.toString(),e);
			return null;
		}

	}

	/**
	 * 删除用户组
	 * @param groupId
	 * @return
	 */
	public boolean delUserGroup(String groupId){

		try {
			this.mongoTemplate.remove(new Query(Criteria.where("groupId").is(groupId)), UserGroup.class);
			return true;
		} catch (Exception e) {
			logger.error("-----------------> delUserGroup error :"+e.toString(),e);
			return false;
		}
	}

    /**
     * 更新用户组名称
     * @param groupId
     * @param groupName
     * @return
     */
	public UserGroup updateUserGroupName(String groupId,String groupName){

		try {
			Query query = new Query(Criteria.where("groupId").is(groupId));
			Update update = new Update().update("groupName", groupName);
			UserGroup group = this.mongoTemplate.findAndModify(query, update,new FindAndModifyOptions().returnNew(true).upsert(true), UserGroup.class);
			return group;
		} catch (Exception e) {
			logger.error("-----------------> addUserGroup error :"+e.toString(),e);
			return null;
		}

	}

    /**
     * 加入一个用户到用户组
     * @param groupId
     * @param uid
     * @return
     */
	public UserGroup pushMemberToGroup(String groupId,String uid){

		try {
			Query query = new Query(Criteria.where("groupId").is(groupId));
			Update update = new Update().push("uids", uid);
			UserGroup group = this.mongoTemplate.findAndModify(query, update,new FindAndModifyOptions().returnNew(true).upsert(true), UserGroup.class);
			return group;
		} catch (Exception e) {
			logger.error("-----------------> pushMemberToGroup error :"+e.toString(),e);
			return null;
		}

	}

	/**
	 * 从用户组剔除一个用户
	 * @param groupId
	 * @param uid
	 * @return
	 */
	public UserGroup pullMemberFromGroup(String groupId,String uid){

		try {
			Query query = new Query(Criteria.where("groupId").is(groupId));
			Update update = new Update().pull("uids", uid);
			UserGroup group = this.mongoTemplate.findAndModify(query, update,new FindAndModifyOptions().returnNew(true).upsert(true), UserGroup.class);
			return group;
		} catch (Exception e) {
			logger.error("-----------------> pullMemberFromGroup error :"+e.toString(),e);
			return null;
		}

	}

	/**
	 * 将一个用户从一个用户组转移到另一个用户组
	 * @param fromGroupId
	 * @param toGroupId
	 * @param uid
	 * @return
	 */
	public boolean moveUserToGroup(String fromGroupId,String toGroupId,String uid){

		try {
			this.mongoTemplate.updateFirst(new Query(Criteria.where("groupId").is(fromGroupId)), new Update().pull("uids", uid), UserGroup.class);
			this.mongoTemplate.updateFirst(new Query(Criteria.where("groupId").is(toGroupId)), new Update().push("uids", uid), UserGroup.class);
			return true;
		} catch (Exception e) {
			logger.error("-----------------> moveUserToGroup error :"+e.toString(),e);
			return false;
		}
	}


	/**
	 * 通过用户组所有者Id获得用户组集合
	 * @param uid
	 * @return
	 */
	public List<UserGroup> getGroupByUserId(String uid){

		Query query = new Query(Criteria.where("ownerId").is(uid));
		//query.sort().on("orderBy",Order.ASCENDING);//正序
		query.with(new Sort(Sort.Direction.ASC,"orderBy"));
		
    	List<UserGroup> groups = this.mongoTemplate.find(query,UserGroup.class);
    	return groups;
	}

	/**
	 * 通过组Id获取该组的用户集合
	 * @param groupId
	 * @return
	 */
	public List<User> getMembersByGroupId(String groupId){

		UserGroup group = this.mongoTemplate.findOne(new Query(Criteria.where("groupId").is(groupId)), UserGroup.class);
		if(group.getUids()==null)return new ArrayList<User>();
		List<User> users= this.mongoUserDao.getUserListByIds(group.getUids());

		return users;
	}




}
