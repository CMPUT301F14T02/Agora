package com.brogrammers.agora.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.brogrammers.agora.CacheDataManager;
import com.brogrammers.agora.DeviceUser;
import com.brogrammers.agora.ESDataManager;
import com.brogrammers.agora.MainActivity;
import com.brogrammers.agora.Question;
import com.brogrammers.agora.QuestionController;

public class AddAnswerWithControllerTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public AddAnswerWithControllerTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
//		HttpClient client = new DefaultHttpClient();
//		try {
//			HttpDelete deleteRequest = new HttpDelete("http://cmput301.softwareprocess.es:8080/cmput301f14t02/agora/_query?q=_type:agora");
//			client.execute(deleteRequest);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private class TestDeviceUser extends DeviceUser {
		public TestDeviceUser() {
			setUsername("TestBingsF");
			favoritesPrefFileName = "TEST_FAVORITES";
			cachedPrefFileName = "TEST_CACHED";
			authoredPrefFileName = "TEST_AUTHORED";
			usernamePrefFileName = "TEST_USERNAME";
		}
	}
	
	private class TestCacheManager extends CacheDataManager {
		public TestCacheManager() {
			super("TEST_CACHE");
		}
	}
	
	private class TestESManager extends ESDataManager {
		public TestESManager() {
			super("http://cmput301.softwareprocess.es:8080/", "cmput301f14t02/", "agora/");
		}
	}
	
	private class TestController extends QuestionController {
		public TestController(DeviceUser user, CacheDataManager cache, ESDataManager es) {
			super(user, cache, es);
		}
	}
	
	public void testRemember() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final List<ArrayList<Question>> results = new ArrayList<ArrayList<Question>>();
		DeviceUser user = new TestDeviceUser();
		CacheDataManager cache = new TestCacheManager();
		final ESDataManager es = new TestESManager();
		QuestionController controller = new TestController(user, cache, es);
		
		// create a question
		
		Long qid = controller.addQuestion("Thunderwave?", "OMG you guys, Mega Thunder Wave just got released! But only Mega Pikachu can learn it :(", null);
		Log.e("TEST", qid.toString());
		// wait for it to be uploaded
		try {
			signal.await(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
		
		assertTrue(cache.getQuestionById(qid) != null);
		Long aid = controller.addAnswer("Answer Body TW OP", null, qid);
		
		// wait for it to be uploaded
		try {
			signal.await(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
		
		// check that the question was pushed to the ES server
		runTestOnUiThread(new Runnable() { public void run() {
				try {
					results.add((ArrayList<Question>)es.getQuestions());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}	
			}
		});
		
		// wait for the response
		try {
			signal.await(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
		
		List<Question> qList = results.get(0);
		assertTrue("List is empty", qList.size() > 0);
		assertTrue("List has more than one item", qList.size() < 2);
		Question q2 = qList.get(0);
		assertTrue("Retrieved Question has wrong body", q2.getBody().equals("Test Body G"));
		assertTrue("Retrieved Question has wrong ID", q2.getID().equals(qid));
		assertTrue("Question does not have 1 answer", q2.countAnswers() == 1);
		assertTrue("Contained answer does not match aid", q2.getAnswerByID(aid).getID().equals(aid));
		assertTrue("Contained answer does not match body", q2.getAnswerByID(aid).getBody().equals("Answer Body H"));
		
		
	}
	
}