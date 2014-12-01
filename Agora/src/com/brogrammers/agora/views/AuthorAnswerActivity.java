package com.brogrammers.agora.views;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Activity screen when posting an answer to a question. Contains a textbox for the body of an answer.
 * Todo: implement picturehandler to post pictures.
 * @author Group02
 *
 */
public class AuthorAnswerActivity extends Activity {
	private Long qid;
	protected Uri imageUri = null;
	protected byte[] image = null;
	
	/**
	 * Todo: implement picturehandler to post pictures.
	 * 
	 * Retrieves question id from intent. 
	 * Retrieves button layouts and activity layout.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_author_answer);

		Intent intent = getIntent();
		qid = intent.getLongExtra("qid", 0L);
		if (qid.equals(0L)) { Toast.makeText(this, "Didn't recieve a qid in intent", 0).show(); finish(); }		
		
		Button addAnswer = (Button) findViewById(R.id.authorAnswerAddAnswerButton);
		Button addPictureCamera = (Button) findViewById(R.id.authorAnswerAddPictureCamera);
		Button addPictureGallery = (Button) findViewById(R.id.authorAnswerAddPictureGallery);

		addAnswer.setOnClickListener(answerHandler);
		addPictureCamera.setOnClickListener(cameraHandler);
		addPictureGallery.setOnClickListener(galleryHandler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.author_answer, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(Agora.getContext(), UserPrefActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	View.OnClickListener answerHandler = new View.OnClickListener() {
		public void onClick(View v) {
			// add answer
			EditText bodyText = (EditText)findViewById(R.id.authorAnswerBodyEditText);
    		Toast.makeText(Agora.getContext(), "Adding Answer!", Toast.LENGTH_SHORT).show();
    		String body = bodyText.getText().toString();
    		try {
				QuestionController.getController().addAnswer(body, image, qid);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		finish();
    		
		}
	};
	

	View.OnClickListener cameraHandler = new View.OnClickListener() {
		public void onClick(View v) {
			ImageGetter imageGetter = new ImageGetter(AuthorAnswerActivity.this);
			imageUri = imageGetter.getCameraUri();
			imageGetter.getCameraImage(imageUri);
		}
	};


	View.OnClickListener galleryHandler = new View.OnClickListener() {
		public void onClick(View v) {
			ImageGetter imageGetter = new ImageGetter(AuthorAnswerActivity.this);
			imageGetter.getGalleryImage();
		}
	};
	
	// ImageGetter activity callback
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ImageGetter.CAMERA_ACTIVITY_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			image = ImageResizer.resize(imageUri);
			(new File(imageUri.getPath())).delete(); // delete the original file
			ImageView iv = (ImageView) findViewById(R.id.AuthorAnswerImage);

			Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(
					BitmapFactory.decodeStream(new ByteArrayInputStream(image)), 480, 360);
			iv.setImageBitmap(ThumbImage);
			
		} else if (requestCode == ImageGetter.GALLERY_ACTIVITY_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			Uri galleryImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(galleryImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			String picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
			
			image = ImageResizer.resize(picturePath);
			
			ImageView iv = (ImageView) findViewById(R.id.AuthorAnswerImage);

			Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(
					BitmapFactory.decodeStream(new ByteArrayInputStream(image)), 480, 360);
			iv.setImageBitmap(ThumbImage);

		} else if (resultCode != RESULT_CANCELED) {
			Toast.makeText(this, "Error when adding image.", 0).show();
		}
	}
}
