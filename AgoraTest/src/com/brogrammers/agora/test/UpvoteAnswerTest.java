package com.brogrammers.agora.test;

import android.test.ActivityInstrumentationTestCase2;

import com.brogrammers.agora.Answer;
import com.brogrammers.agora.MainActivity;
import com.brogrammers.agora.Question;
import com.brogrammers.agora.QuestionController;


//Activity not made yet
public class UpvoteAnswerTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	public UpvoteAnswerTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	// Assuming questions start with 0 upvotes
	static QuestionController controller = QuestionController.getController();
	static long questionID;
	static long answerID;

	// upvoting once
	void testQuestionUpvoted() {
	questionID = controller.addQuestion("I can't get past this road.", "There is a snorlax blocking the path.", null);
	answerID = controller.addAnswer("You should try the pokeflute.", null, questionID);
	controller.upvote(null, answerID);
	Question question = controller.getQuestionById(questionID);
	Answer answer = question.getAnswerByID(answerID);
	
	assertTrue("Answer not upvoted.", answer.getRating() == 1);
	}
	
	//upvoting 100 times
	void testQuestion100UpvoteCount() {
		Question question = controller.getQuestionById(questionID);
		Answer answer = question.getAnswerByID(answerID);
		
		for (int i = 0; i < 99; i++){
			controller.upvote(null, answerID);
		}
		assertTrue("Answer is not upvoted 100 times.",
				answer.getRating() == 100);
	}
}
