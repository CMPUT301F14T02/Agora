package com.brogrammers.agora.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.brogrammers.agora.helper.md5;

import android.graphics.Bitmap;

/**
 * Answer model data class. Contains information about Answer, including
 * upvotes, comments, body, author. All answers should be contained by one
 * Question.
 * 
 * @author Group02
 * 
 */
public class Answer implements Serializable {
	private static final long serialVersionUID = 1340865626156695502L;

	private String body;
	private int rating;
	private byte[] image;
	private String author;
	private Long date;
	private Long uniqueID;
	private ArrayList<Comment> comments;
	private boolean hasImage;
	private SimpleLocation location;
	private String locationName;
	
	
	/**
	 * Answer constructor
	 * 
	 * @param body text body of the answer
	 * @param image Image will be set in body of the answer. To be implemented, null.
	 * @param author username of the author posting the answer
	 */
	public Answer(String body, byte[] image, String author) {
		this.body = body;
		this.image = image;
		this.author = author;
		rating = 0;
		date = System.currentTimeMillis();
		uniqueID = md5.hash(date.toString() + author + body);
		comments = new ArrayList<Comment>();
		hasImage = ((image != null) ? (image.length > 0) : false);
	}

	public boolean hasImage() {
		return hasImage;
	}
	
	public void setLocation(SimpleLocation location){
		this.location = location;
	}
	
	public void setLocationName(String locationName){
		this.locationName = locationName;
	}
	
	public SimpleLocation getLocation(){
		if (this.location != null){
            return location;
		} else {
			return null;
		}
	}

    public String getLocationName(){
		if (this.locationName != null){
            return locationName;
		} else {
			return null;
		}
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
		// return Collections.unmodifiableList(comments);
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

	public byte[] getImage() {
		return image;
	}

	public String getBody() {
		return body;
	}

	public int getRating() {
		return rating;
	}

	public String getAuthor() {
		return author;
	}

	public Long getDate() {
		return date;
	}

	public Long getID() {
		return uniqueID;
	}

	public void setImage(byte[] image) {
		this.image = image;
		this.hasImage = ((image != null) ? (image.length > 0) : false);
	}

}
