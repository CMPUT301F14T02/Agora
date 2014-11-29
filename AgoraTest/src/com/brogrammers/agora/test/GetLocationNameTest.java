package com.brogrammers.agora.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.data.ESDataManager;
import com.brogrammers.agora.data.LocationDataManager;
import com.brogrammers.agora.helper.QuestionLoaderSaver;
import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.Author;
import com.brogrammers.agora.model.Comment;
import com.brogrammers.agora.model.Location;
import com.brogrammers.agora.model.Question;
import com.brogrammers.agora.views.MainActivity;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Toast;

public class GetLocationNameTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public GetLocationNameTest() {
		super(MainActivity.class);
	}
	public void testLocationNameTest() throws Throwable {
		// create a question object post it, and ensure we get back the same object.
		final CountDownLatch postSignal = new CountDownLatch(1);
		LocationDataManager ldm = new LocationDataManager();
        ldm.initLocation(53.526797, -113.5273);
		postSignal.await(1, TimeUnit.SECONDS);
		final CountDownLatch signal = new CountDownLatch(1);
		// compare the local and received copies.
		Location location = ldm.getLocation();
		assertTrue("Location name did not match received name", location.getLocationName().equalsIgnoreCase("Edmonton"));
	}
}

	
