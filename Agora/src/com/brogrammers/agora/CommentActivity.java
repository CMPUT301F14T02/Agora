package com.brogrammers.agora;

import java.util.List;

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

public class CommentActivity extends Activity implements Observer {
	private long qid;
	private List<Question> qList;
	private List<Comment> cList;
	private CommentAdapter cadapter;
	private QuestionController controller;

	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

		Button postComment = (Button) findViewById(R.id.CommentPostButton);
		postComment.setOnClickListener(postcomment);

		// Question q = new Question("New Thunderwave Answer",null,null, new
		// Author("mudkip"));
		// q.addComment(new Comment("adfsfsafsdfsdfsdfffffffffffffffffs"));

		qid = getIntent().getLongExtra("qid", 0L);
		// Toast.makeText(Agora.getContext(), Long.toString(qid),
		// Toast.LENGTH_SHORT).show();

		controller = QuestionController.getController();
		controller.setObserver(this);
		qList = controller.getQuestionById(qid);

		lv = (ListView) findViewById(R.id.CommentListView);
		if (qList.size() > 0) {
			try {
				cadapter = new CommentAdapter(qList.get(0));
				lv.setAdapter(cadapter);
				//Toast.makeText(this, " Set Comment Adapter", 0).show();
			} catch (NullPointerException e) {
				Toast.makeText(this,
						"CommentActivity Nullptr in setting adapter", 0).show();
			}
		} else {
			Toast.makeText(this, "qList empty onCreate", 0).show();
		}

	}

	@Override
	public void update() {
		if (qList.size() > 0) {
			try {
				cadapter = new CommentAdapter(qList.get(0));
				lv.setAdapter(cadapter);
				//Toast.makeText(this, " Set Comment Adapter", 0).show();
				cadapter.notifyDataSetChanged();
			} catch (NullPointerException e) {
				Toast.makeText(this,
						"CommentActivity Nullptr in setting adapter", 0).show();
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

	View.OnClickListener postcomment = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText commentBody = (EditText) findViewById(R.id.CommentEditText);
    		String body = commentBody.getText().toString();

			//Toast.makeText(Agora.getContext(), commentBody.getText().toString(), Toast.LENGTH_SHORT).show();
			try {
				controller.addComment(body, qid, null);
			} catch (NullPointerException e) {
				Toast.makeText(Agora.getContext(),
						"CommentActivity Nullptr in adding comment", 0).show();
			}
		}
	};

}