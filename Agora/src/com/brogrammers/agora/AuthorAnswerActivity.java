package com.brogrammers.agora;

import java.io.UnsupportedEncodingException;

import com.brogrammers.agora.data.QuestionController;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Activity screen when posting an answer to a question. Contains a textbox for the body of an answer.
 * Todo: implement picturehandler to post pictures.
 * @author Group02
 *
 */
public class AuthorAnswerActivity extends Activity {
	private Long qid;
	/**
	 * Todo: implement picturehandler to post pictures.
	 * 
	 * Retrieves question id from intent. 
	 * Retrieves button layouts and activity layout.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_author_answer);
		Button addAnswer = (Button)findViewById(R.id.authorAnswerAddAnswerButton);
		Button addPicture = (Button)findViewById(R.id.authorAnswerAddPictureButton);

		Intent intent = getIntent();
		qid = intent.getLongExtra("qid", 0L);
		if (qid.equals(0L)) { Toast.makeText(this, "Didn't recieve a qid in intent", 0).show(); finish(); }		
		addAnswer.setOnClickListener(answerhandler);
		addPicture.setOnClickListener(picturehandler);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.author_answer, menu);
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

	View.OnClickListener answerhandler = new View.OnClickListener() {
		public void onClick(View v) {
			// add question
			EditText bodyText = (EditText)findViewById(R.id.authorAnswerBodyEditText);
    		Toast.makeText(Agora.getContext(), "Adding Answer!", Toast.LENGTH_SHORT).show();
    		String body = bodyText.getText().toString();
//    		Toast.makeText(Agora.getContext(), "Body: "+body, Toast.LENGTH_SHORT).show();
    		try {
				QuestionController.getController().addAnswer(body, null, qid);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
