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
import com.brogrammers.agora.data.CacheDataManager;
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

public class AddCommentToQuestionTestES extends ActivityInstrumentationTestCase2<MainActivity> {

	public AddCommentToQuestionTestES() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		HttpClient client = new DefaultHttpClient();
		try {
			HttpDelete deleteRequest = new HttpDelete("http://cmput301.softwareprocess.es:8080/cmput301f14t02/AddCommentToQuestionTest/_query?q=_type:AddCommentToQuestionTest");
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
			super("http://cmput301.softwareprocess.es:8080/", "cmput301f14t02/", "AddCommentToQuestionTest/");
		}
	}
	
	private class TestCacheManager extends CacheDataManager {
		public TestCacheManager() {
			super("TEST_CACHE");
		}
	}
	
	public void testESGetQuestions() throws Throwable {
		// create a question object post it, add a comment locally.
		Question q = new Question("Big Questions", "What do you think the meaning of life is?", null, new Author("Ted"));
		Answer a = new Answer("Not really sure", null, new Author("Bill"));
		a.addComment(new Comment("Yikes", new Author("Dr. Bob")));
		q.addAnswer(a);
		q.addAnswer(new Answer("I mean who really knows?", null, new Author("Bob")));
		Answer b = new Answer("This post doesn't belong here.", null, new Author("Tim"));
		b.addComment(new Comment("It's a secret", new Author("Dr. Joe")));
		q.addAnswer(b);
		q.addComment(new Comment("Wow", new Author("Eric")));
		
		// update the server with the new question
		final ESDataManager es = new TestESManager();
		final CacheDataManager cache = new TestCacheManager();
		final CountDownLatch postSignal = new CountDownLatch(1);
		es.pushQuestion(q);
		postSignal.await(2, TimeUnit.SECONDS);
		
		// add a comment to the question locally
		Comment c = new Comment("Good Idea", new Author("Dr. Newhart"));
		q.addComment(c);
		
		// cache the question
		cache.pushQuestion(q);
		
		// push the new comment to the server.
		es.pushComment(c, q.getID(), null, cache);
		postSignal.await(2, TimeUnit.SECONDS);

		// get the question from the server
		final Long qID = q.getID();
		final List<ArrayList<Question>> results = new ArrayList<ArrayList<Question>>();
		final CountDownLatch signal = new CountDownLatch(1);
		runTestOnUiThread(new Runnable() {
			public void run() {
					results.add((ArrayList<Question>)es.getQuestionById(qID));
			}
		});
		
		// ensure is empty before receiving any response
		assertTrue("Got result before one was expected", results.get(0).size() == 0);
		try {
			signal.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
		
		// compare the local and received copies to ensure the server
		// copy matches the local copy.
		assertTrue("Expected at least 1 question, got none", results.get(0).size() > 0);
		Gson gson = new Gson();
		String jsonLocalQuestion = gson.toJson(q);
		String jsonReceivedQuestion = gson.toJson(results.get(0).get(0));
		assertTrue("Local question did not match its server copy", jsonLocalQuestion.equals(jsonReceivedQuestion));
	}
}

	