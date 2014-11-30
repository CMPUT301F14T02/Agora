//package com.brogrammers.agora.test;
//
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpDelete;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//import android.test.ActivityInstrumentationTestCase2;
//
//import com.brogrammers.agora.data.CacheDataManager;
//import com.brogrammers.agora.data.DeviceUser;
//import com.brogrammers.agora.data.ESDataManager;
//import com.brogrammers.agora.data.QuestionController;
//import com.brogrammers.agora.model.Question;
//import com.brogrammers.agora.views.MainActivity;
//
//
//public class UpvoteQuestionTest extends ActivityInstrumentationTestCase2<MainActivity> {
//	
//	public UpvoteQuestionTest() {
//		super(MainActivity.class);
//	}
//
//	protected void setUp() throws Exception {
//		super.setUp();
//
//		HttpClient client = new DefaultHttpClient();
//		try {
//			HttpDelete deleteRequest = new HttpDelete("http://cmput301.softwareprocess.es:8080/cmput301f14t02/testing/_query?q=_type:testing");
//			client.execute(deleteRequest);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//	}
//
//	protected void tearDown() throws Exception {
//		super.tearDown();
//	}
//	
//	private class TestDeviceUser extends DeviceUser {
//		public TestDeviceUser() {
//			setUsername("TestBingsF");
//			favoritesPrefFileName = "TEST_FAVORITES";
//			cachedPrefFileName = "TEST_CACHED";
//			authoredPrefFileName = "TEST_AUTHORED";
//			usernamePrefFileName = "TEST_USERNAME";
//		}
//	}
//	
//	private class TestCacheManager extends CacheDataManager {
//		public TestCacheManager() {
//			super("TEST_CACHE");
//		}
//	}
//	
//	private class TestESManager extends ESDataManager {
//		public TestESManager() {
//			super("http://cmput301.softwareprocess.es:8080/", "cmput301f14t02/", "testing/");
//		}
//	}
//	
//	private class TestController extends QuestionController {
//		public TestController(DeviceUser user, CacheDataManager cache, ESDataManager es) {
//			super(user, cache, es);
//		}
//	}
//
//	
//	public void testQuestionUpvoted() throws Throwable {
//		DeviceUser user = new TestDeviceUser();
//		CacheDataManager cache = new TestCacheManager();
//		final ESDataManager es = new TestESManager();
//		QuestionController controller = new TestController(user, cache, es);
//		final List<ArrayList<Question>> results = new ArrayList<ArrayList<Question>>();
//		final CountDownLatch signal = new CountDownLatch(1);
//		
//		// upvoting once
//		Long questionID = controller.addQuestion("I can't get past this road.", "There is a snorlax blocking the path.", null);
//		controller.upvote(questionID, null);
//		// wait for it to be uploaded
//		try {
//			signal.await(1, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			assertTrue(false);
//		}
//		// check that the question was pushed to the ES server
//		runTestOnUiThread(new Runnable() { public void run() {
//				results.add((ArrayList<Question>)es.getQuestions());
//			}
//		});
//		
//		// wait for the response
//		try {
//			signal.await(2, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			assertTrue(false);
//		}
//		
//		List<Question> qList = results.get(0);
//		assertTrue(qList.get(0).getRating() == 1);
//	
//		
//		
//	}
////	questionID = controller.addQuestion("I can't get past this road.", "There is a snorlax blocking the path.", null);
////	controller.upvote(questionID, null);
////	Question question = controller.getQuestionById(questionID);
////	assertTrue("Question not upvoted.", question.getRating() == 1);
////	}
////	
////	//upvoting 100 times
////	public void testQuestion100UpvoteCount() {
////		for (int i = 0; i < 99; i++){
////			controller.upvote(questionID, null);
////		}
////		Question question = controller.getQuestionById(questionID);
////		assertTrue("Question is not upvoted 100 times.",
////				question.getRating() == 100);
////	}
//}
