package com.brogrammers.agora;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Callable;

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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

public class ESDataManager implements DataManager {
	// domain
	public static final String DOMAIN = "http://cmput301.softwareprocess.es:8080/";
	// name of the ES database/index
	public static final String INDEXNAME = "testing/";
	// name of the ES table/type 
	public static final String TYPENAME = "question/";
	// connected status
	public boolean connected;
	// Queue of statements that need to be run on
	// the server
	private OfflineQueue offlineQueue;
	
	// TODO: Register a broadcast receiver for connectivity status.
	
	private static ESDataManager self;
			
	public static ESDataManager getInstance(){
		if (self == null) {
			self = new ESDataManager();
		}
		return self;
	}

	public boolean isConnected(){
		return this.connected;
	}
	
	private ESDataManager() {
		// get connection status
		// after creation connectivity status will be monitored by a
		// broadcast receiver.
		ConnectivityManager cm = (ConnectivityManager) Agora.getContext().getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		
	}


	private void updateServer(final QueryItem qItem){
		if (connected) {
			AsyncHttpClient client = new AsyncHttpClient();
			client.post(Agora.getContext(), qItem.getURI(), qItem.getBody(), "application/json", new AsyncHttpResponseHandler() {
				@Override
			    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
			        // called when response HTTP status is "200 OK"
					Log.i("SERVER UPDATED", "Update server method success.");
			    }

			    @Override
			    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
			        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
			    	offlineQueue.addToQueue(qItem);
			    }
			});
			
		} else {
			offlineQueue.addToQueue(qItem);
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
		return getQuestions(DOMAIN, INDEXNAME, TYPENAME, requestBody, endPoint);
	}
	
	public List<Question> getQuestions(String domain, String indexName, String typeName, String requestBody, String endPoint) throws UnsupportedEncodingException {
		AsyncHttpClient client = new AsyncHttpClient();
		StringEntity stringEntityBody = new StringEntity(requestBody);
		final List<Question> questionList = new ArrayList<Question>();
		client.post(Agora.getContext(), domain + indexName + typeName + 
						endPoint, 
						stringEntityBody,
						"application/json",
						new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
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
				    	}
				    //QuestionController.getInstance().update();
				} catch (JSONException e) {
					e.printStackTrace();
				}	
			}
        });
		return null;
	}
	
	
	public List<Question> searchQuestions(String query) throws UnsupportedEncodingException {	
		String requestBody = "{" +
			    "\"query\": {" +
			        "\"multi_match\": {" +
			           "\"query\":" + 
			           query +
			           "\"type\": \"most_fields\"," +
			           "\"fields\": [" +
			           "\"title\", \"body\"" +
			           "]}}}";
		String endPoint = "_search";
		return getQuestions(DOMAIN, INDEXNAME, TYPENAME, requestBody, endPoint);
	}
	
	public Answer getAnswerById(Long answerID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Question> getQuestionById(Long id) throws UnsupportedEncodingException {
		// assuming the question view by default sorts by date
		String requestBody = "{" +
			    "\"query\": {" +
	        	"\"match\": {" +
	           		"\"_id\":" + id.toString() +
	        	"}}}";
		String endPoint = "_search";
		List<Question> result = getQuestions(DOMAIN, INDEXNAME, TYPENAME, requestBody, endPoint);
		if (result.size() == 0){
			return null;
		} else if (result.size() > 1){
			Log.e("AGORA", "Returned array in getQuestionByID is > 1");
		}
		return getQuestions(DOMAIN, INDEXNAME, TYPENAME, requestBody, endPoint);
	}
	
	@Override	
	public boolean pushQuestion(Question q) throws UnsupportedEncodingException {		
		Gson gson = new Gson();
		String questionSerialized = gson.toJson(q);
		String endpoint = Long.toString(q.getID());
		return push(DOMAIN, INDEXNAME, TYPENAME, questionSerialized, endpoint);
	}
	
	public boolean pushAnswer(Answer a, Long qID) throws UnsupportedEncodingException {		
		Question q = CacheDataManager.getInstance().getQuestionById(qID);
		Gson gson = new Gson();
		String answerSerialized = gson.toJson(q.getAnswers());
		answerSerialized = "{" +
				"\"doc\": { " +
				"\"answers\":" + answerSerialized + 
					"}" +
				"}";
		String endpoint = Long.toString(qID) + "/" + "_update";
		return push(DOMAIN, INDEXNAME, TYPENAME, answerSerialized, endpoint);
	}

	public boolean pushComment(Comment c, Long qID, Long aID) throws UnsupportedEncodingException {
		String endpoint = Long.toString(qID) + "/" + "_update";
		Question q = CacheDataManager.getInstance().getQuestionById(qID);
		Gson gson = new Gson();
		if (aID == null) {
			String commentSerialized = gson.toJson(q.getComments());
			commentSerialized = "{" +
					"\"doc\": { " +
					"\"comments\":" + commentSerialized + 
						"}" +
					"}";
			return push(DOMAIN, INDEXNAME, TYPENAME, commentSerialized, endpoint);

		} else {
			// if qid is not null re-upload the answer array
			String answerSerialized = gson.toJson(q.getAnswers());
			answerSerialized = "{" +
					"\"doc\": { " +
					"\"answers\":" + answerSerialized + 
						"}" +
					"}";
			return push(DOMAIN, INDEXNAME, TYPENAME, answerSerialized, endpoint);	
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
		String endpoint = Long.toString(qID) + "/" + "_update";
		return push(DOMAIN, INDEXNAME, TYPENAME, upVoteSerialized, endpoint);
	}
	
	
	public boolean push(String domain, String indexName, String typeName, String body, String endpoint) throws UnsupportedEncodingException{
		StringEntity stringEntityBody = new StringEntity(body);
		String URI = domain + indexName + typeName  + endpoint;
		QueryItem queryItem = new QueryItem(stringEntityBody, URI, RequestType.POST);
		updateServer(queryItem);
		return false;
	}
}

