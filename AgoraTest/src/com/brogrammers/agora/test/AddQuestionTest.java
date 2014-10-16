package com.brogrammers.agora.test;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class AddQuestionTest extends TestCase {
	QuestionController controller = QuestionController.getController();
	String questionTitle = String.valueOf(System.currentTimeMillis()) + ' Test';
	
	BufferedImage image = null; 
	try {
	    img = ImageIO.read(new File("strawberry.jpg"));
	} catch (IOException e) {
	}
	
	controller.addQuestion(questionTitle, 'Test Body', image);
	
	WebServiceModel webServiceModel = WebServiceModel.getModel();
	Question addedQuestion = webServiceModel.searchQuestions('questionTitle');
	assertTrue(addedQuestion.size() == 1);
}
