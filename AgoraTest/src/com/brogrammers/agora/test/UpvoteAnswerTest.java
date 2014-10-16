package com.brogrammers.agora.test;

import junit.framework.TestCase;


public class UpvoteAnswerTest extends TestCase
{
	//assuming answers start with 0 upvotes
	QuestionController controller = QuestionController.getController();
	WebServiceModel webModel = WebServiceModel.getModel();
	
	//retrieving and adding answer
	controller.addQuestion("Test Title", "Test Body", null);
	List<Question> questions = webModel.getQuestions();
	testQuestion = questions[0]
	questionID = questions[0].getUniqueID();
	controller.addAnswer('Test Body', null, questionID);
	
	//upvoting
	ArrayList<Answers> testAnswers = testQuestion.getAnswers():
	testAnswer = testAnswers[0];

	
	assertTrue("Answer is upvoted.", testAnswer.getRating() == 1);
}
