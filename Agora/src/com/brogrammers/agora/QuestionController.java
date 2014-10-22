package com.brogrammers.agora;

import java.util.Map;

import com.brogrammers.agora.QueryItem.RequestType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import android.graphics.Bitmap;
import android.renderscript.Type;

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
		Type type = (Type) new TypeToken<Map<String, String>>(){}.getType();
		Map<String, String> paramMap = gson.fromJson(question, (java.lang.reflect.Type) type);
		RequestParams params = new RequestParams(paramMap);
		QueryItem queryItem = new QueryItem(params, URI, RequestType.POST);
		WebserviceModel.getWebserviceModel().updateServer(queryItem);
		
		cache.setQuestion(q);
		return q.getID(); // for testing
	}
	
	public Long addAnswer(String body, Bitmap image, Long qID) {
		Answer a = new Answer(body, image, user);
		a.setImage(resizer.resizeTo64KB(image));
		Question q = cache.getQuestionByID(qID);
		q.addAnswer(a);
		
		// TODO: generate query string and pass to webservice
		Gson gson = new Gson();
		String answer = gson.toJson(a);
		Type type = (Type) new TypeToken<Map<String, String>>(){}.getType();
		Map<String, String> paramMap = gson.fromJson(answer, (java.lang.reflect.Type) type);
		RequestParams params = new RequestParams(paramMap);
		String URI = DOMAIN + INDEX + TYPE + qID;
		QueryItem queryItem = new QueryItem(params, URI, RequestType.POST);
		WebserviceModel.getWebserviceModel().updateServer(queryItem);
		

		
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
