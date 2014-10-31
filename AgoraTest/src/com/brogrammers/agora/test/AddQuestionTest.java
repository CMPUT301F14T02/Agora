package com.brogrammers.agora.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import android.graphics.Bitmap;

import com.brogrammers.agora.Question;
import com.brogrammers.agora.QuestionController;

import junit.framework.TestCase;

public class AddQuestionTest extends TestCase {
	QuestionController controller = QuestionController.getController();
	String questionTitle = String.valueOf(System.currentTimeMillis());
	Bitmap img = null;

	void testAddQuestion() {
		Long id = controller.addQuestion(questionTitle, "Body Test", img);
		Question addedQuestion = controller.getQuestionById(id);
		assertTrue(addedQuestion.getTitle() == questionTitle);
		assertTrue(addedQuestion.getBody() == "Body Test");
		assertTrue(addedQuestion.getImage() == null);
	}
}
