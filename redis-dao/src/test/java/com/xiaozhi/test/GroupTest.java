package com.xiaozhi.test;

import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xiaozhi.blog.mongo.MongoFollowingDao;
import com.xiaozhi.blog.mongo.MongoUserGroupDao;
import com.xiaozhi.blog.vo.Range;
import com.xiaozhi.blog.vo.User;
import com.xiaozhi.blog.vo.UserGroup;



public class GroupTest extends ServiceTestBase {


	@Autowired
	private MongoUserGroupDao mongoUserGroupDao;
	@Autowired
	private MongoFollowingDao mongoFollowingDao;


	//@Test
	public void addGroupTest(){
        UserGroup group = new UserGroup();
        group.setGroupId(UUID.randomUUID().toString());
        group.setGroupName("第二组");
        group.setOrderBy(2);
        group.setOwnerId("1");

        UserGroup returnGroup = this.mongoUserGroupDao.addUserGroup(group);
        logger.debug("----------------------------> group :"+returnGroup.toString());
	}

	@Test
	public void pushMemberTest(){

		UserGroup returnGroup = this.mongoUserGroupDao.pushMemberToGroup("5a58f98e-29ad-4150-9116-5495fb086df2", "2");
        logger.debug("----------------------------> group :"+returnGroup.toString());
	}


	@Test
	public void pullMemberTest(){

		UserGroup returnGroup = this.mongoUserGroupDao.pullMemberFromGroup("5a58f98e-29ad-4150-9116-5495fb086df2", "2");
        logger.debug("----------------------------> group :"+returnGroup.toString());
	}

	@Test
	public void getGroupsByUserIdTest(){
		List<UserGroup> groups = this.mongoUserGroupDao.getGroupByUserId("1");
		for(UserGroup group : groups){
		  logger.debug("----------------------------> group :"+group.toString());
		}
	}

	@Test
	public void getUsersByGroupIdTest(){
		List<User> users = this.mongoUserGroupDao.getMembersByGroupId("5a58f98e-29ad-4150-9116-5495fb086df2");
		for(User user : users){
		  logger.debug("----------------------------> user :"+user.toString());
		}
	}

	@Test
	public void updateGroupNameTest(){
		UserGroup group = this.mongoUserGroupDao.updateUserGroupName("5a58f98e-29ad-4150-9116-5495fb086df2", "改名的用户组");
		logger.debug("----------------------------> group :"+group.toString());
	}

	@Test
	public void userListTest(){
		Range range = new Range();
		range.setBeing(0);
		range.setEnd(9);
		List<User> users =  this.mongoFollowingDao.getFollowings("1", range);

		for(User user : users){
			logger.debug("----------------------------> user :"+user.toString());
		}
	}

}
