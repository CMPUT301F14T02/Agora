package com.brogrammers.agora;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;


public class QuestionActivity extends Activity implements Observer {
	private List<Question> qList;
	private Long qid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		
		Intent intent = getIntent();
		qid = intent.getLongExtra("qid", 0L);
		if (qid.equals(0L)) { Toast.makeText(this, "Didn't recieve a qid in intent", 0).show(); finish(); }
		
		QuestionController controller = QuestionController.getController(); 
		controller.setObserver(this);
		qList = controller.getQuestionById(qid);

		Button viewComment = (Button)findViewById(R.id.QuestionCommentsButton);
		Button viewAnswer = (Button)findViewById(R.id.QuestionAnswersButton);
		ImageButton upVoteQuestion= (ImageButton)findViewById(R.id.QuestionUpVoteButton);
		
		
		viewComment.setOnClickListener(new openCommentsView());
		viewAnswer.setOnClickListener(new openAnswerView());
		upVoteQuestion.setOnClickListener (new upVoteQuestion());
	}
	
	@Override
	public void update() {
		TextView qTitle = (TextView)findViewById(R.id.qTitle);
		TextView qBody = (TextView)findViewById(R.id.qBody);
		TextView qScore= (TextView)findViewById(R.id.qScore);
		if (qList.size() > 0) {
			Question q = qList.get(0);
			qTitle.setText(q.getTitle());
			qBody.setText(q.getBody());
			qScore.setText(Integer.toString(q.getRating()));
			CacheDataManager.getInstance().pushQuestion(q);
		} else {
			Toast.makeText(this, "QuestionActivity recieved empty list on update", 0).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	switch (id) {
		case R.id.action_settings:
			return true;
		case R.id.action_favorite:
//			favorite();
			return true;
		case R.id.action_flag:
//			flag();
			return true;
		case R.id.action_addanswer:
			openAddAnswerView();
			return true;	
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private class openAnswerView implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(QuestionActivity.this, AnswerActivity.class);
			intent.putExtra("qid", qid);
			startActivity(intent);
		}
	}
	
	private class openCommentsView implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(QuestionActivity.this, CommentActivity.class);
			intent.putExtra("qid", qid);
			startActivity(intent);
		}
	}
	
	private class upVoteQuestion implements OnClickListener {
		public void onClick(View v) {
			if(qList.size() != 0){
				Question q = qList.get(0);
				q.upvote();
				TextView qScore= (TextView)findViewById(R.id.qScore);
				qScore.setText(Integer.toString(q.getRating()));
			}
		}
	}


	
//	
//	View.OnClickListener opencommentview = new View.OnClickListener() {	
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			Intent intent = new Intent(Agora.getContext(), CommentActivity.class);
//			startActivity(intent);
//		}
//	};
//	
	public void openAddAnswerView() {
		Intent intent = new Intent(Agora.getContext(), AuthorAnswerActivity.class);
		intent.putExtra("qid", qid);
		startActivity(intent);
		//Toast.makeText(Agora.getContext(), "Hook up Add a question here", Toast.LENGTH_SHORT).show();
	}


}
