package com.brogrammers.agora.test;

import junit.framework.TestCase;

public class SortByVoteTest extends TestCase {

	QuestionController controller = QuestionController.getController();
	WebServiceModel webModel = WebServiceModel.getModel();
	List<Question> questions = weModel.getQuestions();
	
	Question tenVote = new Question("Test post please ignore", "ignore pls", null );
	Question fiveVote = new Question("Why can't my Meowth talk?", "talk pls", null );
	Question threeVote = new Question("Where is infinity and beyond?", "the claw", null );
	Question noVote = new Question("There's a snake in my boot.", "howdy howdy howdy", null );
	
	//Order should be {noVote, fiveVote, threeVote, tenVote}
	long noID = controller.addQuestion(noVote);
	long fiveID = controller.addQuestion(fiveVote);
	long threeVote = controller.addQuestion(threeVote);
	long tenID = controller.addQuestion(tenVote);
	
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
