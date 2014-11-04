package com.brogrammers.agora;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class AnswerActivity extends Activity implements Observer{
	
	private QuestionController qController = QuestionController.getController();
	private AnswerAdapter aadapter;

	ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer);
		
		//comment button
		Button viewComment = (Button)findViewById(R.id.AnswerCommentsButton);
		viewComment.setOnClickListener(opencommentview);	
		

		// For testing:
		//Long qID = 0L; //TODO: get question ID out of Intent/Bundle
		
		//Question q = new Question("New Thunderwave", "Why is it OP?", null, new Author("Mudkip"));
		//Answer a = new Answer("New Thunderwave Answer",null,new Author("mudkip"));
		//q.addAnswer(a);
		qController.setObserver(this);
		Question q = qController.getQuestionById(-6488159365839201000L);
==
	
		lv = (ListView)findViewById(R.id.AnswerListView);
		try {
			aadapter = new AnswerAdapter(q);
			lv.setAdapter(aadapter);
		} catch (NullPointerException e) {
			Toast.makeText(this, "Did not get question from server", 0);	
		}
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.answer, menu);
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

	//open comment view
	View.OnClickListener opencommentview = new View.OnClickListener() {	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Agora.getContext(), CommentActivity.class);
			startActivity(intent);
		}
	};
		
	//remove these later, made for button testing. actual function is implemented in controller.
	public void upvote(){
		Toast.makeText(this, "upvote", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void update() {
		aadapter.notifyDataSetChanged();
		Toast.makeText(this, "updating answer activity", 0);
		
	}


}
