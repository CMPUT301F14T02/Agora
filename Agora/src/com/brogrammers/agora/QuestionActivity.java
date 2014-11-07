package com.brogrammers.agora;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.brogrammers.agora.data.CacheDataManager;
import com.brogrammers.agora.data.QuestionController;
import com.brogrammers.agora.model.Question;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

/**
 * Question activity displaying view of a single question.
 * Todo: implement answer count
 * @author Group 02
 *
 */
public class QuestionActivity extends Activity implements Observer {
	private List<Question> qList;
	private Long qid;
	/**
	 * When activity is created, retrieve question id via intent from main activity.
	 * Sets buttons, and onclick listeners for comments/answers.
	 */
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
		ImageView upVoteQuestion= (ImageView)findViewById(R.id.QuestionUpVoteButton);
		
		
		viewComment.setOnClickListener(new openCommentsView());
		viewAnswer.setOnClickListener(new openAnswerView());
		upVoteQuestion.setOnClickListener (new upVoteQuestion());
	}
	/**
	 * helper function to convert date in ms to a date format MMM d yyyy
	 * @param milliseconds date in milliseconds
	 * @return string of formatted date
	 */
	public String datetostring(long milliseconds){
	    Date date = new Date(); 
	    date.setTime(milliseconds);
	    String newDate=new SimpleDateFormat("MMM d yyyy").format(date);
	    return newDate;
	}
	/**
	 * Called when question list is changed. check questionlist for question retrieved via id, and set the textviews accordingly.
	 */
	@Override
	public void update() {
		TextView qTitle = (TextView)findViewById(R.id.qTitle);
		TextView qBody = (TextView)findViewById(R.id.qBody);
		TextView qScore= (TextView)findViewById(R.id.qScore);
		TextView authordate = (TextView)findViewById(R.id.AuthourDate);
		String authorline = "Submitted by: ";

		
		if (qList.size() > 0) {
			Question q = qList.get(0);
			authorline += q.getAuthor().getUsername()+", "+ datetostring(q.getDate());
			authordate.setText(authorline);
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
	/**
	 * Action bar that contains button for settings, favouriting, flagging, and adding an answer.
	 */
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
	/**
	 * Opens activity for the answers corresponding to the question and puts question id in intent
	 * @author group02
	 *
	 */
	private class openAnswerView implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(QuestionActivity.this, AnswerActivity.class);
			intent.putExtra("qid", qid);
			startActivity(intent);
		}
	}
	/**
	 * Opens activity for the comments corresponding to the question and puts question id in intent
	 * @author group02
	 *
	 */
	private class openCommentsView implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(QuestionActivity.this, CommentActivity.class);
			intent.putExtra("qid", qid);
			startActivity(intent);
		}
	}
	/**
	 * upvote question when upvote button is clicked on.
	 * @author Kevin
	 *
	 */
	private class upVoteQuestion implements OnClickListener {
		public void onClick(View v) {
			if(qList.size() != 0){
				//increments votes in the view, but does not save.
				QuestionController controller = QuestionController.getController(); 
				Question q = qList.get(0);
				try {
					controller.upvote(q.getID(),null);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	/**
	 * Opens AuthorAnswer activity to post an answer to the question. Sends question id via intent.
	 */
	public void openAddAnswerView() {
		Intent intent = new Intent(Agora.getContext(), AuthorAnswerActivity.class);
		intent.putExtra("qid", qid);
		startActivity(intent);
		//Toast.makeText(Agora.getContext(), "Hook up Add a question here", Toast.LENGTH_SHORT).show();
	}


}
