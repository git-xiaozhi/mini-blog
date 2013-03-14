package netty.model;

import java.io.Serializable;

public class BrodcastMessage implements Serializable{

	private static final long serialVersionUID = -741021300299843538L;
	
	private String message;
	

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
