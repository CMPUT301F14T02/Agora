package com.brogrammers.agora;

public class QuestionPreview {
	public String title;
	public int rating;
	public int answerCount;
	public long date;
	public Author author;
	public long ID;
	
	public QuestionPreview(String title, int rating, int answerCount, long date, Author author, long ID){
		this.title = title;
		this.rating = rating;
		this.answerCount = answerCount;
		this.date = date;
		this.author = author;
		this.ID = ID;	
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}
}
