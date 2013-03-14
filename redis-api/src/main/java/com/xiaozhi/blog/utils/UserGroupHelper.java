package com.xiaozhi.blog.utils;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.xiaozhi.blog.vo.UserGroup;



public class UserGroupHelper {


	public static ArrayListMultimap<String,UserGroup>  getUserGroupsMulitMap(List<UserGroup> groups){
		ArrayListMultimap<String,UserGroup> multiMap = ArrayListMultimap.create();
		for(UserGroup group :groups){
			List<String> userids = group.getUids();
			if(userids!=null)
			for(String userid :userids){
				multiMap.put(userid, group);
			}
		}
		return multiMap;
	}

}
