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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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

public class SearchAnswersTestES extends ActivityInstrumentationTestCase2<MainActivity> {

	public SearchAnswersTestES() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		// Set the server mapping for nested answer objects
		HttpClient client = new DefaultHttpClient();
		try {
			HttpDelete deleteRequest = new HttpDelete("http://cmput301.softwareprocess.es:8080/cmput301f14t02/SearchAnswers/_mapping");
			client.execute(deleteRequest);
			String mapping="{ \"SearchAnswers\": {\n"+
					" \"properties\": {\n"+
					" \"answers\": {\n"+
					" \"type\": \"nested\", \n"+
					" \"properties\": {\n"+
					" \"author\": {\n"+
					" \"type\": \"string\"\n"+
					" },\n"+
					" \"body\": {\n"+
					" \"type\": \"string\"\n"+
					" },\n"+
					" \"comments\": {\n"+
					" \"properties\": {\n"+
					" \"author\": {\n"+
					" \"properties\": {\n"+
					" \"username\": {\n"+
					" \"type\": \"string\"\n"+
					" }\n"+
					" }\n"+
					" },\n"+
					" \"body\": {\n"+
					" \"type\": \"string\"\n"+
					" },\n"+
					" \"date\": {\n"+
					" \"type\": \"long\"\n"+
					" },\n"+
					" \"posted\": {\n"+
					" \"type\": \"boolean\"\n"+
					" }\n"+
					" }\n"+
					" },\n"+
					" \"date\": {\n"+
					" \"type\": \"long\"\n"+
					" },\n"+
					" \"hasImage\": {\n"+
					" \"type\": \"boolean\"\n"+
					" },\n"+
					" \"rating\": {\n"+
					" \"type\": \"long\"\n"+
					" },\n"+
					" \"uniqueID\": {\n"+
					" \"type\": \"long\"\n"+
					" }\n"+
					" }\n"+
					" },\n"+
					" \"author\": {\n"+
					" \"type\": \"string\"\n"+
					" },\n"+
					" \"body\": {\n"+
					" \"type\": \"string\"\n"+
					" },\n"+
					" \"comments\": {\n"+
					" \"properties\": {\n"+
					" \"author\": {\n"+
					" \"properties\": {\n"+
					" \"username\": {\n"+
					" \"type\": \"string\"\n"+
					" }\n"+
					" }\n"+
					" },\n"+
					" \"body\": {\n"+
					" \"type\": \"string\"\n"+
					" },\n"+
					" \"date\": {\n"+
					" \"type\": \"long\"\n"+
					" },\n"+
					" \"posted\": {\n"+
					" \"type\": \"boolean\"\n"+
					" }\n"+
					" }\n"+
					" },\n"+
					" \"date\": {\n"+
					" \"type\": \"long\"\n"+
					" },\n"+
					" \"hasImage\": {\n"+
					" \"type\": \"boolean\"\n"+
					" },\n"+
					" \"location\": {\n"+
					" \"type\": \"geo_point\" \n"+
					" },\n"+
					" \"rating\": {\n"+
					" \"type\": \"long\"\n"+
					" },\n"+
					" \"title\": {\n"+
					" \"type\": \"string\"\n"+
					" },\n"+
					" \"uniqueID\": {\n"+
					" \"type\": \"long\"\n"+
					" }\n"+
					" }\n"+
					" }\n"+
					" }";
			HttpPost httppost = new HttpPost("http://cmput301.softwareprocess.es:8080/cmput301f14t02/SearchAnswers/_mapping");
			httppost.setEntity(new StringEntity(mapping));
			httppost.setHeader("Accept", "application/json");
			client.execute(httppost);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private class TestESManager extends ESDataManager {
		public TestESManager() {
			super("http://cmput301.softwareprocess.es:8080/", "cmput301f14t02/", "SearchAnswers/");
		}
	}
	
	public void testESGetQuestions() throws Throwable {
		// create a question objects with comments and answers attached
		Question q = new Question("Big Questions", "What do you think the meaning of life is?", null, "Ted");
		Answer a = new Answer("It's all about perspective", null, "Bill");
		a.addComment(new Comment("Yikes", new Author("Dr. Bob")));
		q.addAnswer(a);
		q.addAnswer(new Answer("I mean who really knows?", null, "Bob"));
		Answer b = new Answer("This post doesn't belong here.", null, "Tim");
		b.addComment(new Comment("It's a secret", new Author("Dr. Joe")));
		q.addAnswer(b);
		Answer c = new Answer("Who cares about perspective when you can't see.", null, "Tim");
		q.addAnswer(c);
		q.addComment(new Comment("Wow", new Author("Eric")));
		
		// add a second question
		Question q2 = new Question("Big Questions", "What do you think the meaning of life is?", null, "Ted");
		Answer d = new Answer("It's all about perspective", null, "Bill");
		q2.addAnswer(d);
		Question q3 = new Question("Small ideas", "Chocolate or vanilla is better?", null, "Ted");
		Answer e = new Answer("Who cares about vision when you can't see.", null, "Tim");

		// update the server with the new questions
		final ESDataManager es = new TestESManager();
		final CountDownLatch postSignal = new CountDownLatch(1);
		es.pushQuestion(q);
		postSignal.await(1, TimeUnit.SECONDS);
		es.pushQuestion(q2);
		postSignal.await(1, TimeUnit.SECONDS);
		es.pushQuestion(q3);
		postSignal.await(1, TimeUnit.SECONDS);
		
		// Search the newly posted objects for the word "perspective".
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
		
		// Ensure not results before we receive a server response for the search
		assertTrue("Received result before one was expected.", results.get(0).size() == 0);
		try {
			signal.await(1, TimeUnit.SECONDS);
		} catch (InterruptedException f) {
			assertTrue(false);
		}

		// Create json objects of the local and received question objects
		assertTrue(results.get(0).size() == 3);
		Gson gson = new Gson();
		String locAnswer1 = gson.toJson(a);
		String locAnswer2 = gson.toJson(c);
		String locAnswer3 = gson.toJson(d);
		String recAnswer1 = gson.toJson(results.get(0).get(0));
		String recAnswer2 = gson.toJson(results.get(0).get(1));
		String recAnswer3 = gson.toJson(results.get(0).get(2));
		
		// Make sure all the received objects match the their local counterparts
		// Can't guarantee order here so make sure the questions match each other
		assertTrue("Local answer did not match server counterpart.", locAnswer1.equals(recAnswer1) || locAnswer1.equals(recAnswer2) || locAnswer1.equals(recAnswer3));
		assertTrue("Local answer did not match server counterpart.", locAnswer2.equals(recAnswer1) || locAnswer2.equals(recAnswer2) || locAnswer2.equals(recAnswer3));
		assertTrue("Local answer did not match server counterpart.", locAnswer3.equals(recAnswer1) || locAnswer3.equals(recAnswer2) || locAnswer3.equals(recAnswer3));
	}
}

	