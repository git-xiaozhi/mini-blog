package org.redis.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;




@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/applicationContext.xml"})
public abstract class ServiceTestBase {

	public Log logger = LogFactory.getLog(ServiceTestBase.class);

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//org.springframework.core.annotation.AnnotationUtils
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

}
