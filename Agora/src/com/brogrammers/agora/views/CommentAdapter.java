package com.brogrammers.agora.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.R;
import com.brogrammers.agora.R.id;
import com.brogrammers.agora.R.layout;
import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.Comment;
import com.brogrammers.agora.model.Question;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * Adapter for populating and formatting comments onto comment view 
 * @author Group02
 *
 */

public class CommentAdapter extends BaseAdapter {
	private Answer answer;
	private LayoutInflater inflater;
	private Question question;
	
	/**
	 * Constructor for the adapter, there separate constructors for Answers and Questions as both can have comments
	 * @param a
	 */
	public CommentAdapter(Answer a){
		this.answer = a;
		this.question = null;
		this.inflater = (LayoutInflater)Agora.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
	}
	public CommentAdapter(Question q){
		this.answer = null;
		this.question = q;
		this.inflater = (LayoutInflater)Agora.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
	}
	
	
	
	/**
	 * method that returns the number of comments for answer/question
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(question == null){
			return answer.countComments();
		} else {
			return question.countComments();
		}
	}
	
	/**
	 * method that given the position/index of the comment in the answer/question it returns the comment in that position.
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(question == null){
			return answer.getComments().get(position);
		} else {
			return question.getComments().get(position);
		}
	}
	
	/**
	 * return ID of the comment, return 0 as comments do not have unique IDs
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Method of getting the view that will populate each item on the listview with comment_object
	 * 
	 */
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
		((TextView)convertView.findViewById(R.id.cAuthourDate)).setText("Submitted by: " +comment.getAuthor().getUsername()+", "+ datetostring(comment.getDate()));
	
		return convertView;
	}

	/**
	 * Converts time passed from Epoch time into a formatted date string.
	 * @param milliseconds		Input the java built in time as milliseconds
	 * @return newDate 				Output a date format that interprets time from milliseconds
	 */
	public String datetostring(long milliseconds){
	    Date date = new Date(); 
	    date.setTime(milliseconds);
	    String newDate=new SimpleDateFormat("MMM d yyyy").format(date);
	    return newDate;
	}
}
