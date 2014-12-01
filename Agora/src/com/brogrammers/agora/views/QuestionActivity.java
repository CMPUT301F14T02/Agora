package com.brogrammers.agora.views;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.util.TextUtils;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.Observer;
import com.brogrammers.agora.R;
import com.brogrammers.agora.R.id;
import com.brogrammers.agora.R.layout;
import com.brogrammers.agora.R.menu;
import com.brogrammers.agora.data.CacheDataManager;
import com.brogrammers.agora.data.QuestionController;
import com.brogrammers.agora.model.Question;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

/**
 * Question activity displaying view of a single question. Todo: implement
 * answer count
 * 
 * @author Group 02
 * 
 */
public class QuestionActivity extends Activity implements Observer {
	private List<Question> qList;
	private Long qid;
	private QuestionController controller;

	/**
	 * When activity is created, retrieve question id via intent from main
	 * activity. Sets buttons, and onclick listeners for comments/answers.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);

		Intent intent = getIntent();
		qid = intent.getLongExtra("qid", 0L);
		if (qid.equals(0L)) {
			Toast.makeText(this, "Didn't recieve a qid in intent", 0).show();
			finish();
		}

		controller = QuestionController.getController();
		controller.setObserver(this);

		
		Button viewComment = (Button) findViewById(R.id.QuestionCommentsButton);
		Button viewAnswer = (Button) findViewById(R.id.QuestionAnswersButton);
		ImageView upVoteQuestion = (ImageView) findViewById(R.id.QuestionUpVoteButton);

		viewComment.setOnClickListener(new openCommentsView());
		viewAnswer.setOnClickListener(new openAnswerView());
		upVoteQuestion.setOnClickListener(new upVoteQuestion());


	}

	protected void onResume() {
		super.onResume();
		qList = controller.getQuestionById(qid);
		update();
	}

	/**
	 * helper function to convert date in ms to a date format MMM d yyyy
	 * 
	 * @param milliseconds
	 *            date in milliseconds
	 * @return string of formatted date
	 */
	public String datetostring(long milliseconds) {
		Date date = new Date();
		date.setTime(milliseconds);
		String newDate = new SimpleDateFormat("MMM d yyyy").format(date);
		return newDate;
	}

	/**
	 * Called when question list is changed. check questionlist for question
	 * retrieved via id, and set the textviews accordingly.
	 */
	@Override
	public void update() {
		if (qList.size() == 0) {
			Toast.makeText(this, "QuestionActivity recieved empty list on update", 0).show();
			return;
		}
		Question q = qList.get(0);
		
		TextView qTitle = (TextView) findViewById(R.id.qTitle);
		TextView qBody = (TextView) findViewById(R.id.qBody);
		TextView qScore = (TextView) findViewById(R.id.qScore);
		TextView qLocation = (TextView) findViewById(R.id.aqLocationText);
		TextView authorDate = (TextView) findViewById(R.id.AuthourDate);
		String authorLine = "Submitted by: ";

		Button viewComment = (Button) findViewById(R.id.QuestionCommentsButton);
		Button viewAnswer = (Button) findViewById(R.id.QuestionAnswersButton);

		authorLine += q.getAuthor() + ", "
				+ datetostring(q.getDate());
		authorDate.setText(authorLine);

		// Handle Empty String Cases to display a blank instead of the hint text
		if (!TextUtils.isEmpty(q.getTitle())) {
			qTitle.setText(q.getTitle().trim());
		} else {
			qTitle.setText(" ");
		}

		if (!TextUtils.isEmpty(q.getBody())) {
			qBody.setText(q.getBody().trim());
		} else {
			qBody.setText(" ");
		}
		
		if (!TextUtils.isEmpty(q.getLocationName())) {
			qLocation.setText(q.getLocationName().trim());
		} else {
			qLocation.setVisibility(View.GONE);
		}
		

		qScore.setText(Integer.toString(q.getRating()));
		CacheDataManager.getInstance().rememberQuestion(q);
		viewAnswer.setText("Answers ("
				+ Integer.toString(q.getAnswers().size()) + ")");
		viewComment.setText("Comments ("
				+ Integer.toString(q.getComments().size()) + ")");

		// set associated image, if any
		ImageView thumbView = (ImageView) findViewById(R.id.QuestionImage);
		if (q.hasImage() && q.getImage() != null) {
//			thumbView.setVisibility(View.VISIBLE); // this ImageView is set to GONE by default
			final Bitmap imageBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(q.getImage()));
			Bitmap thumbImage = ThumbnailUtils.extractThumbnail(imageBitmap, 200, 200);
			thumbView.setImageBitmap(thumbImage);

			thumbView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					(new SimpleImagePopup(imageBitmap, QuestionActivity.this)).show();
				}
			});
		} else {
			thumbView.setVisibility(View.GONE);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		Question q = qList.get(0);
		getActionBar().setTitle(q.getTitle());   
		return true;
	}

	/**
	 * Action bar that contains button for settings, favouriting, flagging, and
	 * adding an answer.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			Intent intent = new Intent(Agora.getContext(), UserPrefActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_favorite:
			favorite();
			return true;
		case R.id.action_flag:
			flag();
			return true;
        case R.id.refreshQ:
        	onResume();
        	return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Opens activity for the answers corresponding to the question and puts
	 * question id in intent
	 * 
	 * @author group02
	 * 
	 */
	private class openAnswerView implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(QuestionActivity.this,
					AnswerActivity.class);
			intent.putExtra("qid", qid);
			startActivity(intent);
		}
	}

	/**
	 * Opens activity for the comments corresponding to the question and puts
	 * question id in intent
	 * 
	 * @author group02
	 * 
	 */
	private class openCommentsView implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(QuestionActivity.this,
					CommentActivity.class);
			intent.putExtra("qid", qid);
			startActivity(intent);
		}
	}

	// Placeholder functions, remove later
	public void favorite() {
		Toast.makeText(this, "favorite", Toast.LENGTH_SHORT).show();
		if (qList.size() != 0) {
			// increments votes in the view, but does not save.
			QuestionController controller = QuestionController.getController();
			Question q = qList.get(0);
			controller.addFavorite(q.getID());
		}
	}

	public void flag() {
		Toast.makeText(this, "flag", Toast.LENGTH_SHORT).show();
		if (qList.size() != 0) {
			// increments votes in the view, but does not save.
			QuestionController controller = QuestionController.getController();
			Question q = qList.get(0);
			controller.addCache(q.getID());
		}
	}

	/**
	 * upvote question when upvote button is clicked on.
	 * 
	 * @author Kevin
	 * 
	 */
	private class upVoteQuestion implements OnClickListener {
		public void onClick(View v) {
			if (qList.size() != 0) {
				// increments votes in the view, but does not save.
				QuestionController controller = QuestionController
						.getController();
				Question q = qList.get(0);
				try {
					controller.upvote(q.getID(), null);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				TextView qScore = (TextView) findViewById(R.id.qScore);
				qScore.setText(Integer.toString(q.getRating()));
			}
		}
	}


}
