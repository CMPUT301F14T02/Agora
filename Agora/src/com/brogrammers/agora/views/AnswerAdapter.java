package com.brogrammers.agora.views;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.PorterDuff;
import java.util.Date;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.R;
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
	private List<Answer> answers;
	
	AnswerAdapter(Question q, Activity activity){
		this.question = q;
		this.activity = activity;
		this.inflater = (LayoutInflater)Agora.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		this.answers = null;
	}
	
	AnswerAdapter(List<Answer> answers, Activity activity) {
		this.question = null;
		this.activity = activity;
		this.answers = answers;
		this.inflater = (LayoutInflater)Agora.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
	}

	@Override
	public int getCount() {
		if (question != null) {
			return question.countAnswers();
		} else if (answers != null) {
			return answers.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (question != null) {
			return question.getAnswers().get(position);
		} else if (answers != null) {
			return answers.get(position);
		} else {
			return 0;
		}
	}

	@Override
	public long getItemId(int position) {
		if (question != null) {
			return question.getAnswers().get(position).getID();
		} else if (answers != null) {
			return answers.get(position).getID();
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
		((TextView)convertView.findViewById(R.id.aLocationText)).setText(answer.getLocationName());

		//set text on each TextView & Buttons\
		comment.setText("Comments ("+Integer.toString(answer.getComments().size())+")");
		
		// Handles the empty string case to display a blank instead of hint text
		if (!TextUtils.isEmpty(answer.getBody())) {
			((TextView)convertView.findViewById(R.id.aBody)).setText(answer.getBody().trim());
		} else {
			((TextView)convertView.findViewById(R.id.aBody)).setText(" ");
		}
		
		
		TextView aScore = (TextView)convertView.findViewById(R.id.aScore);
		aScore.setText(Integer.toString(answer.getRating()));
		((TextView)convertView.findViewById(R.id.aAuthourDate)).setText("Submitted by: " +answer.getAuthor()+", "+ datetostring(answer.getDate()));
		
		if (!TextUtils.isEmpty(answer.getLocationName())) {
			((TextView)convertView.findViewById(R.id.aLocationText)).setText(answer.getLocationName());
		} else {
			((TextView)convertView.findViewById(R.id.aLocationText)).setVisibility(View.GONE);
		}
		
		
		comment.setOnClickListener(new CommentOnClickListener(position));
		upvote.setOnClickListener(new UpVoteOnClickListener(position, aScore));
		
		ImageView thumbView = (ImageView) convertView.findViewById(R.id.AnswerImage);
		if (answer.hasImage() && answer.getImage() != null) {
			thumbView.setVisibility(View.VISIBLE);
			final Bitmap imageBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(answer.getImage()));
			Bitmap thumbImage = ThumbnailUtils.extractThumbnail(imageBitmap, 150, 100);
			thumbView.setImageBitmap(thumbImage);
			
			thumbView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					(new SimpleImagePopup(imageBitmap, activity)).show();
				}
			});
		} else {
			thumbView.setVisibility(View.GONE);
		}
		
		return convertView;
	}

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
			try {
				QuestionController.getController().upvote(question.getID(), getItemId(position));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			// Update the rating display
			Answer a = (Answer)getItem(position);
			((TextView)aScoreTextView).setText(Integer.toString(a.getRating()));

		}
	}
	
}

	



