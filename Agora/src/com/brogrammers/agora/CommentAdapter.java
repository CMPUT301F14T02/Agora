package com.brogrammers.agora;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	private Answer answer;
	private LayoutInflater inflater;
	private Question question;
	
	CommentAdapter(Answer a){
		this.answer = a;
		this.question = null;
		this.inflater = (LayoutInflater)Agora.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
	}
	
	CommentAdapter(Question q){
		this.answer = null;
		this.question = null;
		this.inflater = (LayoutInflater)Agora.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(question == null){
			return answer.countComments();
		} else {
			return question.countComments();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(question == null){
			return answer.getComments().get(position);
		} else {
			return answer.getComments().get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return (Long) null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = inflater.inflate(R.layout.comment_object, null);
		}
		
		//inflate each listview item with "comment_object"
		Comment comment = (Comment)getItem(position);
		//set text on each TextView			
		((TextView)convertView.findViewById(R.id.cBody)).setText(comment.getBody());
		((TextView)convertView.findViewById(R.id.cAuthourDate)).setText(comment.getDate().toString());
		
		return convertView;
	}

}
