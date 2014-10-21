package com.brogrammers.agora;

import java.io.Serializable;

public class Author implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3747080879874796622L;
	protected String username;
	
	protected Author() { }
	
	public Author(String author) {
		this.username = author;
	}
	
	public String getUsername() {
		return username;
	}
}



