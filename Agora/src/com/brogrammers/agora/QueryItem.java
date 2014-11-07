package com.brogrammers.agora;

import org.apache.http.entity.StringEntity;

import com.loopj.android.http.RequestParams;

/**
 * QuryItem contains all the required information to make a server request. THis includes
 * The server address, the body of the request, and the request type.
 * 
 * @author Group 02
 *
 */
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
