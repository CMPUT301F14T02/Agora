package com.brogrammers.agora.test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.data.DeviceUser;
import com.brogrammers.agora.helper.ImageGetter;
import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.Question;
import com.brogrammers.agora.views.MainActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import junit.framework.TestCase;


/**
 * Tests images in the model without server
 * @author Group 2
 *
 */
public class ImageResizeTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	public ImageResizeTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testImage() {
		Bitmap image = BitmapFactory.decodeResource(Agora.getContext().getResources(), R.drawable.bingsf_sock);		
		assertTrue(image.getByteCount() > 64000);
		

	}
}

//package com.brogrammers.agora.test;
//
//import com.brogrammers.agora.data.DeviceUser;
//import com.brogrammers.agora.model.Question;
//
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import junit.framework.TestCase;
//
///**
// * Tests if Image Resize works
// * @author TN
// *
// */
//public class ImageResizeTest extends TestCase {
//	
//	Resources res;
//	Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
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
//	void testResizeImage() {
//		DeviceUser user = new TestDeviceUser();
//		Question question0 = new Question("q0", "b0", picture, user);
//		
//		assertTrue("Image is not <64kb", question0.getImage().getByteCount() < 64);
//	}	
//}
