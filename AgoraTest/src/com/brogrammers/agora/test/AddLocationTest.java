package com.brogrammers.agora.test;


import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;

import com.brogrammers.agora.views.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;

public class AddLocationTest extends
		ActivityInstrumentationTestCase2<MainActivity> implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	public AddLocationTest(Class<MainActivity> activityClass) {
		super(MainActivity.class);
		// TODO Auto-generated constructor stub
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	public void testCreateLocation() {
		String cow = "Mooo";
		assertTrue(cow=="Mooo");
	}

}