package com.brogrammers.agora;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import android.widget.Toast;

public class CacheDataManager implements DataManager {

	static private CacheDataManager self;
	
	protected TreeMap<Long, Question> questionCache; // for getQuestionByID()
	protected List<QuestionPreview> questionPreviewList; // for getQuestions()
	
	QuestionLoaderSaver qls;

	/* Constructors */
	protected CacheDataManager() {
		questionCache = new TreeMap<Long, Question>();
		// load saved questions from file
		qls = new QuestionLoaderSaver();
		for (Question q : qls.loadQuestions()) {
			questionCache.put(q.getID(), q);
		}
	}
	
	protected CacheDataManager(String fileName) {
		questionCache = new TreeMap<Long, Question>();
		// load saved questions from file
		qls = new QuestionLoaderSaver(fileName);
		for (Question q : qls.loadQuestions()) {
			questionCache.put(q.getID(), q);
		}
	}

	static public CacheDataManager getInstance() {
		if (self == null) {
			self = new CacheDataManager();
		}
		return self;
	}
	
	public List<Question> getQuestions() {
		return Collections.unmodifiableList(new ArrayList<Question>(questionCache.values()));
	}
	
	// Warning: can return null
	public Question getQuestionById(Long id) {
		Question q = questionCache.get(id);
		return q;
	}
	
	public boolean pushQuestion(Question q) {
		questionCache.put(q.getID(), q);
		qls.saveQuestion(q);
		return true;
	} 
	
	public boolean pushAnswer(Answer a, Long qID) {
		Question q = questionCache.get(qID);
		q.addAnswer(a);
		pushQuestion(q);
		return true;
	}
	
	public boolean pushComment (Comment c, Long qID, Long aID) {
		Question q = questionCache.get(qID);
		if (aID != null) {
			Answer a = q.getAnswerByID(aID);
			a.addComment(c);
		} else {
			q.addComment(c);
		}
		pushQuestion(q);
		return true;
	}

}