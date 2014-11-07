package com.brogrammers.agora.model;

import java.io.Serializable;
/**
 * Author data model. Contains information on user - the name as well as implementing 
 * a unique serial ID for the author.
 * 
 * @author Group02
 *
 */
public class Author implements Serializable {
	private static final long serialVersionUID = -3747080879874796622L;
	protected String username;
	
	protected Author() { }
	/**
	 * Author contstructor.
	 * @param author String will be set as the author's username.
	 */
	public Author(String author) {
		this.username = author;
	}
	
	public String getUsername() {
		return username;
	}
}



