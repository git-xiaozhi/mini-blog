package solr.search;

import java.util.Arrays;
import java.util.List;

import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.utils.SolrPage;
import com.xiaozhi.blog.vo.User;

public class EchoImpl implements EchoService {


	/* (non-Javadoc)
	 * @see solr.search.EchoService#echo(java.lang.String)
	 */
	@Override
	public String echo(String name){

		return "Hello "+name;
	}

	@Override
	public List<User> echoList(){
		User user = new User();
		user.setId("1");
		user.setName("xiaozhi");

		User user1 = new User();
		user1.setId("2");
		user1.setName("xiaoran");

		List<User> returnList = Arrays.asList(user,user1);
		return returnList;
	}


	@Override
	public ListPage<User> echoPage(){
		User user = new User();
		user.setId("1");
		user.setName("我的商标没有注册，<font color='red'>商品</font>没法保证");

		User user1 = new User();
		user1.setId("2");
		user1.setName("xiaoran");

		List<User> returnList = Arrays.asList(user,user1);

		return new ListPage<User>(returnList,0,1,2);
	}


}
