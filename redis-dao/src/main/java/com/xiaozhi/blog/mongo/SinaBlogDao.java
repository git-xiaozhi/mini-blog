package com.xiaozhi.blog.mongo;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.xiaozhi.blog.vo.SinaPost;



@Repository
public class SinaBlogDao {

	private static Log logger = LogFactory.getLog(SinaBlogDao.class);
	
	
	 @Autowired
	 private MongoOperations mongoTemplate;
	 
	 
	 public void setMongoTemplate(MongoOperations mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	/**
	  * 保存定时微博
	  * @param post
	  * @return
	  */
	 public SinaPost saveSinaPost(SinaPost post){
		try {
			this.mongoTemplate.save(post);
			return post;
		} catch (Exception e) {
			logger.error("---------------> saveSinaPost error :",e.fillInStackTrace());
			return null;
		}  
	 }
	 
	 /**
	  * 通过id获取一个定时微博
	  * @param id
	  * @return
	  */
	 public SinaPost getSinaPostById(long id){
		 return this.mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), SinaPost.class);
	 }
	 
	 /**
	  * 删除一个定时微博，并返回它的实体对象
	  * @param id
	  * @return
	  */
	 public SinaPost removeSinaPostById(long id){
		 SinaPost post = this.mongoTemplate.findAndRemove(new Query(Criteria.where("id").is(id)), SinaPost.class);
		 return post;
	 }
	 
	 
	 /**
	  * 返回当前页的微博列表
	  * @param uid
	  * @param firstResult
	  * @param pageSize
	  * @return
	  */
	 public List<SinaPost> findSinaPosts(String uid,int firstResult, int pageSize){
		 
		 Query query = new Query(Criteria.where("uid").is(uid));
		 query.skip(firstResult).limit(pageSize);
	     query.sort().on("delaySeconds",Order.ASCENDING);//按延迟发布时间正排序
	     List<SinaPost> posts = this.mongoTemplate.find(query,SinaPost.class);
	      
		return posts; 
	 }
	
	 /**
	  * 获取集合总数
	  * @param uid
	  * @return
	  */
	 public int getSinaPostCount(String uid){
		 
		 int totalCount = (int)this.mongoTemplate.count(new Query(Criteria.where("uid").is(uid)), SinaPost.class);
		 return totalCount;
	 }
	 
	 

}
