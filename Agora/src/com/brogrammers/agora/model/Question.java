package com.brogrammers.agora.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.brogrammers.agora.helper.md5;


import android.graphics.Bitmap;
/**
 * Question model data class. Contains information on the question, including
 * title, body, rating, image, author, answers, and comments.
 * 
 * @author Group02
 *
 */
public class Question implements Serializable, Comparable<Question> {
	private static final long serialVersionUID = -4895951515889586534L;

	private String title;
	private String body;
	private int rating;
	private byte[] image;
	private String author;
	private Long date;
	private Long uniqueID;
	private ArrayList<Comment> comments;
	private ArrayList<Answer> answers;
	private boolean hasImage;
	
	/**
	 * Question constructor. Date is created at time of creation.
	 * @param title String will be set as title/
	 * @param body String will be set as body.
	 * @param image Image to be set in the body of the image. Not yet implemented. Null.
	 * @param author Username of the author will be set as author of the question.
	 */
	public Question(String title, String body, byte[] image, String user) {
		this.title = title;
		this.body = body;
		this.author = user;
		this.image = image;
		rating = 0;
		date = System.currentTimeMillis();
		uniqueID = md5.hash(date.toString()+author+body+title);
		comments = new ArrayList<Comment>();
		answers = new ArrayList<Answer>();
		hasImage = ((image != null) ? (image.length > 0) : false);
	}
	
	public boolean hasImage() {
		return hasImage;
	}

	public void setImage(byte[] image) {
		this.image = image;
		hasImage = ((image != null) ? (image.length > 0) : false);
	}
	
	/**
	 * Returns size of comments list.
	 * @return
	 */
	public int countComments() {
		if (comments == null) {
			return 0;
		} else {
			return comments.size();			
		}

	}
	/**
	 * returns size of answers list.
	 * @return
	 */
	public int countAnswers() {
		if (answers == null) {
			return 0;
		} else {
			return answers.size();
		}
	}
	
	public void upvote() {
		rating++;
	}
	
	public void addComment(Comment c) {
		comments.add(c);
	}
	
	public void addAnswer(Answer a) {
		
		answers.add(a);
	}
	
	public List<Comment> getComments() {
		// don't modify the list directly!
		return Collections.unmodifiableList(comments);
	}
	
	public List<Answer> getAnswers() {
		// don't modify the list directly!
		return answers;
	}
	
	public Answer getAnswerByID(Long aID) {
		for (Answer a : answers) {
			if (a.getID().equals(aID)) {
				return a;
			}
		}
		return null;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public int getRating() {
		return rating;
	}

	public byte[] getImage() {
		return image;
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


	@Override
	public int compareTo(Question other) {
		return uniqueID.compareTo(other.uniqueID);
	}

	public void setDate(Long date) {
		this.date = date;	
	}
}
