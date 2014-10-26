/*package com.brogrammers.agora.test;

import com.brogrammers.agora.Answer;
import com.brogrammers.agora.ElasticSearch;
import com.brogrammers.agora.QuestionController;

import junit.framework.TestCase;


public class UpvoteAnswerTest extends TestCase
{
	//assuming answers start with 0 upvotes
	QuestionController controller = QuestionController.getController();
	ElasticSearch webModel = ElasticSearch.getInstance();

	//retrieving and adding answer
	long questionID = controller.addQuestion("Why won't my Pikachu evolve?", "He just slaps me.", null);
	long answerID = controller.addAnswer("You should love Pikachu for who he is", null, questionID);
	//upvoting
	controller.upvote(null, answerID);
	Answer testAnswer = webModel.getAnswerById(answerID);
	void testAnswerUpVote() {
		assertTrue("Answer is not upvoted.", testAnswer.getRating() == 1);
	}
	
	//upvoting 100 times
	for (int i = 0; i < 99; i++){
		controller.upvote(null, answerID);
	}
	void testUpvoteAnswerCount() {
		assertTrue("Answer is not upvoted 100 times.", testAnswer.getRating() == 100);
	}
}
*/