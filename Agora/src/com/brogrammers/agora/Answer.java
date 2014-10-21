package com.brogrammers.agora;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Bitmap;

public class Answer {
	private String body;
	private int rating;
	private Bitmap image = null;
	private Author author;
	private Long date;
	private Long uniqueID;
	private ArrayList<Comment> comments;
	private int version;
	private boolean posted;
	
	public boolean isPosted() {
		return posted;
	}

	public void setPosted(boolean posted) {
		this.posted = posted;
	}

	
	public Answer(String body, Bitmap image, Author author) {
		this.body = body;
		this.image = image;
		this.author = author;
		rating = 0;
		date = System.currentTimeMillis();
		uniqueID = md5.hash(date.toString()+author.getUsername()+body);
		comments = new ArrayList<Comment>();
	}
	
	public int countComments() {
		return comments.size();
	}
	
	public List<Comment> getComments() {
		// don't modify the list directly!
		return Collections.unmodifiableList(comments);
	}
	
	public void addComment(Comment c) {
		comments.add(c);
	}
	
	public void upvote() {
		rating++;
	}

	public Bitmap getImage() {
		return image;
	}

	public String getBody() {
		return body;
	}

	public int getRating() {
		return rating;
	}

	public Author getAuthor() {
		return author;
	}

	public Long getDate() {
		return date;
	}

	public Long getID() {
		return uniqueID;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}
	
	
}
