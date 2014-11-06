package com.brogrammers.agora;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private QuestionController controller;
	private List<Question> qList;
	private Activity activity;
	
	public QuestionAdapter(List<Question> qList, Activity activity) {
		this.inflater = (LayoutInflater)Agora.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// Need to pull from the controller the list of questions for the adapter
//		this.controller = QuestionController.getController();
		this.qList = qList;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return qList.size();
	}

	@Override
	public Object getItem(int position) {
		return qList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return qList.get(position).getID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.question_object, null);
		}
		
		final Question question = (Question)getItem(position);
		
		// Set the text fields for Question Object
		((TextView)convertView.findViewById(R.id.qobjectbody)).setText(question.getBody());
		((TextView)convertView.findViewById(R.id.qobjecttitle)).setText(question.getTitle());
		((TextView)convertView.findViewById(R.id.qObjectScore)).setText(Integer.toString(question.getRating()));
		
		
		// Set button for Question Object
		ImageButton upvote = (ImageButton) convertView.findViewById(R.id.qUpvoteButton);
		upvote.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO: Implement upvote Button
				Toast.makeText(Agora.getContext(), "PRESSBUTTON", 0).show();
				question.upvote();
			}
		});
		
		
		/*List<Long> favoritedQuestions = DeviceUser.getUser().getFavoritedQuestionIDs();
		
		if (favoritedQuestions.contains(question.getID())) {
			((ImageButton)convertView.findViewById(R.id.qObjectFavourite)).setImageResource(R.drawable.ic_action_rating_favoritepink);	
		} else {
			// TODO: change to grey
			((ImageButton)convertView.findViewById(R.id.qObjectFavourite)).setImageResource(R.drawable.ic_action_rating_favoritepink);	
		}
		*/
		
		convertView.setOnClickListener(new QuestionOnClickListener(position));
		
		return convertView;
	}
	
	private class QuestionOnClickListener implements OnClickListener {
		private int position;
		QuestionOnClickListener(int position) {
			this.position = position;
		}
		public void onClick(View view) {
			Long qid = getItemId(position);
			Intent intent = new Intent(activity, QuestionActivity.class);
			intent.putExtra("qid", qid);
			activity.startActivity(intent);
		}
	}
}






