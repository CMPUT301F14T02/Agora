/*package com.brogrammers.agora.test;

import junit.framework.TestCase;

public class DeviceRemembersQuestionsAskedTest extends TestCase {
	public void testDeviceRemembersQuestionsAsked() {
		QuestionController controller = QuestionController.getController();
		DeviceUser user = DeviceUser.getUser();
		Long qid1 = controller.addQuestion("Priority Moves",
				"What are the moves that will always go first? #GottaGoFast",
				null);
		Long qid2 = controller.addQuestion("Best Moves",
				"What are the moves that will good pokemon always have?",
				null);
		assertTrue("Authored questions are saved in DeviceUser.authoredQuestionIDs",
				user.authoredQuestionIDs().contains(qid1) &&
			    user.authoredQuestionIDs().contains(qid2) );
	}
}
*/