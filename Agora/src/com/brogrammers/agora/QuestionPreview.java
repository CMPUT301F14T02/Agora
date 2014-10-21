package com.brogrammers.agora;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class QuestionPreview {
	public String title;
	public int rating;
	public Author author;
	public Long date;
	public int answerCount;
	public Long ID;
	public int version;

	QuestionPreview(String title, int rating, Author author, 
			Long date, int answerCount, Long ID, int version) {
		this.title = title;
		this.rating = rating;
		this.author = author;
		this.date = date;
		this.answerCount = answerCount;
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
