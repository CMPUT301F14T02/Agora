package com.brogrammers.agora;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

public class QuestionLoaderSaver {
	private String questionPrefsFileName;
	
	public QuestionLoaderSaver() { 
		questionPrefsFileName = "QUESTION";
	}
	public QuestionLoaderSaver(String file) {
		questionPrefsFileName = file;
	}

	public void saveQuestion(Question q) {
		String jsonQuestion = (new Gson()).toJson(q);
		SharedPreferences prefsFile = 
			Agora.getContext().getSharedPreferences(questionPrefsFileName, Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = prefsFile.edit();
		editor.putString(q.getID().toString(), jsonQuestion);
		editor.commit();
	}
	
	public List<Question> loadQuestions() {
		List<Question> qList = new ArrayList<Question>();
		SharedPreferences prefsFile = 
			Agora.getContext().getSharedPreferences(questionPrefsFileName, Context.MODE_PRIVATE);
		Map<String, String> prefsMap = (Map<String, String>) prefsFile.getAll();
		
		for (String jsonQuestion : prefsMap.values()) {
			Question q = (new Gson()).fromJson(jsonQuestion, new TypeToken<Question>(){}.getType());
			qList.add(q);
		}
		
		return qList;
	}

}
