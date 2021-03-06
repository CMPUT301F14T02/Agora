package com.brogrammers.agora.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.data.ESDataManager;
import com.brogrammers.agora.helper.QuestionLoaderSaver;
import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.Author;
import com.brogrammers.agora.model.Comment;
import com.brogrammers.agora.model.Question;
import com.brogrammers.agora.views.MainActivity;
import com.google.gson.Gson;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

public class SearchQuestionTestES extends ActivityInstrumentationTestCase2<MainActivity> {

	public SearchQuestionTestES() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		HttpClient client = new DefaultHttpClient();
		try {
			HttpDelete deleteRequest = new HttpDelete("http://cmput301.softwareprocess.es:8080/testing/SearchQuestionTest/_mapping");
			client.execute(deleteRequest);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private class TestESManager extends ESDataManager {
		public TestESManager() {
			super("http://cmput301.softwareprocess.es:8080/", "cmput301f14t02/", "SearchQuestionTest/");
		}
	}
	
	
	public void testGetQuestionsByID() throws Throwable {
		// create several question objects ensure only 1 contains "weird" 
		final Question q = new Question("Bad weird Questions", "It's weird isn't it?", null, "Tod");
		final Question q1 = new Question("Wow Questions", "What do you think the meaning of monkey is?", null, "Tod");
		final Question q2= new Question("Wowee Questions", "What do you think the meaning of life is?", null, "Tod");
		final Question q3 = new Question("Bigtime Questions", "What do you think the meaning of life is?", null, "Tod");
		final Question q4 = new Question("Bigtime Foo", "What do you think the meaning of life is?", null, "Tod");

		
		// update the server with the new questions
		final ESDataManager es = new TestESManager();
		final CountDownLatch postSignal = new CountDownLatch(1);
		es.pushQuestion(q);
		es.pushQuestion(q1);
		es.pushQuestion(q2);
		es.pushQuestion(q3);
		es.pushQuestion(q4);
		postSignal.await(5, TimeUnit.SECONDS);
		
		// Search all questions for the term "weird"
		final List<ArrayList<Question>> results = new ArrayList<ArrayList<Question>>();
		final CountDownLatch signal = new CountDownLatch(1);
		runTestOnUiThread(new Runnable() {
			public void run() {
				results.add((ArrayList<Question>)es.searchQuestions("weird"));
			}
		});
//		assertTrue("Received result before one was expected.", results.get(0).size() == 1);
		try {
			signal.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
		
		// ensure we only get 1 match back
		
		Gson gson = new Gson();
		String jsonLocalQuestion = gson.toJson(q);
		String jsonReceivedQuestion = gson.toJson(results.get(0).get(0));
		assertTrue("Local question did not match the question on the server.", jsonLocalQuestion.equals(jsonReceivedQuestion));
		
		
		// Search for a word not present in the posts ensure we get no results
		final List<ArrayList<Question>> resultsEmpty = new ArrayList<ArrayList<Question>>();
		runTestOnUiThread(new Runnable() {
			public void run() {
				resultsEmpty.add((ArrayList<Question>)es.searchQuestions("Memory"));
			}
		});

		try {
			signal.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
		
		// check that we get no results back.
		assertTrue("Received result before one was expected.", resultsEmpty.get(0).size() == 0);
		
		
		// Search for a word present in all 4 posts and make sure we get all 4 back
		final List<ArrayList<Question>> results4 = new ArrayList<ArrayList<Question>>();
		runTestOnUiThread(new Runnable() {
			public void run() {
				results4.add((ArrayList<Question>)es.searchQuestions("Questions"));
			}
		});

		try {
			signal.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
		
		// check that we get 4 results back.
		assertTrue("Expected 4 questions matching search but did not receive 4.", results4.get(0).size() == 4);
	}
}
