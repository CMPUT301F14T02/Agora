package com.brogrammers.agora.test;

import java.util.List;

import com.brogrammers.agora.ElasticSearch;
import com.brogrammers.agora.Question;
import com.brogrammers.agora.QuestionController;

import junit.framework.TestCase;

public class SortByVoteTest extends TestCase {

	QuestionController controller = QuestionController.getController();
	ElasticSearch webModel = ElasticSearch.getInstance();
	List<Question> questions = webModel.getQuestions();

	//Order should be {noVote, fiveVote, threeVote, tenVote}
	long noID = controller.addQuestion("There's a snake in my boot.", "howdy howdy howdy", null );
	long fiveID = controller.addQuestion("Why can't my Meowth talk?", "talk pls", null );
	long threeID = controller.addQuestion("Where is infinity and beyond?", "the claw", null );
	long tenID = controller.addQuestion("Test post please ignore", "ignore pls", null );
	
	//upvoting
	for (int i = 0; i < 10; i++){
		controller.upvote(tenID);
	}
	for (int i = 0; i < 5; i++){
		controller.upvote(fiveID);
	}
	for (int i = 0; i < 3; i++){
		controller.upvote(threeID);
	}
	
	//assuming enum {VOTES, DATE};
	questions.sortBy(0);
	
	//order should now be { tenVote, fiveVote, threeVote, noVote}
	void testSortOnVote() {
		assertTrue("tenVote not first", questions[0] == tenVote);
		assertTrue("fiveVote not second", questions[1] == fiveVote);
		assertTrue("threeVote not third", questions[2] == threeVote);
		assertTrue("noVote not last", questions[-1] == noVote);
		assertTrue("Correct count", questions.size() = 4);
	}
}
