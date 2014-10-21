package com.brogrammers.agora;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

public class QuestionLoaderSaver {
	private static final String questionPrefsFileName = "QUESTION";
	
	public static void saveQuestion(Question q) {
		 SharedPreferences prefsFile = 
				 Agora.getContext().getSharedPreferences(questionPrefsFileName, Context.MODE_PRIVATE);
		 SharedPreferences.Editor editor = prefsFile.edit();
		 String serializedQuestion = serializeQuestion(q); 
		 editor.putString(q.getID().toString(), serializedQuestion);
		 editor.commit();
	}
	
	public static List<Question> loadQuestions() {
		// TODO
		return null;
	}
	
	 /* 
	  * Serialization solution below adapted from Matthias @ stackoverflow:
	  * http://stackoverflow.com/questions/1636623/how-do-i-preserve-a-complex-object-across-activity-restarts
	  */
	 // Question object --> Base64-encoded String
	 private static String serializeQuestion (Question q) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
		try {
			// Use an ObjectOutputStream to write the to-do to the ByteArrayOutputStream
			new ObjectOutputStream(baos).writeObject(q);  
			// Use the ByteArrayOutputStream to convert the serialized todo to raw bytes.
			byte[] raw_data = baos.toByteArray(); 
			baos.close();
			// return a Base64-encoded String representing the serialized todo.
			return Base64.encodeToString(raw_data, Base64.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	// Base64-encoded String --> Question object (the inverse of this.serializeTodo())
	private Question deserializeTodo (String str){
		byte[] raw_data = Base64.decode(str, Base64.DEFAULT);
		ByteArrayInputStream bais = new ByteArrayInputStream(raw_data);
	
		try {
			return (Question) new ObjectInputStream(bais).readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
}
