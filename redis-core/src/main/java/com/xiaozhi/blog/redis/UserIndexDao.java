package com.xiaozhi.blog.redis;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.stereotype.Component;

import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.vo.Range;

import solr.index.UserIndexData;

@Component
public class UserIndexDao {



    private static Log logger = LogFactory.getLog(UserIndexDao.class);

    @Autowired
    private  StringRedisTemplate template;

    /**
     * 分页获取所有用户
     * @param uid
     * @return
     */
    public List<UserIndexData> getGlobalUsers(Range range) {

        SortQuery<String> query = SortQueryBuilder.sort(KeyUtils.users()).noSort()
                .get("#")
                .get("uid:*->school")
                .get("uid:*->company")
                .get("uid:*->nickname")
                .limit(range.being, range.end-range.being+1).build();

        BulkMapper<UserIndexData, String> hm = new BulkMapper<UserIndexData, String>() {
            public UserIndexData mapBulk(List<String> bulk) {
                UserIndexData user = new UserIndexData();
                user.setId(bulk.get(0));
                user.setSchool(bulk.get(1));
                user.setCompany(bulk.get(2));
                user.setNickname(bulk.get(3));
                return user;
            }
        };
        return template.sort(query, hm);
    }


    /**
     * 返回总粉丝数
     * @param uid
     * @return
     */
    public int getGlobalUsersNum(){
        return template.opsForList().size(KeyUtils.users()).intValue();
    }

}
