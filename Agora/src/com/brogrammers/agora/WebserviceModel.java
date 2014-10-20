package com.brogrammers.agora;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
	
	public List<QuestionPreview> getQuestions(){
		// This function should compare the id's and versions of the list 
		// of questions held in the cache. If the id's from the get question
		// items aren't in the cache, download them and insert them into the cache
		// if the id's are present but the version numbers are different, theres
		// been an update to the question and we'll have to redownload the changes
		
		// assuming the question view by default sorts by date
		int responseCode = HttpRequest.get(DOMAIN + INDEXNAME + TYPENAME).code();
		// want to sort based on most upvoted or newest first?
		
		
		// TO-DO return list of question previews objects.
		// Create a query that only returns required list view info.
		
		
		

	}
		
	
	// the following methods should be called when the user
	// makes local changes that need to be pushed to server.
	
}
