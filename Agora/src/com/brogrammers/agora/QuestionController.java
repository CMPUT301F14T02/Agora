package com.brogrammers.agora;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.brogrammers.agora.QueryItem.RequestType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

import android.graphics.Bitmap;
import android.renderscript.Type;

public class QuestionController {
	private static DeviceUser user;
	private static CacheDataManager cache;
	private static ESDataManager eSearch;
	private static ImageResizer resizer;
	
	static private QuestionController self = null;
	
	static public QuestionController getController() {
		if (self == null) {
			self = new QuestionController();
		}
		return self;
	}
	
//	Dependency injection, for testing.
	static public QuestionController getController(DeviceUser user, 
			CacheDataManager cache, ESDataManager eSearch) {
		if (self == null) {
			self = new QuestionController(user, cache, eSearch);
		}
		return self;
	}
	
	private QuestionController() {
		user = DeviceUser.getUser();
		cache = CacheDataManager.getInstance();
		eSearch = ESDataManager.getInstance();
	}
	
//	Dependency injection, for testing.
	private QuestionController(DeviceUser user_, 
			CacheDataManager cache_, ESDataManager eSearch_) {
		user = user_;
		cache = cache_;
		eSearch = eSearch_;
	}
	
	public Long addQuestion(String title, String body, Bitmap image) throws UnsupportedEncodingException {
		Question q = new Question(title, body, image, user);
//		q.setImage(resizer.resizeTo64KB(image)); TODO: implement
		q.setImage(null); // Images to be implemented in Part 4
		
		user.addAuthoredQuestionID(q.getID());
		eSearch.pushQuestion(q);
		
		return q.getID(); // for testing
	}
	
	public Long addAnswer(String body, Bitmap image, Long qID) {
		Answer a = new Answer(body, image, user);
		a.setImage(resizer.resizeTo64KB(image));
//		Question q = cache.getQuestionByID(qID);
//		q.addAnswer(a);
		
		// TODO: generate query string and pass to webservice
		Gson gson = new Gson();
		String answer = gson.toJson(a);
		String URI = ESDataManager.DOMAIN + ESDataManager.INDEXNAME + ESDataManager.TYPENAME + qID;
		//QueryItem queryItem = new QueryItem(params, URI, RequestType.POST);
		//eSearch.updateServer(queryItem);
		

		
		return a.getID();
	}
	
	// if adding a comment to a question, pass null for aID
	public void addComment(String body, Long qID, Long aID) {
		Comment c = new Comment(body);
//		Question q = cache.getQuestionByID(qID);
//		if (aID == null) {
//			q.addComment(c);
//		} else {
//			q.getAnswerByID(aID).addComment(c);
//		}
//		
		// TODO: generate query string and pass to webservice

	}
	
	public void upvote(Long qID, Long aID) {
//		Question q = cache.getQuestionByID(qID);
		if (aID == null) {
			// upvoting question
//			q.upvote();
			// TODO: generate query string and pass to webservice
			// 
		} else {
			// upvoting answer
//			q.getAnswerByID(aID).upvote();
			// TODO: generate query string and pass to webservice
		}
	}
	
}
