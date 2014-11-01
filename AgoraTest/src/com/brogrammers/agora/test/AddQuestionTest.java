package com.brogrammers.agora.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.Author;
import com.brogrammers.agora.ESDataManager;
import com.brogrammers.agora.MainActivity;
import com.brogrammers.agora.Question;
import com.brogrammers.agora.QuestionLoaderSaver;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

public class AddQuestionTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public AddQuestionTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testESGetQuestions() throws Throwable {
		final ESDataManager es = ESDataManager.getInstance();
		final List<ArrayList<Question>> results = new ArrayList<ArrayList<Question>>();
		final CountDownLatch signal = new CountDownLatch(1);
		runTestOnUiThread(new Runnable() {
			public void run() {
		
				try {
					results.add((ArrayList<Question>)es.getQuestions());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}	
			}
		});
		assertTrue(results.get(0).size() == 0);
		try {
			signal.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
		assertTrue(results.get(0).size() > 0);
	}
}

	