package com.brogrammers.agora;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Bitmap;
/**
 * Answer model data class. Contains information about Answer, including upvotes, comments, body, author.
 * 
 * @author Team 02
 *
 */
public class Answer implements Serializable {
	private static final long serialVersionUID = 1340865626156695502L;

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

	/**
	 * Answer constructor
	 * @param body text body of the answer
	 * @param image 
	 * @param author username of the author posting the answer
	 */
	public Answer(String body, Bitmap image, Author author) {
		this.body = body;
		this.image = image;
		this.author = author;
		rating = 0;
		date = System.currentTimeMillis();
		uniqueID = md5.hash(date.toString()+author.getUsername()+body);
		comments = new ArrayList<Comment>();
	}
	/**
	 * Returns number of comments 
	 * 
	 */
	public int countComments() {
		return comments.size();
	}
	/**
	 * returns list of comments attributed to the answer
	 * 
	 */
	public List<Comment> getComments() {
		// don't modify the list directly!
//		return Collections.unmodifiableList(comments);
		return comments;
	}
	/**
	 * Add comment to an answer.
	 * 
	 */
	public void addComment(Comment c) {
		comments.add(c);
	}
	/**
	 * Increment upvote once.
	 *
	 */
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
