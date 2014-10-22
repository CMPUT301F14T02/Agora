package com.brogrammers.agora;

import com.google.gson.Gson;

import android.graphics.Bitmap;

public class QuestionController {
	private DeviceUser user;
	private LocalCache cache;
	private ElasticSearch webservice;
	private ImageResizer resizer;
	
	static private QuestionController self = null;
	
	static public QuestionController getController() {
		if (self == null) {
			self = new QuestionController();
		}
		return self;
	}
	
	private QuestionController() {
		user = DeviceUser.getUser();
		cache = LocalCache.getInstance();
		webservice = ElasticSearch.getInstance();
		
	}
	
	public Long addQuestion(String title, String body, Bitmap image) {
		Question q = new Question(title, body, image, user);
		user.addAuthoredQuestionID(q.getID());
		q.setImage(resizer.resizeTo64KB(image));
		
		Gson gson = new Gson();
		String question = gson.toJson(q);
		String URI = ElasticSearch.DOMAIN + ElasticSearch.INDEXNAME + ElasticSearch.TYPENAME + q.getID();
//		QueryItem queueItem = new QueryItem(URI, questionParams, post);
		// TODO: Pass queue item to webservice for posting.
		
		cache.setQuestion(q);
		return q.getID(); // for testing
	}
	
	public Long addAnswer(String body, Bitmap image, Long qID) {
		Answer a = new Answer(body, image, user);
		a.setImage(resizer.resizeTo64KB(image));
		Question q = cache.getQuestionByID(qID);
		q.addAnswer(a);
		
		// TODO: generate query string and pass to webservice

		
		return a.getID();
	}
	
	// if adding a comment to a question, pass null for aID
	public void addComment(String body, Long qID, Long aID) {
		Comment c = new Comment(body);
		Question q = cache.getQuestionByID(qID);
		if (aID == null) {
			q.addComment(c);
		} else {
			q.getAnswerByID(aID).addComment(c);
		}
		
		// TODO: generate query string and pass to webservice

	}
	
	public void upvote(Long qID, Long aID) {
		Question q = cache.getQuestionByID(qID);
		if (aID == null) {
			// upvoting question
			q.upvote();
			// TODO: generate query string and pass to webservice
			// 
		} else {
			// upvoting answer
			q.getAnswerByID(aID).upvote();
			// TODO: generate query string and pass to webservice
		}
	}
	
}
