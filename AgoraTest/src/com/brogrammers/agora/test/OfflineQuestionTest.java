//package com.brogrammers.agora.test;
//
//import java.io.IOException;
//import java.net.URL;
//
//import junit.framework.TestCase;
//
//public class OfflineQuestionTest extends TestCase {
//	/* Test if questions can be added if network is down
//	 * and tests if the question will be pushed when network comes back
//	 */
//	
//	QuestionController controller = QuestionController.getController();
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
//	WebServiceModel webServiceModel = WebServiceModel.getModel();
//	webServiceModel.closeConnection(); //Close Network
//	
//	//Add Question
//	controller.addQuestion('Question Title', 'Body Test', img);
//	
//	//Offline Test
//	void testNoQuestionAddedOffline() {
//		Question addedQuestion = webServiceModel.searchQuestions('Question Title');
//		assertTrue(addedQuestion.size() == 0);
//	}
//	
//	//Test Cache
//	void testLocalCacheHasQuestion() {
//		LocalCacheModel localCacheModel = LocalCacheModel.getModel();
//		Question addedQuestion = localCacheModel.getQuestions();
//		assertTrue(addedQuestion.size() == 1);
//		assertTrue(addedQuestion.get(0).title == 'Question Title');
//		assertTrue(addedQuestion.get(0).body == 'Body Test');
//		assertTrue(addedQuestion.get(0).image != null);
//	}
//
//	webServiceModel.openConnection(); //Open Network
//
//	//Test if question gets pushed if network opens
//	void testQuestionPushedOnNetworkOpen() {
//		Question addedQuestion = webServiceModel.searchQuestions('Question Title');
//		assertTrue(addedQuestion.size() == 1);
//		assertTrue(addedQuestion.get(0).title == 'Question Title');
//		assertTrue(addedQuestion.get(0).body == 'Body Test');
//		assertTrue(addedQuestion.get(0).image != null);
//	}
//}
