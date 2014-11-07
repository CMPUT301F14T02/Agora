package com.brogrammers.agora.test;

import java.net.URL;
import java.util.ArrayList;

import com.brogrammers.agora.DeviceUser;
import com.brogrammers.agora.Question;
import com.brogrammers.agora.test.SortLocalTest.TestDeviceUser;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import junit.framework.TestCase;


public class FilterPictureTest extends TestCase {
	
	
	//contains both images and no image posts
	ArrayList<Question> imagelist;
	//contains no images posts
	ArrayList<Question> plainlist;
	
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

	public void testListHasImages() {
		
		DeviceUser user = new TestDeviceUser();
		
		Question image1 = new Question("How do I beat rain teams?", "They are too fast. Thunder is too accurate in the rain.", null, user);
		Question image2 = new Question ("How do I beat sand teams?", "Tyranitar is a broken mon.", null, user);
		Question noimage1 = new Question ("How many apricots do I need for a masterball", "I want one to catch a magikarp.", null, user);
		Question noimage2 = new Question ("My pokemon won't listen to me.", "My friend traded me his lv100 Oddish and it won't listen to me. Please help!", null, user);

		image1.setImage(picture);
		image2.setImage(picture);

		//filter by having an image
		imagelist.add(image1);
		imagelist.add(image2);
		imagelist.add(noimage1);
		imagelist.add(noimage2);

		imagelist.filterOutImages(1);
		
		assertTrue("List has no images.", imagelist.size() == 2);

		//filter by not having images
	
		plainlist.add(image1);
		plainlist.add(image2);
	
		plainlist.filterOutNoImages(0);
	
		assertTrue("List not filtered by not having image.", plainlist.size() == 0);
	}
}