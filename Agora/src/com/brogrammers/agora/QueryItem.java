package com.brogrammers.agora;

import org.apache.http.entity.StringEntity;

import com.loopj.android.http.RequestParams;

public class QueryItem {
	enum RequestType{POST, GET}
	RequestType requestType;
	StringEntity body;
	String URI;
	
	public QueryItem(StringEntity body, String URI, RequestType requestType){
		this.body = body;
		this.URI = URI;
		this.requestType = requestType;
	}

	public StringEntity getBody() {
		return this.body;
	}

	public String getURI() {
		return URI;
	}
	
	public RequestType getRequestType(){
		return requestType;
	}
}
