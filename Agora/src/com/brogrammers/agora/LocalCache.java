package com.brogrammers.agora;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.widget.Toast;

public class LocalCache {
	private TreeMap<Long, Question> questionCache;
	private ElasticSearch eSearch;
	static private LocalCache self;
	private List<QuestionPreview> questionPreviewList;
	private List<Observer> observerList;
	
	private LocalCache() {
		questionCache = new TreeMap<Long, Question>();
		// load saved questions from file
		for (Question q : QuestionLoaderSaver.loadQuestions()) {
			questionCache.put(q.getID(), q);
		}
		eSearch = ElasticSearch.getInstance();
	}
	
	public void update() {
		List<QuestionPreview> previews = eSearch.getQuestions(); 
		for (QuestionPreview qp : previews) {
			if (qp.version > getQuestionByID(qp.ID).getVersion()) {
				questionCache.put(qp.ID, eSearch.getQuestionByID(qp.ID));
			}
		}
	}
	
	static public LocalCache getInstance() {
		if (self == null) {
			self = new LocalCache();
		}
		return self;
	}
	
	public void setQuestionPreviewList(List<QuestionPreview> qpList){
		questionPreviewList = qpList;
		notifyUpdate();
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
	
	private void notifyUpdate() {
		for (Observer o : observerList) {
			o.update();
		}
	}
	
}

