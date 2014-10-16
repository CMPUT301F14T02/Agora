package com.brogrammers.agora.test;

import junit.framework.TestCase;


public class UpvoteQuestionTest extends TestCase
{
//Assuming questions start with 0 upvotes
	QuestionController controller = QuestionController.getController();
	controller.addQuestion(questionTitle, 'Body Body Body', 0);
	controller.upvote(questionID);
	
	assertTrue("Question is upvoted.", QuestionController.getQuestion(questionID).getRating() == 1);
	
}
