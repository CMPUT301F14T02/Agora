package com.brogrammers.agora.test;

import java.util.Date;

import com.brogrammers.agora.ElasticSearch;
import com.brogrammers.agora.QuestionController;

import junit.framework.TestCase;


public class SortByDateTest extends TestCase {
	QuestionController controller = QuestionController.getController();
	ElasticSearch webModel = ElasticSearch.getInstance();
	List<Question> questions = webModel.getQuestions();

	
	//Order should be {fourthDate, secondDate, thirdDate, firstDate}
	//because automatically, newest is first
	long firstID = controller.addQuestion("Test post please ignore", "ignore pls", null);
	long thirdID= controller.addQuestion("Why can't my Meowth talk?", "talk pls", null);
	long secondID = controller.addQuestion("Where is infinity and beyond?", "the claw", null);
	long fourthID = controller.addQuestion("There's a snake in my boot.", "howdy howdy howdy", null);
	
	//manually set date so fourth is oldest and first is now newest
	webModel.getQuestionById(fourthID).setDate(new Date());
	webModel.getQuestionById(thirdID).setDate(new Date());
	webModel.getQuestionById(secondID).setDate(new Date());
	webModel.getQuestionById(firstID).setDate(new Date());
	
	//assuming enum {VOTES, DATE};
	questions.setFilter(1);
	
	//order should now be { firstDate, secondDate, thirdDate, fourthDate}
	void testEnsureDateOrder() {
		assertTrue("firstDate not first", questions[0] == firstDate);
		assertTrue("secondDate not second", questions[1] == secondDate);
		assertTrue("thirdDate not third", questions[2] == thirdDate);
		assertTrue("fourthDate not last", questions[-1] == fourthDate);
		assertTrue("Correct count", questions.size() = 4);
	}
}
