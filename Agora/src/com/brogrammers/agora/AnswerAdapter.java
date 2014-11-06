package com.brogrammers.agora;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AnswerAdapter extends BaseAdapter {
	private Question question;
	private LayoutInflater inflater;
	private List<Question> qList;
	private Activity activity;
	
	AnswerAdapter(Question q, Activity a){
		this.question = q;
		this.activity = a;
//		if (q.size() > 0) {
//			this.question = q.get(0);
//		}
		this.inflater = (LayoutInflater)Agora.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
	}

	@Override
	public int getCount() {
		if (question != null) {
			return question.countAnswers();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (question != null) {
			return question.getAnswers().get(position);
		} else {
			return 0;
		}
	}

	@Override
	public long getItemId(int position) {
		if (question != null) {
			return question.getAnswers().get(position).getID();
		} else {
			return 0;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.answer_object, parent, false);
		}
		
		//inflate each listview item with "answer_object"
		Answer answer = (Answer)getItem(position);
		Button comment = (Button) convertView.findViewById(R.id.aComment);
		ImageButton upvote = (ImageButton) convertView.findViewById(R.id.aUpvote);
		
		
		//comment.setOnClickListener(answercomments);
		comment.setOnClickListener(new CommentOnClickListener(position));
		
		//upvote.setOnClickListener(answerupvote);
		upvote.setOnClickListener(new UpVoteOnClickListener(position));
		
		
		//set text on each TextView			
		((TextView)convertView.findViewById(R.id.aBody)).setText(answer.getBody());
		((TextView)convertView.findViewById(R.id.aScore)).setText(Integer.toString(answer.getRating()));
		((TextView)convertView.findViewById(R.id.aAuthourDate)).setText(answer.getDate().toString());
		
		return convertView;
	}

	
	View.OnClickListener answercomments  = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Agora.getContext(), CommentActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Agora.getContext().startActivity(intent);
		}
	};
	
	View.OnClickListener answerupvote =  new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Toast.makeText(Agora.getContext(), "call upvote" , Toast.LENGTH_SHORT).show();
		}
	};
	
	public void setQuestion(Question q) {
		this.question = q;
		notifyDataSetChanged();
	}


	private class CommentOnClickListener implements OnClickListener {
		private int position;
		CommentOnClickListener(int position) {
			this.position = position;
		}
		public void onClick(View view) {
			Long aid = getItemId(position);
			Intent intent = new Intent(activity, CommentActivity.class);
			intent.putExtra("aid", aid);
			activity.startActivity(intent);
		}
	}
	
	private class UpVoteOnClickListener implements OnClickListener {
		private int position;
		UpVoteOnClickListener(int position) {
			this.position = position;
		}
		public void onClick(View view) {
			Long aid = getItemId(position);
			question.getAnswerByID(aid).upvote();
			Toast.makeText(Agora.getContext(), Integer.toString(question.getAnswerByID(aid).getRating()), Toast.LENGTH_SHORT).show();
			//update the rating textview afterwards
		}
	}
	
}

	



