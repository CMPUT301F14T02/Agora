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
import com.brogrammers.agora.Answer;
import com.brogrammers.agora.Author;
import com.brogrammers.agora.Comment;
import com.brogrammers.agora.ESDataManager;
import com.brogrammers.agora.MainActivity;
import com.brogrammers.agora.Question;
import com.brogrammers.agora.QuestionLoaderSaver;
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
			HttpDelete deleteRequest = new HttpDelete("http://cmput301.softwareprocess.es:8080/testing/agora/_query?q=_type:agora");
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
			super("http://cmput301.softwareprocess.es:8080/", "testing/", "agora/");
		}
	}
	
	
	public void testGetQuestionsByID() throws Throwable {
		// create a question object post it, and ensure we get back the same object.
		final Question q = new Question("Bad weird Questions", "It's weird isn't it?", null, new Author("Tod"));
		final Question q1 = new Question("Wow Questions", "What do you think the meaning of monkey is?", null, new Author("Tod"));
		final Question q2= new Question("Wowee Questions", "What do you think the meaning of life is?", null, new Author("Tod"));
		final Question q3 = new Question("Bigtime Questions", "What do you think the meaning of life is?", null, new Author("Tod"));

		
		// update the server with the new questions
		final ESDataManager es = new TestESManager();
		final CountDownLatch postSignal = new CountDownLatch(1);
		es.pushQuestion(q);
		es.pushQuestion(q1);
		es.pushQuestion(q2);
		es.pushQuestion(q3);
		postSignal.await(5, TimeUnit.SECONDS);
		
		// make the request
		final List<ArrayList<Question>> results = new ArrayList<ArrayList<Question>>();
		final CountDownLatch signal = new CountDownLatch(1);
		runTestOnUiThread(new Runnable() {
			public void run() {
				try {
					results.add((ArrayList<Question>)es.searchQuestions("weird"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}	
			}
		});

		try {
			signal.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
		
		// compare the local and received copies.
		assertTrue(results.get(0).size() == 1);
		Gson gson = new Gson();
		String jsonLocalQuestion = gson.toJson(q);
		String jsonReceivedQuestion = gson.toJson(results.get(0).get(0));
		assertTrue(jsonLocalQuestion.equals(jsonReceivedQuestion));
		
		
		// Search for a word not present in the posts ensure we get no results
		final List<ArrayList<Question>> resultsEmpty = new ArrayList<ArrayList<Question>>();
		runTestOnUiThread(new Runnable() {
			public void run() {
				try {
					resultsEmpty.add((ArrayList<Question>)es.searchQuestions("Memory"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}	
			}
		});

		try {
			signal.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
		
		// check that we get no results back.
		assertTrue(resultsEmpty.get(0).size() == 0);
		
		
		// Search for a word not present in all 4 posts and make sure we get all 4 back
		final List<ArrayList<Question>> results4 = new ArrayList<ArrayList<Question>>();
		runTestOnUiThread(new Runnable() {
			public void run() {
				try {
					results4.add((ArrayList<Question>)es.searchQuestions("Questions"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}	
			}
		});

		try {
			signal.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
		
		// check that we get 4 results back.
		assertTrue(results4.get(0).size() == 4);
	}
}