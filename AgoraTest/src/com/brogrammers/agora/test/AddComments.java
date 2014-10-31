/*package com.brogrammers.agora.test;

import com.brogrammers.agora.DeviceUser;
import com.brogrammers.agora.WebServiceModel;

import junit.framework.TestCase;

public class AddComments extends TestCase {
	// These are singletons
	DeviceUser user = new DeviceUser();
	WebServiceModel webServiceModel = new WebServiceModel;
	LocalCacheModel localCacheModel = new LocalCacheModel;
	QuestionController controller = new QuestionController();
	
	String questionTitle = "Scald vs Surf. Which one?";
	String questionBody = "Water with burh too OP? Or Power Level of SURF?";
	String answerBody = "Neither. Use Hydro Pump";
	String comment1 = "I liek Rain Dance";
	
	controller.addQuestion(questionTitle, questionBody, null);
	
	Question q1 = webServiceModel.getQuestions()[0];
	long q1id = q1.getUniqueID();
	controller.addAnswer(answerBody, null, q1id);
	Answer a1 = q1.getAnswers()[0];
	long a1id = a1.getUniqueID();
	
	Question q1c = localCacheModel.getQuestions()[0];
	long q1cid = q1c.getUniqueID();
	Answer a1c = q1c.getAnswers()[0];
	long a1cid = a1c.getUniqueID();
	
	void testAddCommentToQ() { 
		controller.addComment(comment1, q1id);
		assertSame("Q1 comment did not get added in web model", q1.getComments()[0].getBody(), comment1);
		assertSame("Q1 comment did not get added in cache model", q1c.getComments()[0].getBody(), comment1);
	}
	
	void testAddCommentToA() {
		controller.addComment(comment1, a1id);
		assertSame("A1 comment did not get added in web model", a1.getComments()[0].getBody(), comment1);
		assertSame("A1 comment did not get added in cache model", a1c.getComments()[0].getBody(), comment1);
		assertSame("Q1 comment got destroyed by adding A1 to Web Model", q1.getComments()[0].getBody(), comment1);
		assertSame("Q1 comment got destroyed by adding A1 to Cache", q1c.getComments()[0].getBody(), comment1);
	}
	
	
	
}

}
*/