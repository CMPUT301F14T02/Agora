package com.brogrammers.agora.test;

import junit.framework.TestCase;


public class UpvoteQuestionTest extends TestCase
{
//Assuming questions start with 0 upvotes
	QuestionController controller = QuestionController.getController();
	WebServiceModel webModel = WebServiceModel.getModel();

	long questionID = controller.addQuestion("I can't get past this road.", "There is a snorlax blocking the path.", null);

	//upvoting once
	Question testQuestion = webModel.getQuestionById(questionID);
	controller.upvote(questionID);
	
	assertTrue("Question is upvoted.", testQuestion.getRating()== 1);
	
	//upvoting 100 times
	for (int i = 1; i < 99; i++){
		controller.upvote(questionID);
	}
	assertTrue("Question is not upvoted 100 times.", testQuestion.getRating() == 100);
}
