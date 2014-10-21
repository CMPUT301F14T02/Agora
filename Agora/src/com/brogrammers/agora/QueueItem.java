package com.brogrammers.agora;

public class QueueItem {
	String body;
	String URI;
	
	public QueueItem(String b, String URI){
		this.body = b;
		this.URI = URI;
	}

	public String getBody() {
		return body;
	}

	public String getURI() {
		return URI;
	}
}
