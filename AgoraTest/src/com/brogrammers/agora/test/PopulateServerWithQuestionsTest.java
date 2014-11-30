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

public class PopulateServerWithQuestionsTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public PopulateServerWithQuestionsTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		// Set the server mapping for nested answer objects
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private class TestESManager extends ESDataManager {
		public TestESManager() {
			super("http://cmput301.softwareprocess.es:8080/", "cmput301f14t02/", "agora/");
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

		
		Question q4 = new Question("Universal Appetite", "Are you hungry?", null, "Beet");
		Answer a1 = new Answer("It's all about perspective", null, "Bill");
		a1.addComment(new Comment("Yikes", new Author("Dr. Bob")));
		q.addAnswer(a1);
		q.addAnswer(new Answer("I mean who really knows?", null, "Bob"));
		Answer b1 = new Answer("This post doesn't belong here.", null, "Tim");
		b1.addComment(new Comment("It's a secret", new Author("Dr. Joe")));
		q4.addAnswer(b1);
		Answer c1 = new Answer("Who cares about perspective when you can't see.", null, "Tim");
		q4.addAnswer(c1);
		q4.addComment(new Comment("Wow", new Author("Eric")));
		// update the server with the new questions
		final ESDataManager es = new TestESManager();
		es.pushQuestion(q);
		es.pushQuestion(q2);
		es.pushQuestion(q3);
		es.pushQuestion(q4);
		
	}
}

	