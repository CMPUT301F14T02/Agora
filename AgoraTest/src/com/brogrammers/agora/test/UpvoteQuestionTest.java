package com.brogrammers.agora.test;

import com.brogrammers.agora.Question;
import com.brogrammers.agora.QuestionController;

import junit.framework.TestCase;

public class UpvoteQuestionTest extends TestCase {
	// Assuming questions start with 0 upvotes
	QuestionController controller = QuestionController.getController();
	long questionID;

	// upvoting once
	void testQuestionUpvoted() {
	questionID = controller.addQuestion("I can't get past this road.", "There is a snorlax blocking the path.", null);
	controller.upvote(questionID, null);
	Question question = controller.getQuestionById(questionID);
	assertTrue("Question not upvoted.", question.getRating() == 1);
	}
	
	//upvoting 100 times
	void testQuestion100UpvoteCount() {
		for (int i = 0; i < 99; i++){
			controller.upvote(questionID, null);
		}
		Question question = controller.getQuestionById(questionID);
		assertTrue("Question is not upvoted 100 times.",
				question.getRating() == 100);
	}
}
