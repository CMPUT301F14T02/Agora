package com.brogrammers.agora.test;

import com.brogrammers.agora.ElasticSearch;
import com.brogrammers.agora.Question;
import com.brogrammers.agora.QuestionController;

import junit.framework.TestCase;


public class UpvoteQuestionTest extends TestCase
{
//Assuming questions start with 0 upvotes
	QuestionController controller = QuestionController.getController();
	ElasticSearch webModel = ElasticSearch.getInstance();

	long questionID = controller.addQuestion("I can't get past this road.", "There is a snorlax blocking the path.", null);
	Question testQuestion = webModel.getQuestionByID(questionID);
	
	//upvoting once
	controller.upvote(questionID);
	
	void testQuestionUpvoted() {
		assertTrue("Question is upvoted.", testQuestion.getRating()== 1);
	}
	
	//upvoting 100 times
	for (int i = 0; i < 99; i++){
		controller.upvote(questionID);
	}
	
	void testQuestionUpvoteCount() {
		assertTrue("Question is not upvoted 100 times.", testQuestion.getRating() == 100);
	}
}
