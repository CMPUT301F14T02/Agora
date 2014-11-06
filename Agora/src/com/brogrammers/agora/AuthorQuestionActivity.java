package com.brogrammers.agora;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AuthorQuestionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_author_question);

		Button addQuestion= (Button)findViewById(R.id.authorQuestionAddQuestionButton);
		Button addPicture = (Button)findViewById(R.id.authorQuestionAddPictureButton);

		addQuestion.setOnClickListener(questionhandler);
		addPicture.setOnClickListener(picturehandler);

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.author_question, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	View.OnClickListener questionhandler = new View.OnClickListener() {
		public void onClick(View v) {
			// add question
			EditText titleText = (EditText)findViewById(R.id.authorQuestionEditText);
			EditText bodyText = (EditText)findViewById(R.id.authorQuestionBodyEditText);
    		Toast.makeText(Agora.getContext(), "Adding Question!", Toast.LENGTH_SHORT).show();
			
    		String title = titleText.getText().toString();
    		String body = bodyText.getText().toString();
//    		Toast.makeText(Agora.getContext(), "Title: "+title, Toast.LENGTH_SHORT).show();
//    		Toast.makeText(Agora.getContext(), "Body: "+body, Toast.LENGTH_SHORT).show();
    		
    		// replace null with image when images are implemented
    		QuestionController.getController().addQuestion(title, body, null); 
    		finish();
    		
		}
	};
	View.OnClickListener picturehandler = new View.OnClickListener() {
		public void onClick(View v) {
			// add picture
    		Toast.makeText(Agora.getContext(), "Adding Picture! (Not Yet Implemented)", Toast.LENGTH_SHORT).show();

		}
	};

}
