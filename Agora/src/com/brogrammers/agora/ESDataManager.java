package com.brogrammers.agora;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.brogrammers.agora.QueryItem.RequestType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.entity.StringEntity;

public class ESDataManager { // implements DataManager
	protected String DOMAIN = "http://cmput301.softwareprocess.es:8080/"; // domain
	protected String INDEXNAME = "testing/"; 	// name of the ES database/index
	protected String TYPENAME = "agora/"; 	// name of the ES table/type 

	public boolean connected; 	// connected status
	// Queue of statements that need to be run on
	// the server
	private OfflineQueue offlineQueue;
	
	private static ConnectionReceiver connectionReceiver;
	private static ESDataManager self;
			
	public static ESDataManager getInstance() {
		if (self == null) {
			self = new ESDataManager();
		}
		return self;
	}

	public boolean isConnected(){
		return this.connected;
	}
	
	protected ESDataManager() {
		// get connection status
		// after creation connectivity status will be monitored by a
		// broadcast receiver.
		ConnectivityManager cm = (ConnectivityManager) Agora.getContext().getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		connectionReceiver = new ConnectionReceiver();
		Agora.getContext().registerReceiver(connectionReceiver, 
							new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}
	
	protected ESDataManager(String domain, String indexName, String typeName) {
		this();
		DOMAIN = domain;
		INDEXNAME = indexName;
		TYPENAME = typeName;
	}

	public class ConnectionReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent){
			ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = conManager.getActiveNetworkInfo();
			// code taken from http://stackoverflow.com/questions/15698790/broadcast-receiver-for-checking-internet-connection-in-android-app
			// Author user1381827
			// November 2, 2014
			if (netInfo != null && netInfo.isConnected()){
				connected = true;
				Toast.makeText(Agora.getContext(), "Connected", Toast.LENGTH_SHORT).show();
			} else {
				connected = false;
				Toast.makeText(Agora.getContext(), "Disconnected", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public List<Question> getQuestions() throws UnsupportedEncodingException {
		// assuming the question view by default sorts by date
		String requestBody = "{" +
			    "\"query\": {\"match_all\": {}}," +
			    "\"sort\": [" +
			    	"{" +
			    	"\"date\": {" +
			    		"\"order\": \"desc\"" + 
			    	"}"+
			    	"}]}";
		String endPoint = "_search";
		return getQuestions(requestBody, endPoint);
	}
	
	public List<Question> getQuestions(String requestBody, String endPoint) throws UnsupportedEncodingException {
		AsyncHttpClient client = new AsyncHttpClient();
		StringEntity stringEntityBody = new StringEntity(requestBody);
		final List<Question> questionList = new ArrayList<Question>();
		String URI = DOMAIN + INDEXNAME + TYPENAME + endPoint;
		Log.e("SERVER GET URI", DOMAIN + INDEXNAME + TYPENAME + endPoint);
		Log.e("SERVER GET BODY", requestBody);
		client.post(Agora.getContext(), URI,
						stringEntityBody,
						"application/json",
						new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int status, Header[] arg1, byte[] responseBody, Throwable arg3) {
				
				Log.e("SERVER", "getQuestion failure: "+Integer.toString(status));
				Log.e("SERVER", "responsebody: "+new String(responseBody));
				Toast.makeText(Agora.getContext(), "ES Get Question On Fail", 0).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("SERVER", "Question get success");
				try {
					String responseBody = new String(arg2);
					JSONObject jsonRes = new JSONObject(responseBody);
					jsonRes = jsonRes.getJSONObject("hits");
				    JSONArray jsonArray = jsonRes.getJSONArray("hits");
				    Gson gson = new Gson();
				    for (int i = 0; i < jsonArray.length(); i++) {
				    	// get each object in the response, convert to object
				    	// and add to list.
				    	JSONObject q = jsonArray.getJSONObject(i);  
						q = q.getJSONObject("_source");
						Question qObject = gson.fromJson(q.toString(), Question.class);
						questionList.add(qObject);
						Log.e("SERVER", qObject.getID().toString()+" "+qObject.getBody());
				    }
				    Toast.makeText(Agora.getContext(), "ES Get Question Sucess", 0).show();
					QuestionController.getController().update();
				} catch (JSONException e) {
					e.printStackTrace();
				}	
			}
        });
		return questionList;
	}
	
//	public List<Answers> searchAnswers(String query) throws UnsupportedEncodingException {
//		String requestBody = "";
//		String endpoint = "_search";
//		List<Question> getQuestions(DOMAIN, INDEX, TYPENAME, requestBody, endPoint);
//		// sort through questions and extract answers
//		return List<Answers>;
//	}
	
