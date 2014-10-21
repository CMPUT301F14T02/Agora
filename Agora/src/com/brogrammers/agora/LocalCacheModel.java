package com.brogrammers.agora;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.widget.Toast;

public class LocalCacheModel {
	private TreeMap<Long, Question> questionCache;
	private WebserviceModel webservice;

	private LocalCacheModel() {
		questionCache = new TreeMap<Long, Question>();
		// load saved questions from file
		for (Question q : QuestionLoaderSaver.loadQuestions()) {
			questionCache.put(q.getID(), q);
		}
		webservice = WebserviceModel.getWebservice();
	}
	
	public void update() {
		List<QuestionPreview> previews = webservice.getQuestions(); 
		for (QuestionPreview qp : previews) {
			if (qp.version > getQuestionByID(qp.ID).getVersion()) {
				questionCache.put(qp.ID, webservice.getQuestionByID(qp.ID));
			}
		}
	}
	
	static public LocalCacheModel getLocalCacheModel() {
		if (self == null) {
			self = new LocalCacheModel();
		}
		return self;
	}
	
	static public void setQuestionPreviewList(List<QuestionPreview> qpList){
		questionPreviewList = qpList;
		//notifyUpdate();
	}
	}

	public List<Question> getQuestions() {
		return new ArrayList<Question>(questionCache.values());
	}
	
	public void addObserver(Observer o) {
		observerList.add(o);
	}
	
	public void removeObserver(Observer o) {
		observerList.remove(o);
	}
	
	// Returns null if the Question with the specified ID does not exist in the cache.
	// This should never happen.
	public Question getQuestionByID(Long id) {
		return questionCache.get(id);
	}
	
	public void addQuestion(Question q) {
		questionCache.put(q.getID(), q);
		QuestionLoaderSaver.saveQuestion(q);
	}
	
}

