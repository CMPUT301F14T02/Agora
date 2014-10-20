package com.brogrammers.agora;

import java.util.Date;
import com.brogrammers.agora.DeviceUser;

public class Comment {
	private Long date;
	private String body;
	private Author author;
	private boolean posted;
	
	public boolean isPosted() {
		return posted;
	}

	public void setPosted(boolean posted) {
		this.posted = posted;
	}

	
	public Comment(String text) {
		body = text;
		author = DeviceUser.getUser();
		date = System.currentTimeMillis();
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
	
	
}
