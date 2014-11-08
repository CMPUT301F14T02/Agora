package com.brogrammers.agora.test;

import junit.framework.TestCase;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;

import com.brogrammers.agora.data.CacheDataManager;
import com.brogrammers.agora.data.DeviceUser;
import com.brogrammers.agora.data.ESDataManager;
import com.brogrammers.agora.data.QuestionController;
import com.brogrammers.agora.views.MainActivity;

public class AddFavouriteTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public AddFavouriteTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
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
//			super("DOMAIN", "INDEX", "TYPE");
		}
	}
	
	private class TestController extends QuestionController {
		public TestController(DeviceUser user, CacheDataManager cache, ESDataManager es) {
			super(user, cache, es);
		}
	}
	
	public void testRemember() {
		DeviceUser user = new TestDeviceUser();
		CacheDataManager cache = new TestCacheManager();
		ESDataManager es = new TestESManager();
		QuestionController controller = new TestController(user, cache, es);
		Long qid = controller.addQuestion("Test Title D", "Test Body D", null);
		
		assertTrue(user.getAuthoredQuestionIDs().size() == 1);
		assertTrue(user.getAuthoredQuestionIDs().get(0) == qid);
		
	}
	
}