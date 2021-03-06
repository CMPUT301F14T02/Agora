package com.brogrammers.agora.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.helper.QuestionLoaderSaver;
import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.Comment;
import com.brogrammers.agora.model.Question;

import android.util.Log;
import android.widget.Toast;

/**
 * A Cache Data Manager to push and pull question, answer, and comment objects
 * to the device. This allows the device to retrieve these data objects while
 * the device is offline.
 * 
 * This class allows the user to: i. Store questions, answers and comments to be
 * locally cached and viewed later (US 17) ii. Allow the user to view favourite
 * questions/answers and their replies for view later (US 19) iii. Author
 * questions, replies, and answers offline (US 20)
 * 
 * @author Group02
 * 
 */

public class CacheDataManager {

	static private CacheDataManager self;

	protected TreeMap<Long, Question> questionCache; // for getQuestionByID()
	// protected List<QuestionPreview> questionPreviewList; // for
	// getQuestions()

	QuestionLoaderSaver qls;

	/* Constructors */
	protected CacheDataManager() {
		questionCache = new TreeMap<Long, Question>();
		// load saved questions from file
		qls = new QuestionLoaderSaver();
		for (Question q : qls.loadQuestions()) {
			questionCache.put(q.getID(), q);

		}
	}

	protected CacheDataManager(String fileName) {
		questionCache = new TreeMap<Long, Question>();
		// load saved questions from file
		qls = new QuestionLoaderSaver(fileName);
		for (Question q : qls.loadQuestions()) {
			questionCache.put(q.getID(), q);
		}
	}

	static public CacheDataManager getInstance() {
		if (self == null) {
			self = new CacheDataManager();
		}
		return self;
	}

	/**
	 * Gets the entire list of questions from the cache
	 * 
	 * @return a list of questions retrieved from the cache
	 */

	public List<Question> getQuestions() {
		// Log.e("CACHE", "size of questionCache is " + questionCache.size());
		return new ArrayList<Question>(questionCache.values());
	}

	/**
	 * Returns a question object from the cache by the question ID
	 * 
	 * @param id
	 *            the unique question id
	 * @return Question the question object we retrieve
	 */
	// Warning: can return null
	public Question getQuestionById(Long id) {
		Question q = questionCache.get(id);
		// if (q != null) {
		// if (q.hasImage()) {
		// if (q.getImage() == null) {
		// Toast.makeText(Agora.getContext(),
		// "Cached image detected but image is null", 0).show();
		// } else {
		// Toast.makeText(Agora.getContext(),
		// "Cached image detected: "+q.getImage().length, 0).show();
		// }
		// }
		// }
		return q;
	}

	/**
	 * Adds a question to the cache and saves the question object to file
	 * 
	 * @param q
	 *            the question object we are adding to the cache
	 */
	public void pushQuestion(Question q) {
		questionCache.put(q.getID(), q);
		qls.saveQuestion(q);
	}

	/**
	 * Adds questions to the cache without saving them to file. Used for
	 * smoother loading of views.
	 * 
	 * @param qList
	 *            A list of questions to be cached
	 */
	public void rememberQuestions(List<Question> qList) {
		for (Question q : qList) {
			rememberQuestion(q);
		}
	}

	/**
	 * Add a question to the cache without saving it to file. Used for smoother
	 * loading of views.
	 * 
	 * @param q
	 *            The question to be cached
	 */
	public void rememberQuestion(Question q) {
		// If the incoming question claims to have an image but has no image
		// (probably because it's coming from MainActivity) then don't overwrite
		// the cached image with null.
		if (q.hasImage() && q.getImage() == null) {
			Question prev_q = questionCache.get(q.getID());
			if (prev_q != null && prev_q.getImage() != null) {
				q.setImage(prev_q.getImage());
			}
		}
		questionCache.put(q.getID(), q);
	}

	/**
	 * Adds answer to an answer to a question in the cache
	 * 
	 * @param a
	 *            the answer object we are adding to the cache
	 * @param qID
	 *            the unique question id where the answer is added to
	 */
	public void pushAnswer(Answer a, Long qID) {
		Question q = questionCache.get(qID);
		q.addAnswer(a);
		pushQuestion(q);
	}

	/**
	 * Allows you to add a comment to an answer or a question in the cache
	 * Passes in either a qID or an aID, and sets the other field to null to
	 * know where the comment goes
	 * 
	 * @param c
	 *            the comment object we are adding to the cache
	 * @param qID
	 *            the unique question id if we are commenting on the question,
	 *            or null if commenting answer
	 * @param aID
	 *            the unique answer id if we are commenting the answer, or null
	 *            if commenting question
	 */
	public void pushComment(Comment c, Long qID, Long aID) {
		Question q = questionCache.get(qID);
		if (aID != null) {
			Answer a = q.getAnswerByID(aID);
			a.addComment(c);
		} else {
			q.addComment(c);
		}
		pushQuestion(q);
	}

	/**
	 * Clears both the in-memory cache of questions, as well as the on-disk
	 * cache of questions.
	 */
	public void clearCache() {
		questionCache.clear();
		qls.clearAll();
	}
}