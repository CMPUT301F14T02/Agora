package com.brogrammers.agora.views;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.R;
import com.brogrammers.agora.R.id;
import com.brogrammers.agora.R.layout;
import com.brogrammers.agora.R.menu;
import com.brogrammers.agora.data.QuestionController;
import com.brogrammers.agora.helper.ImageGetter;
import com.brogrammers.agora.helper.ImageResizer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
		Button addPicture = (Button) findViewById(R.id.authorQuestionAddPictureButton);

		addQuestion.setOnClickListener(questionHandler);
		addPicture.setOnClickListener(pictureHandler);

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
			Toast.makeText(Agora.getContext(), "Adding Question!",
					Toast.LENGTH_SHORT).show();

			String title = titleText.getText().toString();
			String body = bodyText.getText().toString();
			// Toast.makeText(Agora.getContext(), "Title: "+title,
			// Toast.LENGTH_SHORT).show();
			// Toast.makeText(Agora.getContext(), "Body: "+body,
			// Toast.LENGTH_SHORT).show();

			QuestionController.getController().addQuestion(title, body, image);
//			Toast.makeText(Agora.getContext(), "adding question, image size = "+image.length, 0).show();
			finish();

		}
	};
	
	View.OnClickListener pictureHandler = new View.OnClickListener() {
		public void onClick(View v) {
			ImageGetter imageGetter = new ImageGetter(AuthorQuestionActivity.this);
			imageUri = imageGetter.getUri();
			imageGetter.getImage(imageUri);
		}
	};

	// ImageGetter camera activity callback
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ImageGetter.CAMERA_ACTIVITY_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			Log.e("IMAGE", "onActivityResult uri ="+imageUri.getPath());
			image = ImageResizer.resize(imageUri);
			if (image == null) Log.e("IMAGE", "image is null");
			else Log.e("IMAGE", "image byte[] size = "+image.length);
			
			ImageView iv = (ImageView) findViewById(R.id.AuthorQuestionImage);
//			iv.setImageDrawable(Drawable.createFromPath(imageUri.getPath()));

			// https://stackoverflow.com/questions/2577221/android-how-to-create-runtime-thumbnail
			Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(
					BitmapFactory.decodeStream(new ByteArrayInputStream(image)), 480, 360);
			
			Toast.makeText(this, "ThumbImage size = "+Integer.toString(ThumbImage.getByteCount()), 0).show();
			
			iv.setImageBitmap(ThumbImage);
			
			

			
//			ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
//			Bitmap jpegImage = BitmapFactory.decodeStream(bais);
//			iv.setImageBitmap(jpegImage);
//			String s64 = Base64.encode(input, flags)(imageBytes, Base64.DEFAULT);
//			Toast.makeText(this, "string size="+s64.length(), 0).show();
			
		} else {
			Toast.makeText(this, "Error when adding image.", 0).show();
		}
	}

}









