package com.brogrammers.agora;

import android.graphics.Bitmap;

public class QuestionController {
	private DeviceUser user;
	private LocalCacheModel cache;
	private WebserviceModel webservice;
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
		cache = LocalCacheModel.getCache();
		webservice = WebserviceModel.getWebservice();
		
	}
	
	public Long addQuestion(String title, String body, Bitmap image) {
		Question q = new Question(title, body, image, user);
		user.addAuthoredQuestionID(q.getID());
		q.setImage(resizer.resizeTo64KB());
		webservice.updateQuestion(q);
		return q.getID();
	}
	
	public Long addAnswer(String body, Bitmap image, Long qID) {
		Answer a = new Answer(body, image, user);
		a.setImage(resizer.resizeTo64KB());
		Question q = webservice.getQuestionByID(qID);
		q.addAnswer(a);
		// TODO: find a more efficient way to implement this:
		// (what facilities for modifying/adding-to data does elastic-search provide,
		// other than simple replacement?)
		webservice.updateQuestion(q);
		return a.getID();
	}
	
	// if adding a comment to a question, pass null for aID
	public void addComment(String body, Long qID, Long aID) {
		Comment c = new Comment(body);
		Question q = webservice.getQuestionByID(qID);
		if (aID == null) {
			q.addComment(c);
		} else {
			q.getAnswerByID(aID).addComment(c);
		}
		// TODO: find a more efficient way to implement this:
		// (what facilities for modifying/adding-to data does elastic-search provide,
		// other than simple replacement?)
		webservice.updateQuestion(q);
	}
	
	public void upvote(Question q) {
		q.upvote();
	}
	
	public void upvote(Answer a) {
		a.upvote();
	}
}
