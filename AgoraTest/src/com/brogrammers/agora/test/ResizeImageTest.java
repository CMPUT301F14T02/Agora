package com.brogrammers.agora.test;

import junit.framework.TestCase;

public class ResizeImageTest extends TestCase {
	WebserviceModel model = WebserviceModel.getModel();
	QuestionController controller = QuestionController.getController();
	ImageResizer resizer = new ImageResizer();
	
	Image img1 = null;
	try {
		URL url = new URL("http://cdn.bulbagarden.net/upload/2/25/Bulbapedia_logo.png");
		img1 = ImageIO.read(url);
	} catch (IOException e) { }
	
	Long qid = controller.addQuestion("Is Encore blocked by Substitute?", 
			       "My Volbeat used Encore on a Mamoswine behind a Substitute, but it failed! Why?"
				   img1);
	
	assertTrue("Image is not <64kb", model.getQuestionById(qid).getImage().getSize() < (64*1024));
	
}
