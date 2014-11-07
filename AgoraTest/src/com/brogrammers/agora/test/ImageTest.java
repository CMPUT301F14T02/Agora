package com.brogrammers.agora.test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.brogrammers.agora.Answer;
import com.brogrammers.agora.DeviceUser;
import com.brogrammers.agora.Question;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import junit.framework.TestCase;


/**
 * Tests images in the model without server
 * @author Group 2
 *
 */
public class ImageTest extends TestCase {
	
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

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testImage() {
		DeviceUser user = new TestDeviceUser();
		Question question0 = new Question("q0", "b0", picture, user);
		Question question1 = new Question("q1", "b1", null, user);
		
		Answer answer0 = new Answer("a0", null, user);
		Answer answer1 = new Answer("a1", picture, user);
		
		question0.addAnswer(answer0);
		question1.addAnswer(answer1);
		
		assertTrue("question 0 has no picture", question0.getImage() == picture);
		assertTrue("question 1 is not null", question1.getImage() == null);
		
		assertTrue("answer 0 is not null", answer0.getImage() == null);
		assertTrue("answer 1 has no picture", answer1.getImage() == picture);
	}
}