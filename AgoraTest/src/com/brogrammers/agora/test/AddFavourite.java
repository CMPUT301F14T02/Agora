//package com.brogrammers.agora.test;
//
//import junit.framework.TestCase;
//
//public class AddFavourite extends TestCase {
//	// These are singletons
//	User user = new User();
//	WebServiceModel webServiceModel = new WebServiceModel;
//	LocalCacheModel localCacheModel = new LocalCacheModel;
//	QuestionController controller = new QuestionController();
//	
//	String questionTitle = "Scald vs Surf. Which one?";
//	String questionBody = "Water with burh too OP? Or Power Level of SURF?";
//	String answerBody = "Neither. Use Hydro Pump";
//	String comment1 = "I liek Rain Dance";
//	
//	controller.addQuestion(questionTitle, questionBody, null);
//	
//	Question q1 = webServiceModel.getQuestions()[0];
//	long q1id = q1.getUniqueID();
//	controller.addAnswer(answerBody, null, q1id);
//	Answer a1 = q1.getAnswers()[0];
//	long a1id = a1.getUniqueID();
//	
//	controller.addComment(comment1, q1id);
//
//	void testAddFavourite() {
//		controller.addFavourite(q1)
//		AssertSame("AddFavourite saves correct qid in user", user.getFavouritedQuestionIDs.contains(q1), true);
//		Question q1c = localCacheModel.getQuestions()[0];
//		long q1cid = q1c.getUniqueID();		
//		AssertSame("Favourited question is added to cache.", q1cid, q1id);
//		
//	}
//
//
//}
