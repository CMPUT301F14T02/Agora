package com.brogrammers.agora;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.widget.Toast;

public class LocalCache {
	private List<Observer> observerList;
	private ElasticSearch eSearch;
	static private LocalCache self;
	
	private TreeMap<Long, Question> questionCache; // for getQuestionByID()
	private List<QuestionPreview> questionPreviewList; // for getQuestions()

	/* Construction */
	
	private LocalCache() {
		questionCache = new TreeMap<Long, Question>();
		// load saved questions from file
		for (Question q : QuestionLoaderSaver.loadQuestions()) {
			questionCache.put(q.getID(), q);
		}
		eSearch = ElasticSearch.getInstance();
	}

	static public LocalCache getInstance() {
		if (self == null) {
			self = new LocalCache();
		}
		return self;
	}
	
	/* Methods */
	
	public void setQuestionPreviewList(List<QuestionPreview> qpList) {
		questionPreviewList = qpList;
		notifyUpdate();
	}
	
	public List<QuestionPreview> getQuestionPreviews() {
		eSearch.getQuestionPreviews();
		return questionPreviewList;
	}
	
	public void addObserver(Observer o) {
		observerList.add(o);
	}
	
	public void removeObserver(Observer o) {
		observerList.remove(o);
	}
	
	// Warning: this can return null if the question isn't in the cache.
	// In this case, the calling observer will be notified when the Question
	// is retrieved from ElasticSearch.
	public Question getQuestionByID(Long id) {
		eSearch.getQuestionByID(id);
		Question q = questionCache.get(id);
		return q;
	}
	
	public void setQuestion(Question q) {
		questionCache.put(q.getID(), q);
		QuestionLoaderSaver.saveQuestion(q);
		notifyUpdate();
	} 
	
	private void notifyUpdate() {
		for (Observer o : observerList) {
			o.update();
		}
	}
	
}