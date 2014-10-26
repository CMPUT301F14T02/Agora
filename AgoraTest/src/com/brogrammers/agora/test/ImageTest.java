/*package com.brogrammers.agora.test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import junit.framework.TestCase;


//##### Test Cases
//* Have a quesiton with an image and an answer with a different image. We should be able to retrieve two different from question and answer.
//*  Have a quesiton with an image and an answer with no image. We should be able to retrieve an image from question, and 'null from answer'

public class ImageTest extends TestCase {
	
	Image img1 = null;
	Image img2 = null;
	Image img3 = null;
	
	try {
	    URL url1 = new URL("http://cdn.bulbagarden.net/upload/thumb/b/ba/123Scyther.png/250px-123Scyther.png");
		img1 = ImageIO.read(url1);
	} catch (IOException e) {
	}
	
	try {
	    URL url2 = new URL("http://cdn.bulbagarden.net/upload/thumb/a/a5/184Azumarill.png/250px-184Azumarill.png");
		img2 = ImageIO.read(url2);
	} catch (IOException e) {
	}
	
	try {
	    URL url3 = new URL("http://cdn.bulbagarden.net/upload/7/75/434Stunky.png");
		img3 = ImageIO.read(url2);
	} catch (IOException e) {	
	}
	
	Question question0 = new Question("What is a moonstone?", "I found one in Mount Moon and I don't know what it does. Help?", 1, null, "Clefable1", Date(), 1, null, null);
	Question question1 = new Question("How do I trade myself?", "I want to grow up to be a Gengar. But all my friends are Ghastlies.", 1, null, "TehHaunter", Date(), 2, null, null);
	Question question2 = new Question("If flash lights up dark caves, why does it make me lose accuracy when I can see better in the dark?", "See title.", 1, null, "DatVoltorb", Date(), 3, flash, null);
	
	Answer answer0 = new Answer("Moon stones are pieces of swiss that fell to Earth from the moon. They are delicious, with a mild taste", 1, null, "PranksterMankey", Date(), 1, null);
	Answer answer1 = new Answer("They are junk that take up room in your bag! >:(", 1, null, "Hiker'sGeo", Date(), 2, null);
	Answer answer2 = new Answer("With a link cable! /s", 1, null, 'AbraKadabra', Date(), 3, null);
	Answer answer3 = new Answer("'cuz the light is to bright!", 1, null, 'IFullZubat, Date()', 4, null);

	ArrayList<Answer> nullAnswerList;
	nullAnswerList.add(answer0);
	nullAnswerList.add(answer1);
	
	ArrayList<Answer> singleimageAnswerList;
	ArrayList<Answer> multipleAnswerList;
	Arraylist<Answer> flash;
	flash.add(answer3);
	
	// Test individual objects for no image cases
	void testEmptyImageQuestion() {
		AssertTrue(question0.getImage() == null);
	}
	
	void testEmptyImageAnswer() {
		AssertTrue(answer0.getImage() == null);
	}
	
	// Test individual objects for adding images;
	void testAddImageQuestion() {
		AssertTrue(question2.getImage() == null);
		question2.setImage(img1);
		AssertTrue(question2.getImage() == img1);
	}
	
	void testAddImageAnswer() {
		AssertTrue(answer2.getImage() == null);
		answer2.setImage(img2);
		AssertTrue(answer2.getImage() == img2);
	}
	
	singleimageAnswerList.add(answer2);
	
	void testNoQImage1AImage () {
		// Question has no image. Answer has image.
		question1.setAnswer(singleimageAnswerList);
		AssertSame("Question1 in NoQ1A test is not null", question1.getImage(), null);
		AssertSame("answer2 in NoQ1A test is null", question1.getAnswer()[0].getImage(), img2)	
	}
	
	
	void test1QImageNoAImage() {
		// Question has image. Answer has no image.
		AssertSame("Q2 has image in 1QNoA",question2.getImage(), img1);
		AssertSame("A3 has no image in Q2 in 1QNoA", question2.getAnswer()[0].getImage(), null)
	}
	
	void testNoQImageNoAImage() {
		question0.setAnswer(nullAnswerList);
		// Question has no image. Answer has no image.
		AssertSame("Q0 has no image in NoQNoA",question0.getImage(), null);
		AssertSame("A0 has no image in Q0 in NoQNoA", question0.getAnswer()[0].getImage(), null);
		AssertSame("A1 has no image in Q0 in NoQNoA", question0.getAnswer()[1].getImage(), null);
	}
	
	void test1QImage1AImage() {
		// Question has an image. Answer has an image.
		question1.setImage(img3);
		AssertSame("A2 has no image in Q1 in 1Q1A", question1.getAnswer()[0].getImage(), img2);
		AssertSame("A2 has no image in Q1 in 1Q1A", question1.getImage(), img3);
	}
	
	

}
*/