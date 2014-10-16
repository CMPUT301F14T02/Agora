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
	
	controller.addQuestion(questionTitle, 'Body Test', img);
	
	WebServiceModel webServiceModel = WebServiceModel.getModel();
	LocalCacheModel localCacheModel = LocalCacheModel.getModel();
	Question addedQuestion = webServiceModel.searchQuestions('questionTitle');
	Question addedQuestionCached = localCacheModel.searchQuestion('questionTitle');
	assertTrue(addedQuestion.countAnswers() == 0);
	assertTrue(addedQuestionCached.countAnswers() == 0);

	long questionID = addedQuestion.uniqueID;
	webServiceModel.addAnswer('Body Test', questionID, img);
	assertTrue(addedQuestion.countAnswer() == 1);
	assertTrue(addedQuestionCached.countAnswers() == 1);
}
