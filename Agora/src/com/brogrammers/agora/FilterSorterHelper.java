package com.brogrammers.agora;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FilterSorterHelper {

	public ArrayList<Question> filterOutImages(ArrayList<Question> imagelist) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Question> filterOutNoImages(ArrayList<Question> plainlist) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Question> sortByUpvote(List<Question> questions) {
		Collections.sort(questions, new Comparator<Question>() {
		    public int compare(Question m1, Question m2) {
		        return ((Integer) m1.getRating()).compareTo(m2.getRating());
		    }
		});
		return questions;
	}

	public ArrayList<Question> sortByDate(ArrayList<Question> questions) {
		Collections.sort(questions, new Comparator<Question>() {
		    public int compare(Question m1, Question m2) {
		        return m1.getDate().compareTo(m2.getDate());
		    }
		});
		return questions;
	}

}
