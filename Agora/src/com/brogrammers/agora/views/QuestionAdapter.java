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
import com.brogrammers.agora.helper.QuestionFilterer;
import com.brogrammers.agora.helper.QuestionSorter;
import com.brogrammers.agora.model.Question;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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
	private List<Question> qList;
	private List<Question> unfilteredList = null;
	private Activity activity;
	
	// Adapter Constructor
	public QuestionAdapter(List<Question> qList, Activity activity) {
		this.inflater = (LayoutInflater)Agora.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.qList = qList;
		this.activity = activity;
		doSortAndFilter();
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
		((TextView)convertView.findViewById(R.id.qobjecttitle)).setText(question.getTitle().trim());

		((TextView)convertView.findViewById(R.id.qAuthor)).setText("" +question.getAuthor()+", "+ datetostring(question.getDate()));

		LinearLayout HLayout = (LinearLayout)convertView.findViewById(R.id.RatingHLayout);
		((TextView)HLayout.findViewById(R.id.qObjectScore)).setText(Integer.toString(question.getRating()));
		
		LinearLayout HLayoutAcount = (LinearLayout)convertView.findViewById(R.id.HLayoutAnswerCount);
		((TextView)HLayoutAcount.findViewById(R.id.qACountText)).setText(Integer.toString(question.countAnswers()));
		
		
		if (!TextUtils.isEmpty(question.getLocationName())) {
			((TextView)convertView.findViewById(R.id.qlocation)).setText(question.getLocationName());
		} else {
			((TextView)convertView.findViewById(R.id.qlocation)).setVisibility(View.GONE);
		}
		
		List<Long> favoritedQuestions = DeviceUser.getUser().getFavoritedQuestionIDs();
		ImageView qFavorite = (ImageView)convertView.findViewById(R.id.qQuestionFavourite);
		if (favoritedQuestions.contains(question.getID())) {
			qFavorite.setImageResource(R.drawable.ic_pinkfavouritetag);	
		} else {
			
			qFavorite.setImageResource(R.drawable.ic_pinkunfavouritetag);	
		}
		qFavorite.setOnClickListener(new qFavoriteOnClickListener(position, qFavorite));
		
		List<Long> flaggedQuestions = DeviceUser.getUser().getCachedQuestionIDs();
		ImageView qFlag = (ImageView)convertView.findViewById(R.id.qQuestionFlag);
		if (flaggedQuestions.contains(question.getID())) {
			qFlag.setImageResource(R.drawable.ic_tag);
		} else {
			qFlag.setImageResource(R.drawable.ic_untag);
		}
		qFlag.setOnClickListener(new qFlagOnClickListener(position, qFlag));
	
		convertView.setOnClickListener(new QuestionOnClickListener(position));
		
		return convertView;
	}
	
	
	/**
	 * This converts the date from milliseconds to a date format that the user can interpret.
	 * 
	 * @param milliseconds			Input the java built in time as milliseconds
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
			Long qid = qList.get(position).getID();
			Intent intent = new Intent(activity, QuestionActivity.class);
			intent.putExtra("qid", qid);
			activity.startActivity(intent);
		}
	} 
	
	private class qFlagOnClickListener implements OnClickListener {
		private int position;
		private ImageView image;
		qFlagOnClickListener(int position, ImageView image) {
			this.position = position;
			this.image = image;
		}
		public void onClick(View view) {
			Long qid = qList.get(position).getID();
			QuestionController controller = QuestionController.getController();
			controller.addCache(qid);
			image.setImageResource(R.drawable.ic_tag);
		}
	}
	
	private class qFavoriteOnClickListener implements OnClickListener {
		private int position;
		private ImageView image;
		qFavoriteOnClickListener(int position, ImageView image) {
			this.position = position;
			this.image = image;
		}
		public void onClick(View view) {
			Long qid = qList.get(position).getID();
			QuestionController controller = QuestionController.getController();
			controller.addFavorite(qid);
			image.setImageResource(R.drawable.ic_pinkfavouritetag);	
		}
	}
	
	public void doSortAndFilter() {
		qList = unfilteredList == null ? qList : unfilteredList;
		QuestionSorter sorter = new QuestionSorter();
		unfilteredList = sorter.sort(qList);
		QuestionFilterer filterer = new QuestionFilterer();
		qList = filterer.filter(unfilteredList);
		notifyDataSetChanged();
	}

	
}






