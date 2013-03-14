package solr.search;

import java.util.List;

import com.xiaozhi.blog.utils.ListPage;
import com.xiaozhi.blog.vo.User;

public interface EchoService {

	public abstract String echo(String name);

	public abstract List<User> echoList();

	public abstract ListPage<User> echoPage();

}