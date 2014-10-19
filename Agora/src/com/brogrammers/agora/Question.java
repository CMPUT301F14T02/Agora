package com.brogrammers.agora;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Bitmap;

public class Question {
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
	
	public void setImage(Bitmap image) {
		this.image = image;
	}

	public int countComments() {
		return comments.size();
	}
	
	public int countAnswers() {
		return answers.size();
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
		return Collections.unmodifiableList(answers);
	}
	
	public Answer getAnswerByID(Long aID) {
		for (Answer a : answers) {
			if (a.getID() == aID) {
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

}