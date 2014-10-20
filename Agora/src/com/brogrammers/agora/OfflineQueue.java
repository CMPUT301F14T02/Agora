package com.brogrammers.agora;

import java.util.List;
import java.util.Queue;

import android.content.SharedPreferences;

import com.google.gson.Gson;

public class OfflineQueue {
	static private OfflineQueue self = null;
	// list of commands to be run on the server
	public Queue<String> updateQueue;
	// name of queue data file
	String dataFile = "QUEUE_DATA_FILE";
	// name of key for queue in data file
	String queueKeyName = "QUEUE_KEY_NAME";
	
	static public OfflineQueue getOfflineQueue() {
		if (self == null) {
			self = new OfflineQueue();
		}
		return self;
	}
	
	public OfflineQueue(){
		updateQueue = deserializeQueue();
	}
	
	public void addToQueue(String queryString){
		updateQueue.add(queryString);
		serializeQueue();
	}
	
	public String peekAtQueue(){
		return updateQueue.peek();
	}
	
	public void removeFromQueue(){
		updateQueue.remove();
		serializeQueue();
	}
	
	public void serializeQueue(){
		Gson gson = new Gson();
		String serializedQueue = gson.toJson(updateQueue);
		SharedPreferences sp = context.getSharedPrefernces(dataFile, 0);
		SharedPreferences.Editor spEditor = sp.edit();
		spEditor.putString(queueKeyName, serializedQueue);
		spEditor.commit();
	}
	
	// return desiarilized queue
	public Queue<String> deserializeQueue(){
		
		return updateQueue;
	}
}
