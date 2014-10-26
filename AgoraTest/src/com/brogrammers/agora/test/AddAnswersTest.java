//package com.brogrammers.agora.test;
//
//import java.io.IOException;
//import java.net.URL;
//
//import com.brogrammers.agora.QuestionController;
//
//import junit.framework.TestCase;
//
//public class AddAnswersTest extends TestCase{
//	QuestionController controller = QuestionController.getController();
//	String questionTitle1 = String.valueOf(System.currentTimeMillis());
//	
//	// http://stackoverflow.com/questions/5882005/how-to-download-image-from-any-web-page-in-java
//	// Author: planetjones
//	// October 15, 2014
//	Image img = null; 
//	try {
//	    URL url = new URL("http://www.4pipetools.com/external/commerce/1/gfx/thumb/MON1372.jpg");
//		img = ImageIO.read(url);
//	} catch (IOException e) {
//	}
//	
//	long questionID1 = controller.addQuestion(questionTitle1, 'Body Test', img);
//	
//	WebserviceModel WebserviceModel = WebserviceModel.getModel();
//	LocalCacheModel localCacheModel = LocalCacheModel.getModel();
//	Question addedQuestion = WebserviceModel.searchQuestions(questionTitle1).get(0);
//	Question addedQuestionCached = localCacheModel.getQuestions().get(0);
//	
//	void testNoAnswers() {
//		assertTrue(addedQuestion.countAnswers() == 0);
//		assertTrue(addedQuestionCached.countAnswers() == 0);
//	}
//
//	ArrayList<long> answerIdsq1 = new ArrayList<long>();
//	answerIdsq1.add(WebserviceModel.addAnswer("Body Test", questionID1, img));
//	answerIdsq1.add(WebserviceModel.addAnswer("Body Test", questionID1, img));
//	answerIdsq1.add(WebserviceModel.addAnswer("Body Test", questionID1, img));
//
//	void testAnswerCount() {
//		assertTrue(addedQuestion.countAnswer() == 3);
//		assertTrue(addedQuestion.image != null);
//		assertTrue(addedQuestionCached.countAnswers() == 3);
//		assertTrue(addedQuestionCached.image != null);
//	}
//	
//	// add another question with answers and ensure each question
//	// only maintains references to its answers and not the other questions.
//	String questionTitle2 = String.valueOf(System.currentTimeMillis());
//	long questionID2 = controller.addQuestion(questionTitle2, 'Body Test', img);
//	Question addedQuestion2 = WebserviceModel.searchQuestions(questionTitle2).get(0);
//	
//	// assumes the model returns the answer id
//	ArrayList<long> answerIdsq2 = new ArrayList<long>;
//	answerIdsq2.add(WebserviceModel.addAnswer('Body Test', questionID2, img));	
//	answerIdsq2.add(WebserviceModel.addAnswer('Body Test', questionID2, img));
//	answerIdsq2.add(WebserviceModel.addAnswer('Body Test', questionID2, img));
//	
//	// ensure the first question created does not have reference to the new answers.
//	void testNoAnswerConflict() {
//		Question addedQuestion1 = WebserviceModel.searchQuestions(questionTitle1).get(0);
//		for (Answer a: addedQuestion1.answers){
//			assertFalse(answerIdsq2.contains(a.uniqueID));
//		}
//	}
//	
//	void testNoAnswerQuestion2() {
//		// ensure the first question created does not have reference to the new answers.
//		Question addedQuestion2 = WebserviceModel.searchQuestions(questionTitle2).get(0);
//		for (Answer a: addedQuestion2.answers){
//			assertFalse(answerIdsq1.contains(a.uniqueID));
//		}
//	}
//}
