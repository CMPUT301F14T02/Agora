package com.brogrammers.agora.test;

import com.brogrammers.agora.data.DeviceUser;
import com.brogrammers.agora.model.Question;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import junit.framework.TestCase;

/**
 * Tests if Image Resize works
 * @author TN
 *
 */
public class ImageResizeTest extends TestCase {
	
	Resources res;
	Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
	
	private class TestDeviceUser extends DeviceUser {
		public TestDeviceUser() {
			setUsername("TestBingsF");
			favoritesPrefFileName = "TEST_FAVORITES";
			cachedPrefFileName = "TEST_CACHED";
			authoredPrefFileName = "TEST_AUTHORED";
			usernamePrefFileName = "TEST_USERNAME";
		}
	}
	
	void testResizeImage() {
		DeviceUser user = new TestDeviceUser();
		Question question0 = new Question("q0", "b0", picture, user);
		
		assertTrue("Image is not <64kb", question0.getImage().getByteCount() < 64);
	}	
}
