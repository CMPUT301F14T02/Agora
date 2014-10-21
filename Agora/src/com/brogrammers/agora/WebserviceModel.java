package com.brogrammers.agora;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

public class WebserviceModel {
	// domain
	private String DOMAIN = "http://cmput301.softwareprocess.es:8080/testing/";
	// name of the ES database/index
	private String INDEXNAME = "agora/";
	// name of the ES table/type 
	private String TYPENAME = "question/";
	// connected status
	public boolean connected;
	// Queue of statements that need to be run on
	// the server
	private OfflineQueue offlineQueue;
	
	// TODO: Register a broadcast receiver for connectivity status.
	
	private static WebserviceModel webserviceModel;
			
	public static WebserviceModel getWebserviceModel(){
		if (webserviceModel == null) {
			webserviceModel = new WebserviceModel();
		}
		return webserviceModel;
	}
	
	private WebserviceModel(){
		// get connection status
		// after creation connectivity status will be monitored by a
		// broadcast receiver.
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	public void updateServer(String queryString){
		
	}
	
	public List<QuestionPreview> getQuestionPreviews(){
		// assuming the question view by default sorts by date
		AsyncHttpClient client = new AsyncHttpClient();
		// querry to return question preview information
		// elastic search queries must use double quotes, hence the mess.
		RequestParams params = new RequestParams();
		params.put("sort", "[{ \"date\" :{\"order\":\"desc\"}}]");
		params.put("fields", "[\"date\", \"ID\", \"title\", \"rating\", \"answerCount\", \"author\"]");
		params.put("query", "{\"match_all\" : {}}");
				
		client.post(DOMAIN + INDEXNAME + TYPENAME + "_search", params, new JsonHttpResponseHandler() {
		    @Override
		    public void onStart() {
		        // called before request is started
		    	// TODO: Think about creating the empty list here instead of
		    	// returning it.
		    }

		    @Override
		    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		        // called when response HTTP status is "200 OK"
		    	JSONArray questionPreviews = null;
				try {
					questionPreviews = (JSONArray) ((JSONObject) response.get("hits")).get("hits");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	List<QuestionPreview> questionPreviewList = new ArrayList<QuestionPreview>();
				for (int i = 0; i < questionPreviews.length(); i++) {
		    		try {
						JSONObject questionObject = questionPreviews.getJSONObject(i);
						questionObject = questionObject.getJSONObject("fields");
						String title = (String) ((JSONArray) questionObject.getJSONArray("title")).get(0);
						int rating = Integer.parseInt((String) 
											((JSONArray) questionObject.getJSONArray("rating")).get(0));
						int answerCount = Integer.parseInt((String) 
											((JSONArray) questionObject.getJSONArray("answerCount")).get(0));
						long date = Long.parseLong((String) 
											((JSONArray) questionObject.getJSONArray("date")).get(0));
						String author = (String) 
											((JSONArray) questionObject.getJSONArray("author")).get(0);
						long ID = Long.parseLong((String) 
											((JSONArray) questionObject.getJSONArray("ID")).get(0));
						QuestionPreview qp = new QuestionPreview(
											title, rating, answerCount, date, new Author(author), ID);
						questionPreviewList.add(qp);
		    		} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	// assuming question previews are cached in the localcachemodel
		    	// Makes more sense than caching in view or not caching at all
		    	LocalCacheModel.getLocalCacheModel().setQuestionPreviewList(questionPreviewList);   		
		    	}
		    }
		    
		    @Override
		    public void onRetry(int retryNo) {
		        // called when request is retried
			}
		    
		    @Override
		    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
		        Log.w("Question Preview Load Failure", "onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received", throwable);
		    }
		    
		});
		// return a dummy empty list to the caller
		List<QuestionPreview> qpList = new ArrayList<QuestionPreview>();
		return qpList;	
	}
	
}
