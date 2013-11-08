package netty.server;

import netty.model.User;
import netty.server.coder.XLRequest;

import com.contact.entity.UserInfo;


public class ProxyService {
	
	
	
	public XLRequest helloUser(User user){
		/**这里需要将message 转化成同被代理服务器通讯的数据格式*/
		XLRequest xlRequest = new XLRequest();
		xlRequest.setEncode((byte)0);
		xlRequest.setCommand(-1);
		xlRequest.setValue("name", user.getName());
		xlRequest.setValue("password", user.getPassword());
		
		return xlRequest;
	}
	
	
	/**
	 * 新增通讯录中的用户信息
	 * @param userInfo
	 * @return
	 */
	public XLRequest addUser(UserInfo userInfo){
		
		XLRequest xlRequest = new XLRequest();
		xlRequest.setEncode((byte)0);
		xlRequest.setCommand(1);
		xlRequest.setValue("name", userInfo.getUsername());
		xlRequest.setValue("phone", userInfo.getUserPhone());
		xlRequest.setValue("imageid", Integer.toString(userInfo.getImageId()));
		
		return xlRequest;
	}
	
	
	/**
	 * 查询通讯录中的用户信息
	 * @param userInfo
	 * @return
	 */
	public XLRequest findUser(String userid){
		
		XLRequest xlRequest = new XLRequest();
		xlRequest.setEncode((byte)0);
		xlRequest.setCommand(2);
		xlRequest.setValue("userid", userid);
		
		return xlRequest;
	}
	
	
	/**
	 * 根据条件查询
	 * @param userid
	 * @return
	 */
	public XLRequest searchUser(String userid,String condition){
		
		XLRequest xlRequest = new XLRequest();
		xlRequest.setEncode((byte)0);
		xlRequest.setCommand(6);
		xlRequest.setValue("userid", userid);
		xlRequest.setValue("condition", condition);
		
		return xlRequest;
	}
   
	/**
	 * 删除
	 * @param userInfo
	 * @return
	 */
	public XLRequest delUser(String id){
		
		XLRequest xlRequest = new XLRequest();
		xlRequest.setEncode((byte)0);
		xlRequest.setCommand(3);
		xlRequest.setValue("id", id);
		
		return xlRequest;
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public XLRequest batchDeleteUser(String ids){
		
		XLRequest xlRequest = new XLRequest();
		xlRequest.setEncode((byte)0);
		xlRequest.setCommand(5);
		xlRequest.setValue("ids", ids);
		
		return xlRequest;
	}
	
	/**
	 * 修改
	 * @param userInfo
	 * @return
	 */
	public XLRequest modifyUser(UserInfo userInfo){
		
		XLRequest xlRequest = new XLRequest();
		xlRequest.setEncode((byte)0);
		xlRequest.setCommand(4);
		xlRequest.setValue("id", Integer.toString(userInfo.getId()));
		xlRequest.setValue("name", userInfo.getUsername());
		xlRequest.setValue("phone", userInfo.getUserPhone());
		xlRequest.setValue("imageid", Integer.toString(userInfo.getImageId()));
		
		return xlRequest;
	}
   

}
