package com.brogrammers.agora.helper;

import java.util.ArrayList;
import java.util.List;

import com.brogrammers.agora.data.DeviceUser;
import com.brogrammers.agora.model.Question;

public class QuestionFilterer {
	public static boolean filterPicture = false;
	public static boolean filterFavorite = false;
	public static boolean filterAuthor = false;
	public static boolean filterLocation = false;
	
	/**
	 * Filters out questions that have images from a questions list
	 * @param list of questions
	 * @return list of questions that don't have images
	 */
	public List<Question> filterOutImages(List<Question> list) {
		ArrayList<Question> filtered = new ArrayList<Question>();
		for (Question q : list) {
			if (q.hasImage()) {
				filtered.add(q);
			}
		}
		return filtered;
	}
	
	/**
	 * Filters out questions that have images from a questions list
	 * @param list of questions
	 * @return list of questions that don't have images
	 */
	public List<Question> filterOutFavorites(List<Question> list) {
		List<Question> filtered = new ArrayList<Question>();
		List<Long> favorites = DeviceUser.getUser().getFavoritedQuestionIDs();
		for (Question q : list) {
			if (favorites.contains(q.getID())) {
				filtered.add(q);
			}
		}
		return filtered;
	}
	
	/**
	 * Filters out questions that DONT have images from a questions list
	 * @param list of questions
	 * @return list of questions that have images
	 */
	public List<Question> filterOutNoImages(List<Question> list) {
		List<Question> filtered = new ArrayList<Question>();
		for (Question q : list) {
			if (!q.hasImage()) {
				filtered.add(q);
			}
		}
		return filtered;
	}
	
	/**
	 * Filters out questions that aren't authored by you from a questions list
	 * @param list of questions
	 * @return list of questions are authored by you
	 */
	public List<Question> filterAuthoredQuestions(List<Question> list) {
		DeviceUser user = DeviceUser.getUser();
		List<Question> filtered = new ArrayList<Question>();
		for (Question q : list) {
			if (q.getAuthor().equals(user.getUsername())) {
				filtered.add(q);
			}
		}
		return filtered;
	}
	
	public List<Question> filter(List<Question> questions) {
		if (filterPicture) {
			questions = filterOutImages(questions);
		}
		if (filterFavorite) {
			questions = filterOutFavorites(questions);
		}
		if (filterAuthor) {
			questions = filterAuthoredQuestions(questions);
		}

		return questions;
	}
}
