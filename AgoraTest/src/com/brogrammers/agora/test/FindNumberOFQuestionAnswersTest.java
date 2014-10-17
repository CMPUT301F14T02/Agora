package com.brogrammers.agora.test;

import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;

public class FindNumberOFQuestionAnswersTest extends TestCase{
	QuestionController controller = QuestionController.getController();
	String questionTitle = String.valueOf(System.currentTimeMillis());
	
	// http://stackoverflow.com/questions/5882005/how-to-download-image-from-any-web-page-in-java
	// Author: planetjones
	// October 15, 2014
	Image img = null; 
	try {
	    URL url = new URL("http://www.4pipetools.com/external/commerce/1/gfx/thumb/MON1372.jpg");
		img = ImageIO.read(url);
	} catch (IOException e) {
	}
	
	long questionID = controller.addQuestion(questionTitle, 'Body Test', img);
	
	WebServiceModel webServiceModel = WebServiceModel.getModel();
	LocalCacheModel localCacheModel = LocalCacheModel.getModel();
	Question addedQuestion = webServiceModel.searchQuestions(questionTitle).get(0);
	Question addedQuestionCached = localCacheModel.getQuestions().get(0);
	assertTrue(addedQuestion.countAnswers() == 0);
	assertTrue(addedQuestionCached.countAnswers() == 0);

	long answerId1 = webServiceModel.addAnswer('Body Test', questionID, img);
	long answerId2 = webServiceModel.addAnswer('Body Test', questionID, img);
	long answerId3 = webServiceModel.addAnswer('Body Test', questionID, img);

	assertTrue(addedQuestion.countAnswer() == 3);
	assertTrue(addedQuestion.image != null);
	assertTrue(addedQuestionCached.countAnswers() == 3);
	assertTrue(addedQuestionCached.image != null);
	
	// add another question with answers and ensure each question
	// only maintains references to its answers and not the other questions.
	String questionTitle2 = String.valueOf(System.currentTimeMillis());
	long questionID2 = controller.addQuestion(questionTitle2, 'Body Test', img);
	Question addedQuestion2 = webServiceModel.searchQuestions(questionTitle2).get(0);
	
	// assumes the model returns the answer id
	ArrayList<long> answerIds = new ArrayList<long>;
	answerIds.add(webServiceModel.addAnswer('Body Test', questionID2, img));	
	answerIds.add(webServiceModel.addAnswer('Body Test', questionID2, img));
	answerIds.add(webServiceModel.addAnswer('Body Test', questionID2, img));
	
	// ensure the first question created does not have reference to the new answers.
	Question addedQuestion = webServiceModel.searchQuestions(questionTitle).get(0);
	for (Answer a: addedQuestion.answers){
		assertFalse(answerIds.contains(a.uniqueID));
	}

	
	
	

	
	

}
