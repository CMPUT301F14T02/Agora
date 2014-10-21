package com.brogrammers.agora;

import java.util.List;
import java.util.Queue;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OfflineQueue {
	static private OfflineQueue self = null;
	// list of commands to be run on the server
	public Queue<QueueItem> updateQueue;
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
	
	// TO-DO deal with setting context.
	public OfflineQueue(){
		// load serialized commands to be run.
		deserializeQueue();
	}
	
	public static void addToQueue(QueueItem queueItem){
		updateQueue.add(queueItem);
		serializeQueue();
	}
	
	public QueueItem peekAtQueue(){
		return updateQueue.peek();
	}
	
	public void removeFromQueue(){
		updateQueue.remove();
		serializeQueue();
	}
	
	public void serializeQueue(){
		Gson gson = new Gson();
		String serializedQueue = gson.toJson(updateQueue);
		SharedPreferences sp = Agora.getContext().getSharedPreferences(dataFile, 0);
		SharedPreferences.Editor spEditor = sp.edit();
		spEditor.putString(queueKeyName, serializedQueue);
		spEditor.commit();
	}
	
	// return desiarilized queue
	public void deserializeQueue(){
		Gson gson = new Gson();
		SharedPreferences sp = Agora.getContext().getSharedPreferences(dataFile, 0);
        String savedQueue = sp.getString(queueKeyName, "NO_DATA_TO_READ");
        if (savedQueue != "NO_DATA_TO_READ"){
        	// recreate the queue object
        	updateQueue = gson.fromJson(savedQueue, new TypeToken<Queue<String>>(){}.getType());
        }		
	}

}
