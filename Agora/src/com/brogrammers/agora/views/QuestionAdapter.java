package com.brogrammers.agora.views;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.R;
import com.brogrammers.agora.R.id;
import com.brogrammers.agora.R.layout;
import com.brogrammers.agora.data.DeviceUser;
import com.brogrammers.agora.data.QuestionController;
import com.brogrammers.agora.model.Question;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Question adapter is required to assemble the question object 
 * for the UI implementation. This is used to update list views
 * in order to browse questions (US 1)
 * 
 * @author Group02
 */
public class QuestionAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private QuestionController controller;
	private List<Question> qList;
	private Activity activity;
	
	// Adapter Constructor
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

	
	/**
	 * Sets the view with our assembled question object to display in our browse question activity.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.question_object, null);
		}
	
		Question question = (Question)getItem(position);
//		((TextView)convertView.findViewById(R.id.qobjectbody)).setText(question.getBody());
		((TextView)convertView.findViewById(R.id.qobjecttitle)).setText(question.getTitle());

		((TextView)convertView.findViewById(R.id.qAuthor)).setText("Submitted by: " +question.getAuthor().getUsername()+", "+ datetostring(question.getDate()));

		LinearLayout HLayout = (LinearLayout)convertView.findViewById(R.id.RatingHLayout);
		((TextView)HLayout.findViewById(R.id.qObjectScore)).setText(Integer.toString(question.getRating()));
		
		LinearLayout HLayoutAcount = (LinearLayout)convertView.findViewById(R.id.HLayoutAnswerCount);
		((TextView)HLayoutAcount.findViewById(R.id.qACountText)).setText(Integer.toString(question.countAnswers()));


		List<Long> favoritedQuestions = DeviceUser.getUser().getFavoritedQuestionIDs();
		
		if (favoritedQuestions.contains(question.getID())) {
			((ImageView)convertView.findViewById(R.id.qQuestionFavourite)).setImageResource(R.drawable.ic_action_rating_favoritepink);	
		} else {
			
			((ImageView)convertView.findViewById(R.id.qQuestionFavourite)).setImageResource(R.drawable.ic_action_unfavorite);	
		}
		
		List<Long> flaggedQuestions = DeviceUser.getUser().getCachedQuestionIDs();
		
		if (flaggedQuestions.contains(question.getID())) {
			((ImageView)convertView.findViewById(R.id.qQuestionFlag)).setImageResource(R.drawable.ic_action_flag);
		} else {
			((ImageView)convertView.findViewById(R.id.qQuestionFlag)).setImageResource(R.drawable.ic_actionunflag);
		}
	
		
		convertView.setOnClickListener(new QuestionOnClickListener(position));
		
		return convertView;
	}
	
	
	/**
	 * This converts the date from milliseconds to a date format that the user can interprete.
	 * 
	 * @param milliseconds		Input the java built in time as milliseconds
	 * @return newDate 				Output a date format that interprets time from milliseconds
	 */
	public String datetostring(long milliseconds){
	    Date date = new Date(); 
	    date.setTime(milliseconds);
	    String newDate=new SimpleDateFormat("MMM d yyyy").format(date);
	    return newDate;
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






