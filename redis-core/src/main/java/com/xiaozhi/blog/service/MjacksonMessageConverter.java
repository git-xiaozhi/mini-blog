package com.xiaozhi.blog.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.vo.WebPost;

public class MjacksonMessageConverter extends
		MappingJacksonHttpMessageConverter {
	private static Log logger = LogFactory.getLog(MjacksonMessageConverter.class);

	protected JavaType getJavaType(Class<?> clazz) {
		    if (ListPage.class.isAssignableFrom(clazz)) {
		      return TypeFactory.parametricType(ListPage.class, WebPost.class);
		    } else {
		      return super.getJavaType(clazz);
		    }
		    
		  }

}
