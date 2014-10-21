package com.brogrammers.agora;

import com.loopj.android.http.RequestParams;

public class QueryItem {
	enum RequestType{POST, GET}
	RequestType requestType;
	RequestParams requestParams;
	String URI;
	
	public QueueItem(RequestParams requestParams, String URI, RequestType requestType){
		this.requestParams = requestParams;
		this.URI = URI;
		this.requestType = requestType;
	}

	public RequestParams getRequestParams() {
		return requestParams;
	}

	public String getURI() {
		return URI;
	}
	
	public RequestType getRequestType(){
		return requestType;
	}
}
