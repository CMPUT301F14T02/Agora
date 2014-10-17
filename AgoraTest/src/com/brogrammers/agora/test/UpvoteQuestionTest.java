package com.brogrammers.agora.test;

import junit.framework.TestCase;


public class UpvoteQuestionTest extends TestCase
{
//Assuming questions start with 0 upvotes
	QuestionController controller = QuestionController.getController();
	controller.addQuestion("I can't get past this road.", "There is a snorlax blocking the path.", null);
	
	WebServiceModel webModel = WebServiceModel.getModel();
	List<Question> questions = 	webModel.getQuestions();
	
	testQuestion = questions.get(0);
	controller.upvote(testQuestion.getUniqueID());
	
	assertTrue("Question is upvoted.", testQuestion.getRating());
	
	//upvoting 100 times
	for (int i = 1; i < 99; i++){
		controller.upvote(testQuestion.getUniqueID());
	}
	assertTrue("Question is not upvoted 100 times.", testQuestion.getRating() == 100);
}
