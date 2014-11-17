package com.brogrammers.agora.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.data.DeviceUser;
import com.brogrammers.agora.helper.ImageGetter;
import com.brogrammers.agora.helper.ImageResizer;
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
	
	public void testImage() throws IOException {
		Bitmap image = BitmapFactory.decodeResource(Agora.getContext().getResources(), R.drawable.bingsf_sock);		
		assertTrue(image.getByteCount() > 64000);
		
		String path = "/sdcard/AgoraTest/";
		File folder = new File(path);
		if (!folder.exists()){
			folder.mkdir();
		}
		File testImage = new File(path + "test.png");
		testImage.createNewFile();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos); 
		FileOutputStream fos = new FileOutputStream(testImage);
		fos.write(baos.toByteArray());
		fos.flush();
		fos.close();
		
		Bitmap image2 = BitmapFactory.decodeFile(path + "test.png");
		assertTrue(image2.getByteCount() > 64000);
		
		byte[] resized = ImageResizer.resize(Uri.parse(path + "test.png"));
		
		assertTrue(resized.length <= 64000);
		
		testImage.delete();
		

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
