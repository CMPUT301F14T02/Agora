package com.brogrammers.agora;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;

import com.brogrammers.agora.data.QuestionController;
import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.Question;
/**
 * Adapter required to format answers from a question to the answerActivity of the question.
 * @author Group02

 */
public class AnswerAdapter extends BaseAdapter {
	private Question question;
	private LayoutInflater inflater;
	private List<Question> qList;
	private Activity activity;
	private DateFormat df = new SimpleDateFormat ("dd/MM/yy HH:mm:ss",Locale.getDefault());
	
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
//			Toast.makeText(Agora.getContext(), "AnswerAdapter getCount returning "+Integer.toString(question.countAnswers()), 0).show();
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
	/**
	 * Helper method to convert milliseconds into a date
	*	@param milliseconds	Input the java built in time as milliseconds
	 * @return newDate 	Output a date format that interprets time from milliseconds
	 */
	public String datetostring(long milliseconds){
	    Date date = new Date(); 
	    date.setTime(milliseconds);
	    String newDate=new SimpleDateFormat("MMM d yyyy").format(date);
	    return newDate;
	}
	/**
	 * sets view for author, score, date, body.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.answer_object, parent, false);
		}
		
		//inflate each listview item with "answer_object"
		Answer answer = (Answer)getItem(position);
		Button comment = (Button) convertView.findViewById(R.id.aComment);
		ImageView upvote = (ImageView) convertView.findViewById(R.id.aUpvote);
//		LinearLayout upvote = (LinearLayout)convertView.findViewById(R.id.AnswerScoreHLayout);
		
		//set text on each TextView			
		((TextView)convertView.findViewById(R.id.aBody)).setText(answer.getBody());
		TextView aScore = (TextView)convertView.findViewById(R.id.aScore);
		aScore.setText(Integer.toString(answer.getRating()));
		((TextView)convertView.findViewById(R.id.aAuthourDate)).setText("Submitted by: " +answer.getAuthor().getUsername()+", "+ datetostring(answer.getDate()));
		
		//comment.setOnClickListener(answercomments);
		comment.setOnClickListener(new CommentOnClickListener(position));
		
		//upvote.setOnClickListener(answerupvote);
		upvote.setOnClickListener(new UpVoteOnClickListener(position, aScore));
		
		
		return convertView;
	}

	
//	View.OnClickListener answercomments  = new View.OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			Intent intent = new Intent(Agora.getContext(), CommentActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			Agora.getContext().startActivity(intent);
//		}
//	};
//	
//	View.OnClickListener answerupvote =  new View.OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			Toast.makeText(Agora.getContext(), "call upvote" , Toast.LENGTH_SHORT).show();
//		}
//	};
	
	public void setQuestion(Question q) {
		this.question = q;
		notifyDataSetChanged();
	}

	/**
	 * onclick listener when clicking on comment button. Passes question answerID/questionID to commentActivity
	 * @author Team 02
	 *
	 */
	private class CommentOnClickListener implements OnClickListener {
		private int position;
		CommentOnClickListener(int position) {
			this.position = position;
		}
		public void onClick(View view) {
			Long aid = getItemId(position);
			Intent intent = new Intent(activity, CommentActivity.class);
			intent.putExtra("aid", aid);
			intent.putExtra("qid", question.getID());
			activity.startActivity(intent);
		}
	}
	
	/**
	 * onClick listener for notifying when the upvote button is clicked.
	 * @author Team 2
	 *
	 */
	private class UpVoteOnClickListener implements OnClickListener {
		private int position;
		private View aScoreTextView;
		UpVoteOnClickListener(int position, View aScoreTextView) {
			this.aScoreTextView = aScoreTextView;
			this.position = position;
		}
		public void onClick(View view) {
			Long aid = getItemId(position);
//			question.getAnswerByID(aid).upvote();
			
			try {
				QuestionController.getController().upvote(question.getID(), getItemId(position));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			Answer a = (Answer)getItem(position);
			a.upvote();
			((TextView)aScoreTextView).setText(Integer.toString(a.getRating()));

			
			//update the rating textview afterwards
		}
	}
	
}

	



