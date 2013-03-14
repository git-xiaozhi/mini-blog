package mina.remote.model;

import java.io.Serializable;
import java.util.Arrays;





public class OperatorMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String className;
	private String method;
	private Object[] params=new Object[]{};
	
	private boolean login = false;//是否是登录消息
	

	public OperatorMessage() {
		
	}


	public OperatorMessage(String className, String method, Object[] params) {
		super();
		this.className = className;
		this.method = method;
		this.params = params;
	}

	

	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}


	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		this.method = method;
	}


	public Object[] getParams() {
		return params;
	}


	public void setParams(Object[] params) {
		this.params = params;
	}



	public boolean getLogin() {
		return login;
	}


	public void setLogin(boolean login) {
		this.login = login;
	}


	@Override
	public String toString() {
		return "OperatorMessage [className=" + className + ", method=" + method
				+ ", params=" + Arrays.toString(params) + ", login=" + login
				+ "]";
	}


}
