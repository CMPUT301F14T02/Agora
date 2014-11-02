package com.brogrammers.agora;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class QuestionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);

		Button viewComment = (Button)findViewById(R.id.QuestionCommentsButton);
		Button viewAnswer = (Button)findViewById(R.id.QuestionAnswersButton);
		
		viewComment.setOnClickListener(opencommentview);
		viewAnswer.setOnClickListener(openanswerview);

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
	switch (id){
		case R.id.action_settings:
			return true;
		case R.id.action_favorite:
			favorite();
			return true;
		case R.id.action_flag:
			flag();
			return true;
		case R.id.action_addanswer:
			openAddAnswerView();
			return true;	
		default:
			return super.onOptionsItemSelected(item);
	}
}

View.OnClickListener openanswerview = new View.OnClickListener() {	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(Agora.getContext(), AnswerActivity.class);
		startActivity(intent);
	}
};


View.OnClickListener opencommentview = new View.OnClickListener() {	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Agora.getContext(), CommentActivity.class);
		startActivity(intent);
	}
};

public void openAddAnswerView() {
	Intent intent = new Intent(Agora.getContext(), AuthorAnswerActivity.class);
	startActivity(intent);
	//Toast.makeText(Agora.getContext(), "Hook up Add a question here", Toast.LENGTH_SHORT).show();
}

//remove these later, made for button testing. actual function is implemented in controller.
public void upvote(){
	Toast.makeText(this, "upvote", Toast.LENGTH_SHORT).show();
}
public void favorite(){
	Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show();
}
public void flag(){
	Toast.makeText(this, "flag", Toast.LENGTH_SHORT).show();
}
public void settings(){
	Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
}


}
