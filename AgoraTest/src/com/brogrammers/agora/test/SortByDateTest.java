package com.brogrammers.agora.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import android.test.ActivityInstrumentationTestCase2;

import com.brogrammers.agora.CacheDataManager;
import com.brogrammers.agora.DeviceUser;
import com.brogrammers.agora.ESDataManager;
import com.brogrammers.agora.MainActivity;
import com.brogrammers.agora.Question;
import com.brogrammers.agora.QuestionController;

import junit.framework.TestCase;


public class SortByDateTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	
	public SortByDateTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		HttpClient client = new DefaultHttpClient();
		try {
			HttpDelete deleteRequest = new HttpDelete("http://cmput301.softwareprocess.es:8080/cmput301f14t02/agora/_query?q=_type:agora");
			client.execute(deleteRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public void testSorting() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final List<ArrayList<Question>> results = new ArrayList<ArrayList<Question>>();

		DeviceUser user = new TestDeviceUser();
		CacheDataManager cache = new TestCacheManager();
		final ESDataManager es = new TestESManager();
		QuestionController controller = new TestController(user, cache, es);
		
		ArrayList<Question> questions = null;
		
		//Order should be {fourthDate, secondDate, thirdDate, firstDate}
		//because automatically, newest is first
		Long firstID = controller.addQuestion("Test post please ignore", "ignore pls", null);
		Long thirdID= controller.addQuestion("Why can't my Meowth talk?", "talk pls", null);
		Long secondID = controller.addQuestion("Where is infinity and beyond?", "the claw", null);
		Long fourthID = controller.addQuestion("There's a snake in my boot.", "howdy howdy howdy", null);
		
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

		//manually set date so fourth is oldest and first is now newest
		controller.getQuestionById(fourthID).get(0).setDate(System.currentTimeMillis());
		try {
			signal.await(10, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		controller.getQuestionById(thirdID).get(0).setDate(System.currentTimeMillis());
		try {
			signal.await(10, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		controller.getQuestionById(secondID).get(0).setDate(System.currentTimeMillis());
		try {
			signal.await(10, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		controller.getQuestionById(firstID).get(0).setDate(System.currentTimeMillis());

		questions = results.get(0);
		Collections.sort(questions, new Comparator<Question>() {
		    public int compare(Question m1, Question m2) {
		        return m1.getDate().compareTo(m2.getDate());
		    }
		});
		
		//order should now be { firstDate, secondDate, thirdDate, fourthDate}
		assertTrue("firstDate not first", questions.get(3).getID() == firstID);
		assertTrue("secondDate not second", questions.get(2).getID() == secondID);
		assertTrue("thirdDate not third", questions.get(1).getID() == thirdID);
		assertTrue("fourthDate not last", questions.get(0).getID() == fourthID);
		assertTrue("Correct count", questions.size() == 4);

	}
}