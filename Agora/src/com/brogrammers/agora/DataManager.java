package com.brogrammers.agora;

import java.io.UnsupportedEncodingException;
import java.util.List;


public interface DataManager {
	List<Question> getQuestions() throws UnsupportedEncodingException;
	boolean pushQuestion(Question q) throws UnsupportedEncodingException;
	boolean pushAnswer(Answer a, Long qID) throws UnsupportedEncodingException;
	boolean pushComment(Comment c, Long qID, Long aID) throws UnsupportedEncodingException;
}
