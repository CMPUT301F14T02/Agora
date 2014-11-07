
package com.brogrammers.agora;

import java.util.List;
import java.util.Queue;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * The OfflineQueue holds query items that need to be sent to the server but were not able to
 * be sent because of connections issues.
 * 
 * @author Group 02
 *
 */
public class OfflineQueue {
	static private OfflineQueue self = null;
	// list of commands to be run on the server
	public Queue<QueryItem> updateQueue;
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
	
	public void addToQueue(QueryItem queueItem){
		updateQueue.add(queueItem);
		serializeQueue();
	}
	
	public QueryItem peekAtQueue(){
		return updateQueue.peek();
	}
	
	public void removeFromQueue(){
		updateQueue.remove();
		serializeQueue();
	}

	/**
	 * Saves query items to the device for eventual processing.
	 * <p>
	 * When a connection can't be made to the server the queryItem object is serialized.
	 * The items can then be retrieved when a connection is available for processing.
	 * 
	 */	
	public void serializeQueue(){
		Gson gson = new Gson();
		String serializedQueue = gson.toJson(updateQueue);
		SharedPreferences sp = Agora.getContext().getSharedPreferences(dataFile, 0);
		SharedPreferences.Editor spEditor = sp.edit();
		spEditor.putString(queueKeyName, serializedQueue);
		spEditor.commit();
	}

	/**
	 * Retrieves save queryItems for processing.
	 * <p>
	 * When a connection can't be made to the server the queryItem object is serialized.
	 * This method deserializes the query for processing.
	 * 
	 */	
	public void deserializeQueue(){
        // return desiarilized queue
		Gson gson = new Gson();
		SharedPreferences sp = Agora.getContext().getSharedPreferences(dataFile, 0);
        String savedQueue = sp.getString(queueKeyName, "NO_DATA_TO_READ");
        if (savedQueue != "NO_DATA_TO_READ"){
        	// recreate the queue object
        	updateQueue = gson.fromJson(savedQueue, new TypeToken<Queue<String>>(){}.getType());
        }		
	}

}
