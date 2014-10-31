/*
package com.brogrammers.agora.test;

import java.io.IOException;
import java.net.URL;

import android.graphics.Bitmap;
import android.provider.MediaStore.Images;

import com.brogrammers.agora.Author;
import com.brogrammers.agora.Question;
import com.brogrammers.agora.QuestionController;
import com.brogrammers.agora.WebServiceModel;

import junit.framework.TestCase;

public class OfflineCacheTest extends TestCase {
	 Tests if cached and favorite questions are still there when network
	 * connection is down.
	 

	QuestionController controller = QuestionController.getController();

	Bitmap img = null;
	Author jim = new Author("jim");

	void testOfflineCache() {	
	
		controller.addQuestion("Question Title", "Body Test", img);
		controller.addQuestion("Question Title2", "Body Test2", img);
		controller.cacheQuestion();
	
		controller.addFavorite(testQuestion.getID());
		controller.addCache(testQuestion2.getID());
	
		//Offline Test
		Question addedQuestion = webServiceModel.searchQuestions("Question Title");
		assertTrue(addedQuestion.size() == 0);
	
		//Test Cache
		LocalCacheModel localCacheModel = LocalCacheModel.getModel();
		Question addedQuestion = localCacheModel.getQuestions();
		assertTrue(addedQuestion.size() == 2);
		
		assertTrue(addedQuestion.get(0).title == "Question Title");
		assertTrue(addedQuestion.get(0).body == "Body Test");
		assertTrue(addedQuestion.get(0).image != null);
		
		assertTrue(addedQuestion.get(1).title == "Question Title2");
		assertTrue(addedQuestion.get(1).body == "Body Test");
		assertTrue(addedQuestion.get(1).image != null);
	}
}
*/