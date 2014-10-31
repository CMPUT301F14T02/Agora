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
	
	@Override
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
		return pushQuestion(DOMAIN, INDEXNAME, TYPENAME, q);
	}
	
	public boolean pushQuestion(String domain, String indexName, String typeName, Question q) throws UnsupportedEncodingException {		
		Gson gson = new Gson();
		String questionSerialized = gson.toJson(q);
		StringEntity stringEntityBody = new StringEntity(questionSerialized);
		String URI = domain + indexName+ typeName + Long.toString(q.getID());
		QueryItem queryItem = new QueryItem(stringEntityBody, URI, RequestType.POST);
		updateServer(queryItem);
		return false;
	}

	
	public boolean pushAnswer(Answer a, Long qID) throws UnsupportedEncodingException {		
		return pushAnswer(DOMAIN, INDEXNAME, TYPENAME, a, qID);
	}
	

	public boolean pushAnswer(String domain, String indexName, String typeName, Answer a, Long qID) throws UnsupportedEncodingException {
		List<Question> q = CacheDataManager.getInstance().getQuestionById(qID);
		Gson gson = new Gson();
		String answerSerialized = gson.toJson(q.get(0).getAnswers());
		answerSerialized = "{" +
				"\"doc\": { " +
				"\"answers\":" + answerSerialized + 
					"}" +
				"}";
		StringEntity stringEntityAnswer = new StringEntity(answerSerialized);
		String test = stringEntityAnswer.toString();
		String URI = domain + indexName + typeName  + Long.toString(qID) + "/" + "_update";
		QueryItem queryItem = new QueryItem(stringEntityAnswer, URI, RequestType.POST);
		updateServer(queryItem);
		return false;
	}

	public boolean pushComment(Comment c, Long qID, Long aID) throws UnsupportedEncodingException {
		return pushComment(DOMAIN, INDEXNAME, TYPENAME, c, qID, aID);
	}

	public boolean pushComment(String domain, String indexName, String typeName, Comment c, Long qID, Long aID) throws UnsupportedEncodingException {
		Question q = CacheDataManager.getInstance().questionCache.get(qID);
		Gson gson = new Gson();
		if (aID == null) {
			String commentSerialized = gson.toJson(q.getComments());
			commentSerialized = "{" +
					"\"doc\": { " +
					"\"comments\":" + commentSerialized + 
						"}" +
					"}";
			StringEntity stringEntityComments = new StringEntity(commentSerialized);
			String URI = domain + indexName+ typeName  + Long.toString(qID) + "/" + "_update";
			String test = commentSerialized.toString();
			QueryItem queryItem = new QueryItem(stringEntityComments, URI, RequestType.POST);
			updateServer(queryItem);
		} else {
			// if qid is not null re-upload the answer array
			String answerSerialized = gson.toJson(q.getAnswers());
			// add server syntax
			answerSerialized = "{" +
					"\"doc\": { " +
					"\"answers\":" + answerSerialized + 
						"}" +
					"}";
			StringEntity stringEntityAnswer = new StringEntity(answerSerialized);
			String URI = domain + indexName+ typeName  + Long.toString(qID) + "/" + "_update";
			QueryItem queryItem = new QueryItem(stringEntityAnswer, URI, RequestType.POST);
			updateServer(queryItem);
		}
	
		return false;
	}
	
}

