package com.brogrammers.agora;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.brogrammers.agora.data.QuestionController;
import com.brogrammers.agora.model.Question;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
/**
 * Answer Activity which displays the view of answers corresponding to a question.
 * Todo: Upvote, favourite, cache functionality.
 * @author Group02
 */
public class AnswerActivity extends Activity implements Observer{
	
	private QuestionController qController = QuestionController.getController();
	private List<Question> qList;
	private AnswerAdapter aadapter;
	
	ListView lv;
	/**
	 * onCreate method. Gets question id of corresponding question through an intent. Then calls answeradapter to display answers.
	 * Create listview from answer activity layout.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer);
		
		Intent intent = getIntent();
		Long qid = intent.getLongExtra("qid", 0L);
		if (qid.equals(0L)) { Toast.makeText(this, "Didn't recieve a qid in intent", 0).show(); finish(); }
		
		qController.setObserver(this);
		qList = qController.getQuestionById(qid);


		lv = (ListView)findViewById(R.id.AnswerListView);
		try {
			aadapter = new AnswerAdapter(null,this);
			lv.setAdapter(aadapter);
//			Toast.makeText(this," Set Answer Adapter", 0).show();
		} catch (NullPointerException e) {
			Toast.makeText(this, "AnswerActivity Nullptr in setting adapter", 0).show();	
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

	/* This goes in the AnswerAdapter */
//	//open comment view
//	View.OnClickListener opencommentview = new View.OnClickListener() {	
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			Intent intent = new Intent(Agora.getContext(), CommentActivity.class);
//			
//			startActivity(intent);
//		}
//	};
//		
	//remove these later, made for button testing. actual function is implemented in controller.
	public void upvote(){
		Toast.makeText(this, "upvote", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Update when new answer is added.
	 */
	@Override
	public void update() {
		if (qList.size() == 0) {
			Toast.makeText(this, "AnswerActivity update() called, but qList is empty", 0);
		} else {
			aadapter.setQuestion(qList.get(0));
//		aadapter.notifyDataSetChanged();
//			Toast.makeText(this, "AnswerActivity got update notification", 0).show();
		}
		
	}


}
