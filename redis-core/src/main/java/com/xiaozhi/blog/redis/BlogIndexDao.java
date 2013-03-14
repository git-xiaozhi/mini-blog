package com.xiaozhi.blog.redis;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.stereotype.Component;

import com.xiaozhi.blog.utils.KeyUtils;
import com.xiaozhi.blog.utils.MentionUtil;
import com.xiaozhi.blog.vo.Post;
import com.xiaozhi.blog.vo.Range;

import solr.index.BlogIndexData;

@Component
public class BlogIndexDao {

     private static Log logger = LogFactory.getLog(BlogIndexDao.class);

        @Autowired
        private  StringRedisTemplate template;

        @Autowired
        private BlogDao blogDao;

        @Autowired
        private MentionUtil mentionUtil;

        private final HashMapper<Post, String, String> postMapper = new DecoratingStringHashMapper<Post>(new JacksonHashMapper<Post>(Post.class) {
        });


        /**
         * 获取所有微博
         * @param range
         * @return
         */
        public List<BlogIndexData> getAlltimeline(Range range) {
            return convertPidsToPosts(KeyUtils.timeline(), range);
        }

        /**
         * 获取所有微博数
         * @param uid
         * @return
         */
        public int getAllTimelineNum(){
            return template.opsForList().size(KeyUtils.timeline()).intValue();
        }


        /**
         * 获取微薄列表
         */
        private List<BlogIndexData> convertPidsToPosts(String key, Range range) {
            String pid = "pid:*->";
            final String pidKey = "#";
            final String content = "content";
            final String transmitid = "transmitid";


            SortQuery<String> query = SortQueryBuilder.sort(key).noSort()
            .get(pidKey)
            .get(pid + content)
            .get(pid + transmitid)
            .limit(range.being, range.end-range.being+1).build();

            BulkMapper<BlogIndexData, String> hm = new BulkMapper<BlogIndexData, String>() {
                public BlogIndexData mapBulk(List<String> bulk) {
                    Map<String, String> map = new LinkedHashMap<String, String>();
                    //Iterator<String> iterator = bulk.iterator();

                    String pid = bulk.get(0);
                    map.put(content, bulk.get(1));
                    map.put(transmitid, bulk.get(2));
                    return convertPost(pid, map);
                }
            };
            List<BlogIndexData> sort = template.sort(query, hm);
            return sort;
        }



        private BlogIndexData convertPost(String pid, Map<String,String> hash) {
            Post post = postMapper.fromHash(hash);

            if(post==null){
                BlogIndexData blogIndexData = new BlogIndexData();
                blogIndexData.setPid(pid);
                return  blogIndexData;
            }

            BlogIndexData blogIndexData = new BlogIndexData();
            blogIndexData.setContent(post.getContent());
            if(null!=post.getTransmitid() && !"".equals(post.getTransmitid())){//转发微薄
                blogIndexData.setForwardcontent(blogDao.getBlogById(post.getTransmitid(),true).getContent());
            }

            blogIndexData.setPid(pid);
            blogIndexData.setContent(mentionUtil.replaceMentions(post.getContent()));

            return blogIndexData;
        }

}
