package com.brogrammers.agora.test;

import com.brogrammers.agora.Answer;
import com.brogrammers.agora.Author;

import junit.framework.TestCase;

public class AnswerModelTest extends TestCase {
	public void answerConstructorTest(){
		String body = "The answer is 42.";
		Author testUser = new Author("Randy");
		Answer testAnswer = new Answer(body, null, testUser);
		assertTrue("Answer", testAnswer.getAuthor() == testUser);
	}
}
