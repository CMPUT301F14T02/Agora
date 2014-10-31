package com.brogrammers.agora.test;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	private boolean questionsAreEqual(Question q1, Question q2) {
		return (q1.getTitle() == q2.getTitle() &&
				q1.getBody() == q2.getBody() &&
				q1.getID() == q2.getID() &&
				q1.getAuthor().getUsername() == q2.getAuthor().getUsername() &&
				q1.getRating() == q2.getRating() && 
				q1.getDate() == q2.getDate());
				
			
	}
	static Question q1 = new Question("title", "Body", null, new Author("a"));
	public Question getQuestion() {
		return q1;
	}
	public Question mutateQuestion(Question q1) {
		q1.setBody("new body");
		return q1;
	}
	public void testQ() {
		Question a = getQuestion();
		mutateQuestion(getQuestion());
		Log.e("Test", a.getBody());
		Log.e("Test", mutateQuestion(a).getBody());
		
	}
	
}
	
	
	
	
	
/* OLD MOCK TEST */

/*package com.brogrammers.agora.test;


import java.io.File;
import java.io.IOException;
import java.net.URL;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.brogrammers.agora.Author;
import com.brogrammers.agora.MainActivity;
import com.brogrammers.agora.Question;
import com.brogrammers.agora.QuestionController;

import junit.framework.TestCase;

public class AddQuestionTest extends ActivityInstrumentationTestCase2<MainActivity> {
public AddQuestionTest(Class<MainActivity> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}

	//	QuestionController controller = QuestionController.getController();
//	String questionTitle = String.valueOf(System.currentTimeMillis());
//	Bitmap img = null;
	static Question q1 = new Question("Title", "Body", null, new Author("Joe"));

	Question getQuestion() {
		
//		Question q1 = new Question("Title", "Body", null, new Author("Joe"));
		
		return q1;
		
//		Long id = controller.addQuestion(questionTitle, "Body Test", img);
//		Question addedQuestion = controller.getQuestionById(id);
//		assertTrue(addedQuestion.getTitle() == questionTitle);
//		assertTrue(addedQuestion.getBody() == "Body Test");
//		assertTrue(addedQuestion.getImage() == null);
	}
	
		void modifyQuestion(Question q2) {
//			Question q2 = getQuestion();
			q2.setBody("New body");
//			return q2;
		}
	
	public void testMutable() {
		Question a = getQuestion();
		modifyQuestion(getQuestion());
		Log.e("Test", a.getBody());
		assertTrue(a.getBody().equals("New Body"));
	}
	
	
}
*/