package com.brogrammers.agora;


/**
 * Observer interface provides the implementation methods to update
 * our object that uses observers. E.g. update the controller when it 
 * fetches data from ES server
 *  
 * @author Group02
 *
 */

public interface Observer {
	public void update(); 
}
