package com.brogrammers.agora.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.brogrammers.agora.data.DeviceUser;
import com.brogrammers.agora.model.Question;
/**
 * A helper class to help the views in sorting question lists
 * 
 * @author Group2
 *
 */
public class QuestionSorter {
	static public boolean descending = true;
	static public boolean byUpvote = true;
	
	public List<Question> sort(List<Question> questions) {
		if (byUpvote) {
			return sortByUpvote(questions);
		} else {
			return sortByDate(questions);
		}
	}

	/**
	 * Sorts a question list by number of upvotes
	 * @param questions list
	 * @return sorted questions list
	 */
	public List<Question> sortByUpvote(List<Question> questions) {
		if (descending) {
			Collections.sort(questions, new Comparator<Question>() {
			    public int compare(Question m1, Question m2) {
			        return ((Integer) m2.getRating()).compareTo(m1.getRating());
			    }
			});	
		} else {
			Collections.sort(questions, new Comparator<Question>() {
			    public int compare(Question m1, Question m2) {
			        return ((Integer) m1.getRating()).compareTo(m2.getRating());
			    }
			});
		}
		
		return questions;
	}

	/**
	 * Sorts a questions list by date
	 * @param questions list
	 * @return sorted questions list
	 */
	public List<Question> sortByDate(List<Question> questions) {
		if (descending) {
			Collections.sort(questions, new Comparator<Question>() {
			    public int compare(Question m1, Question m2) {
			        return m2.getDate().compareTo(m1.getDate());
			    }
			});
		} else {
			Collections.sort(questions, new Comparator<Question>() {
			    public int compare(Question m1, Question m2) {
			        return m1.getDate().compareTo(m2.getDate());
			    }
			});
		}

		return questions;
	}

}
