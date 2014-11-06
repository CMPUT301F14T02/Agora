package com.brogrammers.agora.test;

import java.util.List;
import android.test.ActivityInstrumentationTestCase2;
import com.brogrammers.agora.MainActivity;
import com.brogrammers.agora.Question;
import com.brogrammers.agora.QuestionController;


public class UpvoteQuestionTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	public UpvoteQuestionTest() {
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

	// upvoting once
//	public void testQuestionUpvoted() {
//	questionID = controller.addQuestion("I can't get past this road.", "There is a snorlax blocking the path.", null);
//	controller.upvote(questionID, null);
//	Question question = controller.getQuestionById(questionID);
//	assertTrue("Question not upvoted.", question.getRating() == 1);
//	}
//	
//	//upvoting 100 times
//	public void testQuestion100UpvoteCount() {
//		for (int i = 0; i < 99; i++){
//			controller.upvote(questionID, null);
//		}
//		Question question = controller.getQuestionById(questionID);
//		assertTrue("Question is not upvoted 100 times.",
//				question.getRating() == 100);
//	}
}
