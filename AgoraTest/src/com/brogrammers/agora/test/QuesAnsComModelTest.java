package com.brogrammers.agora.test;

import java.util.Date;
import java.util.List;

import com.brogrammers.agora.Answer;
import com.brogrammers.agora.Author;
import com.brogrammers.agora.Comment;
import com.brogrammers.agora.Question;

import junit.framework.TestCase;

public class QuesAnsComModelTest extends TestCase {
	// -------------------------------------Question Model Tests--------------------------------------------
	// testing constructor - getTitle,getBody, and getAuthor, upvote
	public void testQuestionConstructor() {
		String title = "Where is the game corner?";
		String body = "I want to redeem some points for a nugget. Is it in goldenrod city?";
		Author author = new Author("Kevin");
		Question testQuestion = new Question(title, body, null, author);
		assertTrue("Title not correct",
				testQuestion.getTitle() == "Where is the game corner?");
		assertTrue("Body not correct",
				testQuestion.getBody() == "I want to redeem some points for a nugget. Is it in goldenrod city?");
		assertTrue("Author not correct", testQuestion.getAuthor() == author);
		assertTrue("Image not correct", testQuestion.getImage() == null);
	}
	//testing add Answer to a question
	public void testQuestionAddAnswer() {
		String title = "Where is the game corner?";
		String body = "I want to redeem some points for a nugget. Is it in goldenrod city?";
		Author author = new Author("Kevin");

		Answer testAnswer1 = new Answer("It's near the railroad.", null,
				new Author("Whitney"));
		Answer testAnswer2 = new Answer(
				"Be careful to not lose all your money.", null, new Author(
						"Old guy"));

		Question testQuestion = new Question(title, body, null, author);
		// Make sure list is 0
		List<Answer> answers = testQuestion.getAnswers();
		assertTrue("Answer list not empty", answers.size() == 0);

		testQuestion.addAnswer(testAnswer1);
		answers = testQuestion.getAnswers();
		assertTrue("Answer list not size 1", answers.size() == 1);
		
		testQuestion.addAnswer(testAnswer2);
		assertTrue("Answer is not the created answer.", answers.get(0) == testAnswer1);
		answers = testQuestion.getAnswers();
		assertTrue("Answer list not size 2", answers.size() == 2);
		assertTrue("Number of answers not 2", testQuestion.countAnswers() == 2);

	}
	//testing add comments to a question
	public void testQuestionAddComment(){
		String title = "Where is the game corner?";
		String body = "I want to redeem some points for a nugget. Is it in goldenrod city?";
		Author author = new Author("Kevin");
		Question testQuestion = new Question(title, body, null, author);
		//Checking if list is 0
		List<Comment> comments = testQuestion.getComments();
		assertTrue("Comment list is not 0", comments.size() == 0);
		assertTrue("Comment count is not 0", testQuestion.countComments() == 0);
		
		Comment testComment1 = new Comment("I hope someone can help with this too. I can't find it either");
		Comment testComment2 = new Comment("What a dumb question. Use a map.");
		testQuestion.addComment(testComment1);
		comments = testQuestion.getComments();
		assertTrue("Comment list not size 1", comments.size() == 1);
		assertTrue("Comment added is not the same as the comment created", comments.get(0)==testComment1);
		
		testQuestion.addComment(testComment2);
		assertTrue("Comment list not size 2", comments.size() == 2);
		assertTrue("Comment count is not 2", testQuestion.countComments() == 2);
		
	}
	
	//testing upvote question
	public void testQuestionUpvote() {
		String title = "Where is the game corner?";
		String body = "I want to redeem some points for a nugget. Is it in goldenrod city?";
		Author author = new Author("Kevin");
		Question testQuestion = new Question(title, body, null, author);
		testQuestion.upvote();
		assertTrue("Question not upvoted", testQuestion.getRating() == 1);
		for (int i = 0; i < 99; ++i) {
			testQuestion.upvote();
		}
		assertTrue("Question not upvoted 100 times",
				testQuestion.getRating() == 100);
	}
	//---------------------------Answer Model Tests--------------------------
	public void testAnswer(){
		String body = "The answer is 42.";
		Author testUser = new Author("Randy");
		Answer testAnswer = new Answer(body, null, testUser);
		assertTrue("Answer author incorrect", testAnswer.getAuthor() == testUser);
		assertTrue("Answer body not correct", testAnswer.getBody() == body);
		assertTrue("Answer iamge is not null", testAnswer.getImage() == null);
	}
	
	public void testAnswerAddComment(){
		String body = "The answer is 42.";
		Author testUser = new Author("Randy");
		Answer testAnswer = new Answer(body, null, testUser);
		List<Comment> comments = testAnswer.getComments();
		assertTrue("Answer comment list not 0", comments.size() == 0);
		assertTrue("Answer comment count not 0", testAnswer.countComments() == 0);
		
		Comment testComment1 = new Comment("Tru dat", new Author("Kyle"));
		testAnswer.addComment(testComment1);
		comments = testAnswer.getComments();
		assertTrue("testAnswer comment list does not have 1 comment", comments.size() == 1);
		assertTrue("Comment added to testAnswer is wrong comment", comments.get(0) == testComment1);
		assertTrue("testAnswer count comment not 1", testAnswer.countComments() == 1 );
	}
	
	public void testAnswerUpvote(){
		String body = "The answer is 42.";
		Author testUser = new Author("Randy");
		Answer testAnswer = new Answer(body, null, testUser);
		
		testAnswer.upvote();
		assertTrue("testAnswer not upvoted once", testAnswer.getRating() == 1);
		
		for(int i = 0; i<99; ++i){
			testAnswer.upvote();
		}		
		assertTrue("testAnswer not upvoted hundred times", testAnswer.getRating() == 100);

	}
	
	//--------------------------Comment model test-------------------------------
	
	public void testComment(){
		String body = "I like this answer.";
		Author testAuthor = new Author("Nurse Joy");
		Comment testComment = new Comment(body, testAuthor);
		
		assertTrue("Comment has incorrect body", testComment.getBody()==body);
		assertTrue("Comment has incorrect author", testComment.getAuthor() == testAuthor);
	}
	
}
