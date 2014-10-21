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
		this.version = version;
	}
}
