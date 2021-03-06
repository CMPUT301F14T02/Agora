package com.brogrammers.agora.views;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.R;
import com.brogrammers.agora.R.id;
import com.brogrammers.agora.R.layout;
import com.brogrammers.agora.R.menu;
import com.brogrammers.agora.data.LocationDataManager;
import com.brogrammers.agora.model.SimpleLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
/**
 * Settings activity for the user. Allows user to set their GPS coordinates
 * either by GPS or by manual input. 
 * @author Group02
 *
 */
public class UserPrefActivity extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener {

	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	private LocationDataManager locationManager;
	private double lat;
	private double lon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_pref);
		mLocationClient = new LocationClient(this, this, this);
		int isGooglePlayServiceAvilable = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(Agora.getContext());
		if (isGooglePlayServiceAvilable == ConnectionResult.SUCCESS) {
		} else {
			Toast.makeText(Agora.getContext(),
					"Google play services is required.", Toast.LENGTH_SHORT)
					.show();
			GooglePlayServicesUtil.getErrorDialog(isGooglePlayServiceAvilable,
					this, 1122).show();
		}
		locationManager = LocationDataManager.getInstance();
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

	@Override
	protected void onResume() {
		super.onResume();
		mLocationClient.connect();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_pref, menu);
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

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();
		LocationDataManager.getInstance();
		// Check which radio button was clicked
		switch (view.getId()) {
		// GPS enabled
		case R.id.useGpsRadio:
			if (checked) {
				setGpsLocation();
			}
			break;
		case R.id.setTextRadio:
			if (checked) {
				//setHardStringLocation();
				// manualLocation();
				break;
			}
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	/*
	 * Creates instance of location data manger which parses the coordinates
	 * given by the GPS/Wifi Location Client
	 */
	private void setGpsLocation() {
		if (mLocationClient.isConnected()) {
			LocationDataManager.getInstance();
			mCurrentLocation = mLocationClient.getLastLocation();
			if (mCurrentLocation != null) {
				lat = mCurrentLocation.getLatitude();
				lon = mCurrentLocation.getLongitude();
				locationManager.initLocation(lat, lon);
				Toast.makeText(Agora.getContext(),
						LocationDataManager.getLocationName(),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Agora.getContext(),
						"Location is not available.", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
	/*
	 * Creates instance of location data manger which parses the string
	 * given by the user
	 */
	public void manualLocation(View v) {
		RadioButton setLocationButton = (RadioButton) findViewById(R.id.setTextRadio);
		if (!setLocationButton.isChecked()){
			Toast.makeText(Agora.getContext(),
					"Please ensure that set location option is selected", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		EditText setLocation = (EditText) findViewById(R.id.setLocationEditText);
		String strLocation = setLocation.getText().toString();

		locationManager.initLocation(strLocation);
		Toast.makeText(Agora.getContext(),
				LocationDataManager.getLocationName(), Toast.LENGTH_SHORT)
				.show();
	}

}
