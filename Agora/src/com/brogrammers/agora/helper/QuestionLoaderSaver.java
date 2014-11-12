package com.brogrammers.agora.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.model.Question;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

/**
 * This is a helper class, responsible for the serialization of cached Question
 * objects. Questions are serialized as JSON strings saved in a
 * SharedPreferences file. The CacheDataManager uses it to persist its cache.
 * 
 * @author Group02
 * 
 */
public class QuestionLoaderSaver {
	private String questionPrefsFileName;

	/**
	 * Uses default SharedPreferences file
	 */
	public QuestionLoaderSaver() {
		questionPrefsFileName = "QUESTION";
	}

	/**
	 * Uses a specified SharedPreferences file.
	 * 
	 * @param file the name of the SharedPreferences file to use
	 */
	public QuestionLoaderSaver(String file) {
		questionPrefsFileName = file;
	}

	/**
	 * Saves a question
	 * 
	 * @param q
	 *            the Question to be serialized to the SharedPreferences file
	 */
	public void saveQuestion(Question q) {
		Log.e("QLS",
				"saveQuestion answerCount = "
						+ Integer.toString(q.countAnswers()));
		String jsonQuestion = (new Gson()).toJson(q);
		SharedPreferences prefsFile = Agora.getContext().getSharedPreferences(
				questionPrefsFileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefsFile.edit();
		editor.putString(q.getID().toString(), jsonQuestion);
		editor.commit();
	}

	/**
	 * Loads all questions stored in the SharedPreferences file
	 * @return a List of the questions
	 */
	public List<Question> loadQuestions() {
		List<Question> qList = new ArrayList<Question>();
		SharedPreferences prefsFile = Agora.getContext().getSharedPreferences(
				questionPrefsFileName, Context.MODE_PRIVATE);
		Map<String, String> prefsMap = (Map<String, String>) prefsFile.getAll();

		for (String jsonQuestion : prefsMap.values()) {
			Question q = (new Gson()).fromJson(jsonQuestion,
					new TypeToken<Question>() {
					}.getType());
			Log.e("QLS",
					"loadQuestions answerCount = "
							+ Integer.toString(q.countAnswers()));

			qList.add(q);
		}

		return qList;
	}

	public void clearAll() {
		SharedPreferences prefsFile = Agora.getContext().getSharedPreferences(
				questionPrefsFileName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefsFile.edit();
		editor.clear();
		editor.commit();
	}
}
