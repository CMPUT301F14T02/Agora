package com.brogrammers.agora;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

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

	
	private ESDataManager(){
		// get connection status
		// after creation connectivity status will be monitored by a
		// broadcast receiver.
		ConnectivityManager cm = (ConnectivityManager) Agora.getContext().getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}


	private void updateServer(final QueryItem qItem){
		if (connected){
			AsyncHttpClient client = new AsyncHttpClient();
			// TODO: check for request type, don't assume post 
			// qItem.requestType
			client.post(Agora.getContext(), qItem.getURI(), qItem.getBody(), "application/json", new AsyncHttpResponseHandler() {
				@Override
			    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
			        // called when response HTTP status is "200 OK"
					Log.i("SERVER UPDATED", "Update server method success.");
			    }

			    @Override
			    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
			        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
					//TODO: Switch threads.
			    	offlineQueue.addToQueue(qItem);
			    }
			});
			
		} else {
			offlineQueue.addToQueue(qItem);
		}
		
	}
	
	public List<Question> getQuestions() throws UnsupportedEncodingException{
		// assuming the question view by default sorts by date
		AsyncHttpClient client = new AsyncHttpClient();
		String requestBody = "{" +
			    "\"query\": {\"match_all\": {}}," +
			    "\"sort\": [" +
			    	"{" +
			    	"\"date\": {" +
			    		"\"order\": \"desc\"" + 
			    	"}"+
	       		"}" + "]" + "}";
		StringEntity stringEntityBody;
		stringEntityBody = new StringEntity(requestBody);
		final List<Question> questionList = new ArrayList<Question>();
		client.post(Agora.getContext(), DOMAIN + INDEXNAME + TYPENAME + 
						"_search", 
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
			        
				    //QuestionController.getInstance().updateQuestionList(questionList);
				} catch (JSONException e) {
					e.printStackTrace();
				}	
			}
        });
		return null;
	}
	
	
	public List<Question> searchQuestions(String query) throws UnsupportedEncodingException{
		AsyncHttpClient client = new AsyncHttpClient();
		String requestBody = "{\"query\":{\"match_all\":{}}}";
		StringEntity stringEntityBody = new StringEntity(requestBody);
		List<Question> questionList = new ArrayList<Question>();
		client.post(Agora.getContext(), DOMAIN + INDEXNAME + TYPENAME + "_search", 
								stringEntityBody, "application/json", new JsonHttpResponseHandler() {

		    @Override
		    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		    	String questionResponseList = null;
				try {
					// response should be array of question objects
					questionResponseList = (String) ((JSONObject) response.get("hits")).get("hits");
				} catch (JSONException e) {
					e.printStackTrace();
				}
		    	List<Question> questionList = new ArrayList<Question>();
				Gson gson = new Gson();
		    	List<Question> q = gson.fromJson(questionResponseList, new TypeToken<List<Question>>(){}.getType());
		    	CacheDataManager.getInstance().questionCache = (TreeMap<Long, Question>) q;
		    }
		    
		    
		    @Override
		    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
		        Log.w("Question Preview Load Failure", "onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received", throwable);
		    }
		    
		});
		return null;
	}



	public Answer getAnswerById(Long answerID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Question getQuestionById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean pushQuestion(Question q) throws UnsupportedEncodingException {		
		Gson gson = new Gson();
		String questionSerialized = gson.toJson(q);
		StringEntity stringEntityBody = new StringEntity(questionSerialized);
		String URI = DOMAIN + INDEXNAME + TYPENAME + Long.toString(q.getID());
		QueryItem queryItem = new QueryItem(stringEntityBody, URI, RequestType.POST);
		updateServer(queryItem);
		return false;
	}

	@Override
	public boolean pushAnswer(Answer a, Long qID) throws UnsupportedEncodingException {
		Question q = CacheDataManager.getInstance().questionCache.get(qID);
		Gson gson = new Gson();
		String answerSerialized = gson.toJson(q.getAnswers());
		answerSerialized = "{" +
				"\"doc\": { " +
				"\"answers\":" + answerSerialized + 
					"}" +
				"}";
		StringEntity stringEntityAnswer = new StringEntity(answerSerialized);
		String test = stringEntityAnswer.toString();
		String URI = DOMAIN + INDEXNAME + TYPENAME + Long.toString(qID) + "/" + "_update";
		QueryItem queryItem = new QueryItem(stringEntityAnswer, URI, RequestType.POST);
		updateServer(queryItem);
		return false;
	}

	@Override
	public boolean pushComment(Comment c, Long qID, Long aID) throws UnsupportedEncodingException {
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
			String URI = DOMAIN + INDEXNAME + TYPENAME + Long.toString(qID) + "/" + "_update";
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
			String URI = DOMAIN + INDEXNAME + TYPENAME + Long.toString(qID) + "/" + "_update";
			QueryItem queryItem = new QueryItem(stringEntityAnswer, URI, RequestType.POST);
			updateServer(queryItem);
		}
	
		return false;
	}
	
}

