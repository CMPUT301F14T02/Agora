/*package com.brogrammers.agora.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.brogrammers.agora.QuestionController;

import junit.framework.TestCase;

public class AddQuestionTest extends TestCase {
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
	Question addedQuestion = webServiceModel.searchQuestions('questionTitle');
	
	void testEnsureQuestionAdded() {
		assertTrue(addedQuestion.size() == 1);
		assertTrue(addedQuestion.get(0).title == questionTitle);
		assertTrue(addedQuestion.get(0).body == 'Body Test');
		assertTrue(addedQuestion.get(0).image != null);
		
	}
	
	LocalCacheModel localCacheModel = LocalCacheModel.getModel();
	Question addedQuestion = localCacheModel.getQuestions();
	void testLocalCacheModel() {
		assertTrue(addedQuestion.size() == 1);
		assertTrue(addedQuestion.get(0).title == questionTitle);
		assertTrue(addedQuestion.get(0).body == 'Body Test');
		assertTrue(addedQuestion.get(0).image != null);
	}
}
*/