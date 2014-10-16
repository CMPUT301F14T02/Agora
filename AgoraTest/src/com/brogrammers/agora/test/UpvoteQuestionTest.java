package com.brogrammers.agora.test;

import junit.framework.TestCase;


public class UpvoteQuestionTest extends TestCase
{
//Assuming questions start with 0 upvotes
	QuestionController controller = QuestionController.getController();
	controller.addQuestion("Test Title", "Test Body", null);
	
	WebServiceModel webModel = WebServiceModel.getModel();
	List<Question> questions = 	webModel.getQuestions();
	
	testQuestion = questions.get(0);
	controller.upvote(testQuestion.getUniqueID());
	
	assertTrue("Question is upvoted.", testQuestion.getRating());
	
}
