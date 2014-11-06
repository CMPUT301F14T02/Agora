package com.brogrammers.agora;

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

public class CommentActivity extends Activity {

	
	ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		Button postComment = (Button)findViewById(R.id.CommentPostButton);
		postComment.setOnClickListener(postcomment);
		
		Answer a = new Answer("New Thunderwave Answer",null,new Author("mudkip"));
		a.addComment(new Comment("adfsfsafs"));
		
		lv = (ListView)findViewById(R.id.CommentListView);
		try {
			CommentAdapter cadapter = new CommentAdapter(a);
			lv.setAdapter(cadapter);
			Toast.makeText(this," Set Comment Adapter", 0).show();
		} catch (NullPointerException e) {
			Toast.makeText(this, "CommentActivity Nullptr in setting adapter", 0).show();	
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
			EditText commentBody = (EditText)findViewById(R.id.CommentEditText);
			Toast.makeText(Agora.getContext(), commentBody.getText().toString(), Toast.LENGTH_SHORT).show();
		}
	};
}