	public List<Question> searchQuestions(String query) throws UnsupportedEncodingException {	
		String requestBody = "{" +
			    "\"query\": {" +
			        "\"multi_match\": {" +
			           "\"query\":" + 
			           "\"" + query + "\"" + "," +
			           "\"type\": \"most_fields\"," +
			           "\"fields\": [" +
			           "\"title\", \"body\"" +
			           "]}}}";
		String endPoint = "_search";
		return getQuestions(requestBody, endPoint);
	}
	
	
	public List<Question> getQuestionById(Long id) throws UnsupportedEncodingException {
		// assuming the question view by default sorts by date
		String requestBody = "{" +
			    "\"query\": {" +
	        	"\"match\": {" +
	           		"\"_id\":" + id.toString() +
	        	"}}}";
		String endPoint = "_search";
		return getQuestions(requestBody, endPoint);
	}
	
		
	public boolean pushQuestion(Question q) throws UnsupportedEncodingException {		
		Gson gson = new Gson();
		String questionSerialized = gson.toJson(q);
		String endPoint = Long.toString(q.getID());
		return push(questionSerialized, endPoint);
	}
	
	public boolean pushAnswer(Answer a, Long qID, CacheDataManager cache) throws UnsupportedEncodingException {		
		Question q = cache.getQuestionById(qID);
		Gson gson = new Gson();
		String answerSerialized = gson.toJson(q.getAnswers());
		answerSerialized = "{" +
				"\"doc\": { " +
				"\"answers\":" + answerSerialized + 
					"}" +
				"}";
		String endPoint = Long.toString(qID) + "/" + "_update";
		return push(answerSerialized, endPoint);
	}
	
	// if comment is on an answer pass aID, if on question itself pass null
	public boolean pushComment(Comment c, Long qID, Long aID, CacheDataManager cache) throws UnsupportedEncodingException {
		String endPoint = Long.toString(qID) + "/" + "_update";
		Question q = cache.getQuestionById(qID);
		Gson gson = new Gson();
		if (aID == null) {
			String commentSerialized = gson.toJson(q.getComments());
			commentSerialized = "{" +
					"\"doc\": { " +
					"\"comments\":" + commentSerialized + 
						"}" +
					"}";
			return push(commentSerialized, endPoint);

		} else {
			// if qid is not null re-upload the answer array
			String answerSerialized = gson.toJson(q.getAnswers());
			answerSerialized = "{" +
					"\"doc\": { " +
					"\"answers\":" + answerSerialized + 
						"}" +
					"}";
			return push(answerSerialized, endPoint);	
		}
	}
	
	public boolean pushUpvote(Long qID) throws UnsupportedEncodingException {
		Question q = CacheDataManager.getInstance().getQuestionById(qID);
		int upvotes = q.getRating();
		String upVoteSerialized = "{" +
				"\"doc\": { " +
				"\"rating\":" + Integer.toString(upvotes) + 
					"}" +
				"}";
		String endPoint = Long.toString(qID) + "/" + "_update";
		return push(upVoteSerialized, endPoint);
	}
	
	
	public boolean push(String body, String endPoint) throws UnsupportedEncodingException{
		StringEntity stringEntityBody = new StringEntity(body);
		String URI = DOMAIN + INDEXNAME + TYPENAME + endPoint;
		QueryItem queryItem = new QueryItem(stringEntityBody, URI, RequestType.POST);
		updateServer(queryItem);
		return false;
	}

	
	private void updateServer(final QueryItem qItem){
		Log.e("SERVER UPDATED", "updateServer called");
		if (connected) {
			AsyncHttpClient client = new AsyncHttpClient();
			client.post(Agora.getContext(), qItem.getURI(), qItem.getBody(), "application/json", new AsyncHttpResponseHandler() {
				@Override
			    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
			        // called when response HTTP status is "200 OK"
					Log.e("SERVER UPDATED", "Update server method success.");
			    }
			    @Override
			    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
			        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
			    	Log.e("SERVER UPDATE", "updateServer method failure.");
			    	Toast.makeText(Agora.getContext(), "Update server method failure.", 0).show();
//			    	offlineQueue.addToQueue(qItem); 
			    }
			});
			
		} else {
			offlineQueue.addToQueue(qItem);
		}
		
	}
	
	public void setServerMapping() throws ClientProtocolException, IOException{
		// remove the forwardslash from the typename
		String typeName = TYPENAME.substring(0, TYPENAME.length() - 1);
		String mapBody = "{" +
	         "\"" +  typeName  + "\": {" +
	            "\"properties\": {" +
	               "\"answers\": {" +
	                  "\"type\": \"nested\"," +
	                  "\"properties\": {" +
	                     "\"author\": {" +
	                        "\"type\": \"string\"" +
	                     "}," +
	                     "\"body\": {" +
	                        "\"type\": \"string\"" +
	                     "}" +
	                  "}" +
	               "}," +
	               "\"author\": {" +
	                  "\"type\": \"string\"" +
	               "}," +
	               "\"date\": {" +
	                  "\"type\": \"string\"" +
	               "}" +
	            "}" +
	         "}" +
         "}" +
     "}" +
 "}";
		// Delete the current mapping
		HttpClient client = new DefaultHttpClient();
        HttpDelete deleteRequest = new HttpDelete(DOMAIN + INDEXNAME + TYPENAME + "_mapping");
        client.execute(deleteRequest);

        // Set a new mapping
		String mapURI = DOMAIN + INDEXNAME + "_mapping/" + TYPENAME;
		StringEntity mapBodyStringEntity = new StringEntity(mapBody);
		QueryItem queryItem = new QueryItem(mapBodyStringEntity, mapURI, RequestType.POST);
		updateServer(queryItem);
	}
}
