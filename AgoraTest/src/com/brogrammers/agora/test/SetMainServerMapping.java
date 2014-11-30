package com.brogrammers.agora.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.data.ESDataManager;
import com.brogrammers.agora.helper.QuestionLoaderSaver;
import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.Author;
import com.brogrammers.agora.model.Comment;
import com.brogrammers.agora.model.Question;
import com.brogrammers.agora.views.MainActivity;
import com.google.gson.Gson;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

public class SetMainServerMapping extends ActivityInstrumentationTestCase2<MainActivity> {

	public SetMainServerMapping() {
		super(MainActivity.class);
	}
	// DO NOT RUN THIS TEST UNLESS YOU KNOW YOU WANT TO DELETE THE APPS DATA
	protected void setUp() throws Exception {
		super.setUp();
		HttpClient client = new DefaultHttpClient();
		try {
			HttpDelete deleteRequest = new HttpDelete("http://cmput301.softwareprocess.es:8080/cmput301f14t02/agora/_mapping");
			client.execute(deleteRequest);
			String mapping="{ \"agora\": {\n"+
					" \"properties\": {\n"+
					" \"answers\": {\n"+
					" \"type\": \"nested\", \n"+
					" \"properties\": {\n"+
					" \"author\": {\n"+
					" \"type\": \"string\"\n"+
					" },\n"+
					" \"body\": {\n"+
					" \"type\": \"string\"\n"+
					" },\n"+
					" \"comments\": {\n"+
					" \"properties\": {\n"+
					" \"author\": {\n"+
					" \"properties\": {\n"+
					" \"username\": {\n"+
					" \"type\": \"string\"\n"+
					" }\n"+
					" }\n"+
					" },\n"+
					" \"body\": {\n"+
					" \"type\": \"string\"\n"+
					" },\n"+
					" \"date\": {\n"+
					" \"type\": \"long\"\n"+
					" },\n"+
					" \"posted\": {\n"+
					" \"type\": \"boolean\"\n"+
					" }\n"+
					" }\n"+
					" },\n"+
					" \"date\": {\n"+
					" \"type\": \"long\"\n"+
					" },\n"+
					" \"hasImage\": {\n"+
					" \"type\": \"boolean\"\n"+
					" },\n"+
					" \"rating\": {\n"+
					" \"type\": \"long\"\n"+
					" },\n"+
					" \"uniqueID\": {\n"+
					" \"type\": \"long\"\n"+
					" }\n"+
					" }\n"+
					" },\n"+
					" \"author\": {\n"+
					" \"type\": \"string\"\n"+
					" },\n"+
					" \"body\": {\n"+
					" \"type\": \"string\"\n"+
					" },\n"+
					" \"comments\": {\n"+
					" \"properties\": {\n"+
					" \"author\": {\n"+
					" \"properties\": {\n"+
					" \"username\": {\n"+
					" \"type\": \"string\"\n"+
					" }\n"+
					" }\n"+
					" },\n"+
					" \"body\": {\n"+
					" \"type\": \"string\"\n"+
					" },\n"+
					" \"date\": {\n"+
					" \"type\": \"long\"\n"+
					" },\n"+
					" \"posted\": {\n"+
					" \"type\": \"boolean\"\n"+
					" }\n"+
					" }\n"+
					" },\n"+
					" \"date\": {\n"+
					" \"type\": \"long\"\n"+
					" },\n"+
					" \"hasImage\": {\n"+
					" \"type\": \"boolean\"\n"+
					" },\n"+
					" \"location\": {\n"+
					" \"type\": \"geo_point\" \n"+
					" },\n"+
					" \"rating\": {\n"+
					" \"type\": \"long\"\n"+
					" },\n"+
					" \"title\": {\n"+
					" \"type\": \"string\"\n"+
					" },\n"+
					" \"uniqueID\": {\n"+
					" \"type\": \"long\"\n"+
					" }\n"+
					" }\n"+
					" }\n"+
					" }";
			HttpPost httppost = new HttpPost("http://cmput301.softwareprocess.es:8080/cmput301f14t02/agora/_mapping");
			httppost.setEntity(new StringEntity(mapping));
			httppost.setHeader("Accept", "application/json");
			client.execute(httppost);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void test_() {
		// empty test to let the above run
	}
}

	