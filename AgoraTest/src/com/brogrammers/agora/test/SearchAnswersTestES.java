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

public class SearchAnswersTestES extends ActivityInstrumentationTestCase2<MainActivity> {

	public SearchAnswersTestES() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		HttpClient client = new DefaultHttpClient();
		try {
			HttpDelete deleteRequest = new HttpDelete("http://cmput301.softwareprocess.es:8080/cmput301f14t02/SearchAnswersTest/_query?q=_type:SearchAnswersTest");
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
			super("http://cmput301.softwareprocess.es:8080/", "cmput301f14t02/", "SearchAnswersTest/");
		}
	}
	
	public void testESGetQuestions() throws Throwable {
		// create a question object post it, and ensure we get back the same object.
		Question q = new Question("Big Questions", "What do you think the meaning of life is?", null, new Author("Ted"));
		Answer a = new Answer("It's all about perspective", null, new Author("Bill"));
		a.addComment(new Comment("Yikes", new Author("Dr. Bob")));
		q.addAnswer(a);
		q.addAnswer(new Answer("I mean who really knows?", null, new Author("Bob")));
		Answer b = new Answer("This post doesn't belong here.", null, new Author("Tim"));
		b.addComment(new Comment("It's a secret", new Author("Dr. Joe")));
		q.addAnswer(b);
		Answer c = new Answer("Who cares about perspective when you can't see.", null, new Author("Tim"));
		q.addAnswer(c);
		q.addComment(new Comment("Wow", new Author("Eric")));
		
		// add a second question
		Question q2 = new Question("Big Questions", "What do you think the meaning of life is?", null, new Author("Ted"));
		Answer d = new Answer("It's all about perspective", null, new Author("Bill"));
		q2.addAnswer(d);
		Question q3 = new Question("Small ideas", "Chocolate or vanilla is better?", null, new Author("Ted"));
		Answer e = new Answer("Who cares about vision when you can't see.", null, new Author("Tim"));
		// update the server with the new questions
		final ESDataManager es = new TestESManager();
		//es.setServerMapping();
		final CountDownLatch postSignal = new CountDownLatch(1);
		es.pushQuestion(q);
		postSignal.await(1, TimeUnit.SECONDS);
		es.pushQuestion(q2);
		postSignal.await(1, TimeUnit.SECONDS);
		es.pushQuestion(q3);
		postSignal.await(1, TimeUnit.SECONDS);

		final List<ArrayList<Answer>> results = new ArrayList<ArrayList<Answer>>();
		final CountDownLatch signal = new CountDownLatch(1);
		runTestOnUiThread(new Runnable() {
			public void run() {
				try {
					results.add((ArrayList<Answer>)es.searchAnswers("perspective"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}	
			}
		});
		assertTrue(results.get(0).size() == 0);
		try {
			signal.await(1, TimeUnit.SECONDS);
		} catch (InterruptedException f) {
			assertTrue(false);
		}
		
		// compare the local and received copies.
		assertTrue(results.get(0).size() == 3);
		Gson gson = new Gson();
		String locAnswer1 = gson.toJson(a);
		String locAnswer2 = gson.toJson(c);
		String locAnswer3 = gson.toJson(d);
		String recAnswer1 = gson.toJson(results.get(0).get(0));
		String recAnswer2 = gson.toJson(results.get(0).get(1));
		String recAnswer3 = gson.toJson(results.get(0).get(2));
		
		// can't guarantee order here so make sure the questions match each other
		assertTrue(locAnswer1.equals(recAnswer1) || locAnswer1.equals(recAnswer2) || locAnswer1.equals(recAnswer3));
		assertTrue(locAnswer2.equals(recAnswer1) || locAnswer2.equals(recAnswer2) || locAnswer2.equals(recAnswer3));
		assertTrue(locAnswer3.equals(recAnswer1) || locAnswer3.equals(recAnswer2) || locAnswer3.equals(recAnswer3));
	}
}

	