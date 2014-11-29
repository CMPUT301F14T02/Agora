package com.brogrammers.agora.views;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.R;
import com.brogrammers.agora.R.id;
import com.brogrammers.agora.R.layout;
import com.brogrammers.agora.R.menu;
import com.brogrammers.agora.data.LocationDataManager;
import com.brogrammers.agora.data.QuestionController;
import com.brogrammers.agora.helper.ImageGetter;
import com.brogrammers.agora.helper.ImageResizer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Activity for posting a question to the app. Displays two text boxes for title
 * and body input.
 * 
 * Todo: implement picturehandler to post pictures to a question.
 * 
 * @author Group02
 * 
 */
public class AuthorQuestionActivity extends Activity {

	protected Uri imageUri = null;
	protected byte[] image = null;


	/**
	 * Retrieves button layouts and activity author question layout.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_author_question);
		

		Button addQuestion = (Button) findViewById(R.id.authorQuestionAddQuestionButton);
		Button addPictureCamera = (Button) findViewById(R.id.authorQuestionAddPictureCamera);
		Button addPictureGallery = (Button) findViewById(R.id.authorQuestionAddPictureGallery);

		addQuestion.setOnClickListener(questionHandler);
		addPictureCamera.setOnClickListener(cameraHandler);
		addPictureGallery.setOnClickListener(galleryHandler);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.author_question, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {

			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	/**
	 * Retrieves text from title and body editTexts and adds question via
	 * controller.
	 */
	View.OnClickListener questionHandler = new View.OnClickListener() {
		public void onClick(View v) {
			// add question
			EditText titleText = (EditText) findViewById(R.id.authorQuestionEditText);
			EditText bodyText = (EditText) findViewById(R.id.authorQuestionBodyEditText);
			
			String title = titleText.getText().toString();
			String body = bodyText.getText().toString();
			if (title.isEmpty() || body.isEmpty()) {
				Toast.makeText(Agora.getContext(),
						"Please include a title and a description", 1).show();
				return;
			} else {
				Toast.makeText(Agora.getContext(), "Adding Question!",
						Toast.LENGTH_SHORT).show();
			}
			
			CheckBox locationBox = (CheckBox) findViewById(R.id.attachLocationQuestionBox);
			if (locationBox.isChecked()){
				LocationDataManager.getInstance();
				if (LocationDataManager.getLocationName() != null){
					
				}
			}

			QuestionController.getController().addQuestion(title, body, image);
			finish(); // ends activity
		}
	};

	View.OnClickListener cameraHandler = new View.OnClickListener() {
		public void onClick(View v) {
			ImageGetter imageGetter = new ImageGetter(
					AuthorQuestionActivity.this);
			imageUri = imageGetter.getCameraUri();
			imageGetter.getCameraImage(imageUri);
		}
	};

	View.OnClickListener galleryHandler = new View.OnClickListener() {
		public void onClick(View v) {
			ImageGetter imageGetter = new ImageGetter(
					AuthorQuestionActivity.this);
			imageGetter.getGalleryImage();
		}
	};

	// ImageGetter activity callback
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ImageGetter.CAMERA_ACTIVITY_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			image = ImageResizer.resize(imageUri);
			(new File(imageUri.getPath())).delete(); // delete the original file
			ImageView iv = (ImageView) findViewById(R.id.AuthorQuestionImage);

			Bitmap ThumbImage = ThumbnailUtils
					.extractThumbnail(BitmapFactory
							.decodeStream(new ByteArrayInputStream(image)),
							480, 360);
			iv.setImageBitmap(ThumbImage);

		} else if (requestCode == ImageGetter.GALLERY_ACTIVITY_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			Uri galleryImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(galleryImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			String picturePath = cursor.getString(cursor
					.getColumnIndex(filePathColumn[0]));

			image = ImageResizer.resize(picturePath);

			ImageView iv = (ImageView) findViewById(R.id.AuthorQuestionImage);

			Bitmap ThumbImage = ThumbnailUtils
					.extractThumbnail(BitmapFactory
							.decodeStream(new ByteArrayInputStream(image)),
							480, 360);
			iv.setImageBitmap(ThumbImage);

		} else if (resultCode != RESULT_CANCELED) {
			Toast.makeText(this, "Error when adding image.", 0).show();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (imageUri != null) {
			outState.putString("uri", imageUri.getPath());
		}
		Log.e("ONSAVEDINSTANCESTATE", "AQA ONSAVE");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		imageUri = Uri.parse(savedInstanceState.getString("uri"));
		Log.e("ONRESTOREINSTANCESTATE", "AQA onRestoreInstanceState()");
	}








}
