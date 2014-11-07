package com.brogrammers.agora;

import java.io.Serializable;
/**
 * Author data model. Contains information on user - the name.
 * @author Team 02
 *
 */
public class Author implements Serializable {
	/** 
	 * Implements a unique serial ID for the author
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



