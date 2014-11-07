package com.brogrammers.agora.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import com.brogrammers.agora.MainActivity;
import com.brogrammers.agora.data.CacheDataManager;
import com.brogrammers.agora.data.DeviceUser;
import com.brogrammers.agora.data.ESDataManager;
import com.brogrammers.agora.data.QuestionController;
import com.brogrammers.agora.model.Question;
/**
 * Tests images on server
 * @author Group2
 * 
 */
public class ImageTestES extends ActivityInstrumentationTestCase2<MainActivity> {

	Resources res;
	Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
	
	public ImageTestES() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		HttpClient client = new DefaultHttpClient();
		try {
			HttpDelete deleteRequest = new HttpDelete("http://cmput301.softwareprocess.es:8080/cmput301f14t02/testing/_query?q=_type:testing");
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
			super("http://cmput301.softwareprocess.es:8080/", "cmput301f14t02/", "testing/");
		}
	}
	
	private class TestController extends QuestionController {
		public TestController(DeviceUser user, CacheDataManager cache, ESDataManager es) {
			super(user, cache, es);
		}
	}
	
	public void testAddQuestion() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		final List<ArrayList<Question>> results = new ArrayList<ArrayList<Question>>();
		DeviceUser user = new TestDeviceUser();
		CacheDataManager cache = new TestCacheManager();
		final ESDataManager es = new TestESManager();
		QuestionController controller = new TestController(user, cache, es);
		

		Long qid = controller.addQuestion("Test Title D", "Test Body D", picture);

		// wait for it to be uploaded
		try {
			signal.await(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
		
		// check that the question was saved in Authored Questions
		assertTrue(user.getAuthoredQuestionIDs().size() == 1);
		assertTrue(user.getAuthoredQuestionIDs().get(0) == qid);
		
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
		assertTrue("List is empty", qList.size() == 1);
		assertTrue("Retrieved Question has wrong image", qList.get(0).getImage().equals(picture));
		
	}
}