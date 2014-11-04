package com.brogrammers.agora;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class QuestionAdapter extends BaseAdapter{
	private Activity activity; // ???
	private LayoutInflater inflater;
	private QuestionController quest_controller;
	
	public QuestionAdapter(Activity activity) {
		this.activity = activity;
		this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		// Need to pull from the controller the list of questions for the adapter
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_main, null);
		}
		
		return null;
	}
	

}
