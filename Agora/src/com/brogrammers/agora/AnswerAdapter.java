package com.brogrammers.agora;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AnswerAdapter extends BaseAdapter {
	private Question question;
	private LayoutInflater inflater;

	AnswerAdapter(Question q){
		this.question = q;
		this.inflater = (LayoutInflater)Agora.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return question.countAnswers();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return question.getAnswers().get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return question.getAnswers().get(position).getID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = inflater.inflate(R.layout.answer_object, null);
		}
		
		//inflate each listview item with "answer_object"
		Answer answer = (Answer)getItem(position);
		//set text on each TextView			
		((TextView)convertView.findViewById(R.id.aBody)).setText(answer.getBody());
		((TextView)convertView.findViewById(R.id.aScore)).setText(Integer.toString(answer.getRating()));
		((TextView)convertView.findViewById(R.id.aAuthourDate)).setText(answer.getDate().toString());
		
		return convertView;
	}

}
