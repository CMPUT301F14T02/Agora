package com.brogrammers.agora;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


import android.graphics.Bitmap;

public class Question implements Serializable, Comparable<Question> {
	private static final long serialVersionUID = -4895951515889586534L;

	private String title;
	private String body;
	private int rating;
	private Bitmap image = null;
	private Author author;
	private Long date;
	private Long uniqueID;
	private ArrayList<Comment> comments;
	private ArrayList<Answer> answers;

	public Question(String title, String body, Bitmap image, Author author) {
		this.title = title;
		this.body = body;
		this.author = author;
		this.image = image;
		rating = 0;
		date = System.currentTimeMillis();
		uniqueID = md5.hash(date.toString()+author.getUsername()+body+title);
		comments = new ArrayList<Comment>();
		answers = new ArrayList<Answer>();

	}
	
	public Question(Object o) {
		if (o == null) {
			this.title = null;
			this.body = null;
			this.author = null;
			this.image = null;
			rating = 0;
			date = null;
			uniqueID = null;
			comments = null;
			answers = null;
		}
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public int countComments() {
		if (comments == null) {
			return 0;
		} else {
			return comments.size();			
		}

	}
	
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

	public Bitmap getImage() {
		return image;
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


	@Override
	public int compareTo(Question other) {
		return uniqueID.compareTo(other.uniqueID);
	}

	public void setDate(Long date) {
		this.date = date;	
	}
}
