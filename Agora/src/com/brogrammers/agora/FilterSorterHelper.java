package com.brogrammers.agora;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * A helper class to help the views in sorting and filtering question lists
 * 
 * @author Group2
 *
 */
public class FilterSorterHelper {
	
	/**
	 * Filters out questions that have images from a questions list
	 * @param list of questions
	 * @return list of questions that don't have images
	 */
	public ArrayList<Question> filterOutImages(ArrayList<Question> list) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Filters out questions that DONT have images from a questions list
	 * @param list of questions
	 * @return list of questions that have images
	 */
	public ArrayList<Question> filterOutNoImages(ArrayList<Question> list) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Sorts a question list by number of upvotes
	 * @param questions list
	 * @return sorted questions list
	 */
	public List<Question> sortByUpvote(List<Question> questions) {
		Collections.sort(questions, new Comparator<Question>() {
		    public int compare(Question m1, Question m2) {
		        return ((Integer) m1.getRating()).compareTo(m2.getRating());
		    }
		});
		return questions;
	}

	/**
	 * Sorts a questions list by date
	 * @param questions list
	 * @return sorted questions list
	 */
	public ArrayList<Question> sortByDate(ArrayList<Question> questions) {
		Collections.sort(questions, new Comparator<Question>() {
		    public int compare(Question m1, Question m2) {
		        return m1.getDate().compareTo(m2.getDate());
		    }
		});
		return questions;
	}

}
