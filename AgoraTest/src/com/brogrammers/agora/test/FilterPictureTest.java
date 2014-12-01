package com.brogrammers.agora.test;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;

import com.brogrammers.agora.data.DeviceUser;
import com.brogrammers.agora.helper.QuestionFilterer;
import com.brogrammers.agora.model.Question;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import junit.framework.TestCase;


public class FilterPictureTest extends TestCase {
	
	
	//contains both images and no image posts
	ArrayList<Question> imagelist = new ArrayList<Question>();
	//contains no images posts
	ArrayList<Question> plainlist = new ArrayList<Question>();
	

	public void testListHasImages() {

		byte[] image = (new String("foo")).getBytes();
		Question image1 = new Question("How do I beat rain teams?", "They are too fast. Thunder is too accurate in the rain.", image, "bingsf");
		Question image2 = new Question ("How do I beat sand teams?", "Tyranitar is a broken mon.", image, "bingsf2");
		Question noimage1 = new Question ("How many apricots do I need for a masterball", "I want one to catch a magikarp.", null, "bingsf3");
		Question noimage2 = new Question ("My pokemon won't listen to me.", "My friend traded me his lv100 Oddish and it won't listen to me. Please help!", null, "bingsf4");

		//filter by having an image
		imagelist.add(noimage1);

		imagelist.add(image1);
		
		imagelist.add(image2);
		imagelist.add(noimage2);

		QuestionFilterer fshelper = new QuestionFilterer();
		imagelist = (ArrayList<Question>) fshelper.filterOutImages(imagelist);
		
		assertTrue("List has no images.", imagelist.size() == 2);

		//filter by not having images
	
		plainlist.add(image1);
		plainlist.add(image2);
	
		plainlist = (ArrayList<Question>) fshelper.filterOutNoImages(plainlist);
	
		assertTrue("List not filtered by not having image.", plainlist.size() == 0);
	}
}