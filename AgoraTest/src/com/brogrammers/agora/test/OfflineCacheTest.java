//package com.brogrammers.agora.test;
//
//import java.io.IOException;
//import java.net.URL;
//
//import junit.framework.TestCase;
//
//public class OfflineCacheTest extends TestCase {
//	/* Tests if cached and favorite questions are still there when network
//	 * connection is down.
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
//
//	
//	controller.addQuestion('Question Title', 'Body Test', img);
//	controller.addQuestion('Question Title2', 'Body Test', img);
//	controller.cacheQuestion();
//	
//	Question testQuestion = webServiceModel.searchQuestions('Question Title');
//	Question testQuestion2 = webServiceModel.searchQuestions('Question Title2');
//	
//	controller.addFavorite(testQuestion.getID());
//	controller.addCache(testQuestion2.getID());
//	
//	webServiceModel.closeConnection(); //Close Network
//	
//	void testOfflineCache() {	
//		//Offline Test
//		Question addedQuestion = webServiceModel.searchQuestions('Question Title');
//		assertTrue(addedQuestion.size() == 0);
//	
//		//Test Cache
//		LocalCacheModel localCacheModel = LocalCacheModel.getModel();
//		Question addedQuestion = localCacheModel.getQuestions();
//		assertTrue(addedQuestion.size() == 2);
//		
//		assertTrue(addedQuestion.get(0).title == 'Question Title');
//		assertTrue(addedQuestion.get(0).body == 'Body Test');
//		assertTrue(addedQuestion.get(0).image != null);
//		
//		assertTrue(addedQuestion.get(1).title == 'Question Title2');
//		assertTrue(addedQuestion.get(1).body == 'Body Test');
//		assertTrue(addedQuestion.get(1).image != null);
//	}
//}
