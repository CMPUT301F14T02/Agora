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

public class AddQuestionTestES extends ActivityInstrumentationTestCase2<MainActivity> {

	public AddQuestionTestES() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		HttpClient client = new DefaultHttpClient();
		try {
			HttpDelete deleteRequest = new HttpDelete("http://cmput301.softwareprocess.es:8080/cmput301f14t02/AQTTES/_query?q=_type:AQTTES");	
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
			super("http://cmput301.softwareprocess.es:8080/", "cmput301f14t02/", "AQTTES/");
		}
	}
	
	public void testESGetQuestions() throws Throwable {
		// create a question object post it, and ensure we get back the same object.
		Question q = new Question("Big Questions", "What do you think the meaning of life is?", null, new Author("Ted"));
		Answer a = new Answer("Not really sure", null, new Author("Bill"));
		a.addComment(new Comment("Yikes", new Author("Dr. Bob")));
		q.addAnswer(a);
		q.addAnswer(new Answer("I mean who really knows?", null, new Author("Bob")));
		Answer b = new Answer("This post doesn't belong here.", null, new Author("Tim"));
		b.addComment(new Comment("It's a secret", new Author("Dr. Joe")));
		q.addAnswer(b);
		q.addComment(new Comment("Wow", new Author("Eric")));
		
		// add a second question
		Question q2 = new Question("Big Questions", "What do you think the meaning of life is?", null, new Author("Ted"));
		
		// update the server with the new questions
		final ESDataManager es = new TestESManager();
		final CountDownLatch postSignal = new CountDownLatch(1);
		es.pushQuestion(q);
		postSignal.await(1, TimeUnit.SECONDS);
		es.pushQuestion(q2);
		Log.i("SERVER", "Post second questions");
		postSignal.await(1, TimeUnit.SECONDS);

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
		assertTrue("Got result before one was expected", results.get(0).size() == 0);
		try {
			signal.await(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
		
		Log.i("SERVER", "Before first assert");
		// compare the local and received copies.
		assertTrue("Did not receive exactly 2 questions after pushing 2.", results.get(0).size() == 2);
		Gson gson = new Gson();
		String jsonLocalQuestion = gson.toJson(q);
		String jsonLocalQuestion2 = gson.toJson(q2);
		String jsonReceivedQuestion = gson.toJson(results.get(0).get(0));
		String jsonReceivedQuestion2 = gson.toJson(results.get(0).get(1));
		
		// can't guarantee order here so make sure the questions match each other
		assertTrue("Local question did not match server copy.", jsonLocalQuestion.equals(jsonReceivedQuestion) || jsonLocalQuestion.equals(jsonReceivedQuestion2));
		assertTrue("Local question did not match server copy.", jsonLocalQuestion2.equals(jsonReceivedQuestion) || jsonLocalQuestion2.equals(jsonReceivedQuestion2));
	}
}

	
