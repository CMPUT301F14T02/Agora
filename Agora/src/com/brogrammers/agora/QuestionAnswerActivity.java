package com.brogrammers.agora;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class QuestionAnswerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_question_answer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.question_answer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id){
			case R.id.action_settings:
				return true;
//			case R.id.action_upvote:
//				upvote();
//				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public void comment(){
		Toast.makeText(this, "Hook up add comment here", Toast.LENGTH_SHORT).show();
	}
	
	public void expandcomment(){
		Toast.makeText(this, "expanded comments", Toast.LENGTH_SHORT).show();
	}
	
	//remove later, implemented in controller.
	public void upvote(){
		Toast.makeText(this, "upvote", Toast.LENGTH_SHORT).show();
	}
}

