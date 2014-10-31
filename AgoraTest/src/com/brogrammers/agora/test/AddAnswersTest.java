package com.brogrammers.agora.test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import com.brogrammers.agora.Answer;
import com.brogrammers.agora.Question;
import com.brogrammers.agora.QuestionController;

import junit.framework.TestCase;

public class AddAnswersTest extends TestCase{
	QuestionController controller = QuestionController.getController();
	String questionTitle1 = String.valueOf(System.currentTimeMillis());
	Bitmap img = null;
	
	Long qID = controller.addQuestion(questionTitle1, "Body Test", img);

	Question addedQuestion = controller.getQuestionById(qID);
	
	void testNoAnswers() {
		assertTrue(addedQuestion.countAnswers() == 0);
	}

	void testAnswerCount() {
		controller.addAnswer("Body Test", img, qID);
		controller.addAnswer("Body Test2", img, qID);
		controller.addAnswer("Body Test3", img, qID);
		assertTrue(addedQuestion.countAnswers() == 3);
	}
	
	// add another question with answers and ensure each question
	// only maintains references to its answers and not the other questions.
	String questionTitle2 = String.valueOf(System.currentTimeMillis());
	Long qID2 = controller.addQuestion(questionTitle2, "Body Test", img);
	Question addedQuestion2 = controller.getQuestionById(qID2	);
	
	// assumes the model returns the answer id
	
	// ensure the first question created does not have reference to the new answers.
	void testNoAnswerConflict() {
		Long a = controller.addAnswer("Body Test", img, qID2);
		Long b = controller.addAnswer("Body Test2", img, qID2);
 
		Question addedQuestion1 = controller.getQuestionById(qID);
		List<Answer> answerList = addedQuestion1.getAnswers();
		assertFalse(answerList.contains(a));
		assertFalse(answerList.contains(b));
	}
}
