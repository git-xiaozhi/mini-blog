package netty.model;

import java.io.Serializable;





public class BrodcastMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;


	public BrodcastMessage() {
		
	}


	public BrodcastMessage(String message) {
		super();
		this.message = message;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	@Override
	public String toString() {
		return "BrodcastMessage [message=" + message + "]";
	}




}
