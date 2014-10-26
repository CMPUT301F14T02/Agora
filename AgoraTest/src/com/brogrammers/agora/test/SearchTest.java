/*package com.brogrammers.agora.test;

import java.util.List;

import junit.framework.TestCase;

public class SearchTest extends TestCase {
	void testQuestionSearch() {
		QuestionController controller = QuestionController.getController();
		WebserviceModel model = WebserviceModel.getModel();
		Long qid1 = controller.addQuestion("Priority Moves",
				"What are the moves that will always go first? #GottaGoFast",
				null);
		Long qid2 = controller.addQuestion("Mudkip Moves",
				"What are the moves that will mudkip always have?"
				null);
		Long qid3 = controller.addQuestion("How to catch mudkip?",
				"I accidentally got a charmander but i want the mudkip, where is mudkip please"
				null);
		Long qid4 = controller.addQuestion("Prof Oak",
				"my friend said you can play as professor oak so how do you catch him"
				null);
		Long qid5 = controller.addQuestion("Giga Drain",
				"When does mudkip get giga drain i need to heal him cuz he's poisoned"
				null);
		
		List<Question> results = model.searchQuestions("mudkip");
		assertTrue("Didn't return 3 questions", results.size() == 3);
		for (Question q : results) {
			Long id = q.getID();
			assertTrue("Returned non-mudkip question", id==qid2 || id==qid3 || id==qid4 );
		}
				
		List<Question> results2 = model.searchQuestions("Quorums");
		assertTrue("Returned questions that don't match the search Quorums", results2.size() == 0);
	}
	
	void testAnswerSearch() {
		QuestionController controller = QuestionController.getController();
		WebserviceModel model = WebserviceModel.getModel();
		Long qid = controller.addQuestion("Giga Drain",
				"When does mudkip get giga drain i need to heal him cuz he's poisoned"
				null);
		Long aid1 = controller.addAnswer("The answer is that mudkip has both water and ground types.",
				null, qid);
		Long aid2 = controller.addAnswer("I think mudkip has great defensive capabilities",
				null, qid);
		Long aid3 = controller.addAnswer("Nobody uses that anymore, swampert is better.",
				null, qid);
		Long aid4 = controller.addAnswer("my mudkip evolved when i gave it a stone but i forget which one maybe someone else can tell you.",
				null, qid);
		Long aid5 = controller.addAnswer("I don't know, sorry.",
				null, qid);
		
		List<Answer> results = model.searchAnswers("mudkip");
		assertTrue("Didn't return 3 answers", results.size() == 3);
		for (Answer a : results) {
			Long id = a.getID();
			assertTrue("Returned non-mudkip question", id==aid1 || id==aid2 || id==aid4 );
		}
				
		List<Answer> results2 = model.searchAnswers("Quorums");
		assertTrue("Returned answers that don't match the search Quorums", results2.size() == 0);
	}
	
	
}
*/