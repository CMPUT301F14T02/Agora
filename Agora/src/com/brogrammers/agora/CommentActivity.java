package com.brogrammers.agora;

import java.util.List;

import com.brogrammers.agora.data.QuestionController;
import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.Comment;
import com.brogrammers.agora.model.Question;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
/**
 * Comment activity for displaying a list of comments corresponding to a question/answer.
 * @author Group02
 *
 */
public class CommentActivity extends Activity implements Observer {
	private long qid;
	private long aid;
	private List<Question> qList;
	private List<Comment> cList;
	private CommentAdapter cadapter;
	private QuestionController controller;

	ListView lv;
	/**
	 * Retrieves question id or answer id from intent. 
	 * Then creates post comment button layout and the listview.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

		Button postComment = (Button) findViewById(R.id.CommentPostButton);
		postComment.setOnClickListener(postcomment);

		qid = getIntent().getLongExtra("qid", 0L);
		aid = getIntent().getLongExtra("aid", 0L);

		controller = QuestionController.getController();
		controller.setObserver(this);
		qList = controller.getQuestionById(qid);
		lv = (ListView) findViewById(R.id.CommentListView);

	}
	/**
	 * When updated, return answer/question and retrieve new set of comments.
	 */
	@Override
	public void update() {
		if (qList.size() > 0) {
			if (aid == 0L) {
		
				Question q = qList.get(0);
				cadapter = new CommentAdapter(q);
				try {
					lv.setAdapter(cadapter);
				} catch (NullPointerException e) {
					Toast.makeText(this,
							"CommentActivity Nullptr in setting adapter", 0).show();
				}
			
			} else { // we're in answer comments
				Question q = qList.get(0);
				Answer a = q.getAnswerByID(aid);
				cadapter = new CommentAdapter(a);
				try {
					lv.setAdapter(cadapter);
				} catch (NullPointerException e) {
					Toast.makeText(this,
							"CommentActivity Nullptr in setting adapter", 0).show();
				}
			}
		} else {
			Toast.makeText(this, "qList empty onUpdate", 0).show();
		}
			

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
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
	/**
	 * When postcomment button clicked on. Posts comment to via corresponding question/answer id.
	 */
	View.OnClickListener postcomment = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText commentBody = (EditText) findViewById(R.id.CommentEditText);
    		String body = commentBody.getText().toString();

			//
			if (aid == 0) { // adding comment to question 
				try {
					controller.addComment(body, qid, null);
					Toast.makeText(Agora.getContext(), "Comment added!", 0).show();
				} catch (NullPointerException e) {
					Toast.makeText(Agora.getContext(),
							"CommentActivity Nullptr in adding comment", 0).show();
				}
			} else { // adding comment to answer
				try {
					controller.addComment(body, qid, aid);
					Toast.makeText(Agora.getContext(), "Comment added!", 0).show();
				} catch (NullPointerException e) {
					Toast.makeText(Agora.getContext(),
							"CommentActivity Nullptr in adding comment", 0).show();
				}
			}
    	 
			finish();
		}
	};

}