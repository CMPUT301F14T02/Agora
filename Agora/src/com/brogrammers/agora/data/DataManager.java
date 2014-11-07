package com.brogrammers.agora.data;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.Comment;
import com.brogrammers.agora.model.Question;

/** Implements the basic data functions required to work on a list of questions
 *  and add questions, add answers, and add comments to the list of question objects.
 *  All classes that implements the DataManager all require these set of functionalities.
 * 
 * @author Team 02
 *
 */


public interface DataManager {
	/**
	 * This gets the list of all the question objects in our app.
	 * @return List<Question> 		a list of questions
	 * @throws UnsupportedEncodingException
	 */
	List<Question> getQuestions() throws UnsupportedEncodingException;
	
	/**
	 * This pushes a question object into our question list
	 * 
	 * @param q		the question we are pushing
	 * @throws UnsupportedEncodingException
	 */
	void pushQuestion(Question q) throws UnsupportedEncodingException;
	
	
	/**
	 * Pushes an answer to the target question object that contains the list of answers
	 * @param a 	the answer object to add to the quesiton
	 * @param qID	the unique question ID (i.e. the location where the answer is going)
	 * @throws UnsupportedEncodingException
	 */
	void pushAnswer(Answer a, Long qID) throws UnsupportedEncodingException;
	
	
	/**
	 * Pushes a comment to the target question or answer
	 * @param c 		the comment object
	 * @param qID 		the target question ID
	 * @param aID 		the target answer ID
	 * @throws UnsupportedEncodingException
	 */
	void pushComment(Comment c, Long qID, Long aID) throws UnsupportedEncodingException;
}
