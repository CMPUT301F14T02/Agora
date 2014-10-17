package com.brogrammers.agora.test;

import java.util.Date;

import junit.framework.TestCase;


public class SortByDateTest extends TestCase {
	QuestionController controller = QuestionController.getController();
	WebServiceModel webModel = WebServiceModel.getModel();
	List<Question> questions = weModel.getQuestions();
	
	Question firstDate= new Question("Test post please ignore", "ignore pls", null );
	Question secondDate = new Question("Why can't my Meowth talk?", "talk pls", null );
	Question thirdDate = new Question("Where is infinity and beyond?", "the claw", null );
	Question fourthDate = new Question("There's a snake in my boot.", "howdy howdy howdy", null );
	
	//Order should be {fourthDate, secondDate, thirdDate, firstDate}
	//because automatically, newest is first
	long firstID = controller.addQuestion(noVote);
	long thirdID= controller.addQuestion(threeVote);
	long secondID = controller.addQuestion(fiveVote);
	long fourthID = controller.addQuestion(tenVote);
	
	//manually set date so fourth is oldest and first is now newest
	controller.getQuestionById(fourthID).setDate(Date());
	controller.getQuestionById(thirdID).setDate(Date());
	controller.getQuestionById(secondID).setDate(Date());
	controller.getQuestionById(firstID).setDate(Date());
	
	//assuming enum {VOTES, DATE};
	questions.setFilter(1);
	
	//order should now be { firstDate, secondDate, thirdDate, fourthDate}
	assertTrue("firstDate not first", questions[0] == firstDate);
	assertTrue("secondDate not second", questions[1] == secondDate);
	assertTrue("thirdDate not third", questions[2] == thirdDate);
	assertTrue("fourthDate not last", questions[-1] == fourthDate);
	assertTrue("Correct count", questions.size() = 4);
}
