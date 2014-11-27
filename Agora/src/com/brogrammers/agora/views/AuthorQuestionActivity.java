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
public class AuthorQuestionActivity extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener {

	protected Uri imageUri = null;
	protected byte[] image = null;
	private LocationClient mLocationClient;
	private Location mCurrentLocation;
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 2;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
   
    // Define an object that holds accuracy and frequency parameters
    LocationRequest mLocationRequest;
    Location testLocation;

	/**
	 * Retrieves button layouts and activity author question layout.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_author_question);
		
		mLocationClient = new LocationClient(this, this, this);
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

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
	protected void onStart() {
		super.onStart();
		// Connect the client.
		mLocationClient.connect();
	}

	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();
		super.onStop();
	}
	protected void onResume() {
		super.onResume();
		mLocationClient.connect();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
	        int isGooglePlayServiceAvilable = GooglePlayServicesUtil
	                .isGooglePlayServicesAvailable(Agora.getContext());
	        if (isGooglePlayServiceAvilable == ConnectionResult.SUCCESS){
				Toast.makeText(Agora.getContext(), "Google play services available",
						Toast.LENGTH_SHORT).show();
				
	        }
	        else{
				Toast.makeText(Agora.getContext(), "Nien, Google play services ist not available. Error Number: "+ isGooglePlayServiceAvilable,
						Toast.LENGTH_SHORT).show();
	            GooglePlayServicesUtil.getErrorDialog(isGooglePlayServiceAvilable, this, 1122).show();
				
	        }
			
			toastLocationTest();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void toastLocationTest() {
		if (mLocationClient.isConnected()) {
			mCurrentLocation = mLocationClient.getLastLocation();
			if (mCurrentLocation != null) {
				Toast.makeText(Agora.getContext(), "Working...",
						Toast.LENGTH_SHORT).show();
				Toast.makeText(Agora.getContext(), mCurrentLocation.toString(),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Agora.getContext(), "Location is null",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(Agora.getContext(),
					"No Bueno, Location Client not connected",
					Toast.LENGTH_SHORT).show();
		}
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

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Toast.makeText(Agora.getContext(), "Connection to location client Failed",
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onConnected(Bundle arg0) {
		Toast.makeText(Agora.getContext(), "Connecting to location client...",
				Toast.LENGTH_SHORT).show();

		if (!mLocationClient.isConnecting()) {
			Toast.makeText(Agora.getContext(), "Connected!",
					Toast.LENGTH_SHORT).show();
			//mock location testing
			mLocationClient.setMockMode(true);
			testLocation = createLocation(LAT, LNG, ACCURACY);
			mLocationClient.setMockLocation(testLocation);
			
			mCurrentLocation = mLocationClient.getLastLocation();
			mLocationClient.requestLocationUpdates(mLocationRequest, this);

		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Toast.makeText(Agora.getContext(), "Location disconnected",
				Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

	}


	 
    private static final String PROVIDER = "flp";
    private static final double LAT = 53.526797;
    private static final double LNG = -113.5273;
    private static final float ACCURACY = 3.0f;
    /*
     * From input arguments, create a single Location with provider set to
     * "flp"
     */
    public Location createLocation(double lat, double lng, float accuracy) {
        // Create a new Location
        Location newLocation = new Location(PROVIDER);
        newLocation.setLatitude(lat);
        newLocation.setLongitude(lng);
        newLocation.setAccuracy(accuracy);
        return newLocation;
    }


}
