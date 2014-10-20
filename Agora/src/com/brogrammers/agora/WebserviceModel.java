package com.brogrammers.agora;

import java.util.ArrayList;
import java.util.List;
import com.loopj.android.http.*;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
	
	public List<QuestionPreview> getQuestionsPreviews(){
		// assuming the question view by default sorts by date
		AsyncHttpClient client = new AsyncHttpClient();
		// querry to return question preview information
		// elastic search queries must use double quotes, hence the mess.
		RequestParams params = new RequestParams();
		params.put("sort", "[{ \"date\" :{\"order\":\"desc\"}}]");
		params.put("fields", "[{ \"date\" :{\"order\":\"desc\"}}]");
		params.put("query", "{\"match_all\" : {}}");
				
		client.post(DOMAIN + INDEXNAME + TYPENAME + "_search", params, new AsyncHttpResponseHandler() {

		    @Override
		    public void onStart() {
		        // called before request is started
		    	// TO-DO Think about setting the list here instead of
		    	// returning it.
		    }

		    @Override
		    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
		        // called when response HTTP status is "200 OK"
		    }

		    @Override
		    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
		        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
		    }

		    @Override
		    public void onRetry(int retryNo) {
		        // called when request is retried
			}
		    
		});
		// return a dummy empty list to the caller
		List<QuestionPreview> qpList = new ArrayList<QuestionPreview>();
		return qpList;	

	}
	
}
