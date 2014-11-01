package com.brogrammers.agora.test;

import java.util.List;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.Author;
import com.brogrammers.agora.MainActivity;
import com.brogrammers.agora.Question;
import com.brogrammers.agora.QuestionLoaderSaver;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public class CacheSerializerTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public CacheSerializerTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSaveAndLoadQuestion() {
		QuestionLoaderSaver qls = new QuestionLoaderSaver("UNIT_TESTING");
		Agora.getContext().getSharedPreferences("UNIT_TESTING", Context.MODE_PRIVATE)
			.edit()
			.clear()
			.commit();
		// loading questions from empty file should return list of size 0
		assertEquals(qls.loadQuestions().size(), 0);
		
		Question q = new Question("How do I set the title of my pokemon team?", 
				"I want to know how I do that and also does snorlax learn body slam?",
				null, new Author("QuestionTesterDude99"));
		qls.saveQuestion(q);
		
		List<Question> qList = qls.loadQuestions();
		
		// now loading questions should return a list of size 1
		assertEquals(qList.size(), 1);
		
		Question q2 = qList.get(0);
		// the loaded question must be identical to the saved question
		assertEquals(q.getID(), q2.getID());
		assertEquals(q.getBody(), q2.getBody());
		assertEquals(q.getTitle(), q2.getTitle());
		assertEquals(q.getAuthor().getUsername(), q2.getAuthor().getUsername());
		
		
	}
	
}
