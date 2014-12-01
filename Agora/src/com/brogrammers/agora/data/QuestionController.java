package com.brogrammers.agora.data;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.Observer;
import com.brogrammers.agora.helper.ImageResizer;
import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.Comment;
import com.brogrammers.agora.model.Question;
import com.brogrammers.agora.model.SimpleLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.*;

import android.graphics.Bitmap;
import android.renderscript.Type;
import android.util.Log;
import android.widget.Toast;

/**
 * Controller class for directing data to and from the views and data managers.
 * 
 * @author Group02
 * 
 */
public class QuestionController {
	private static DeviceUser user;
	private static CacheDataManager cache;
	private static ESDataManager eSearch;

	static private QuestionController self = null;

	private List<Question> allQuestionList = new ArrayList<Question>();
	private List<Question> searchQuestionResults = new ArrayList<Question>();
	private List<Answer> searchAnswerResults = new ArrayList<Answer>();
	private List<Question> questionByIdList = new ArrayList<Question>();
	private List<Question> tempRemoteAllQuestionList = new ArrayList<Question>();
	private List<Question> 	tempRemoteQuestionByIdList = new ArrayList<Question>();
	private List<Question> 	addCacheList = new ArrayList<Question>();
	private List<Question> 	addFavoritesList = new ArrayList<Question>();

	private Observer observer;

	/**
	 * Singleton access method
	 * 
	 * @return
	 */
	static public QuestionController getController() {
		if (self == null) {
			self = new QuestionController();
		}
		return self;
	}

	/**
	 * Dependency-injecting initializer for testing
	 * 
	 * @param user
	 * @param cache
	 * @param eSearch
	 * @return
	 */
	static public QuestionController getController(DeviceUser user,
			CacheDataManager cache, ESDataManager eSearch) {
		if (self == null) {
			self = new QuestionController(user, cache, eSearch);
		}
		return self;
	}

	protected QuestionController() {
		user = DeviceUser.getUser();
		cache = CacheDataManager.getInstance();
		eSearch = ESDataManager.getInstance();
	}

	// Dependency injection, for testing.
	protected QuestionController(DeviceUser user_, CacheDataManager cache_,
			ESDataManager eSearch_) {
		user = user_;
		cache = cache_;
		eSearch = eSearch_;
	}

	/**
	 * Passes on a search query to the ESDataManager, and returns the matching
	 * Question objects. If there is no connection, throws an exception to
	 * notify the calling view.
	 * 
	 * @param query
	 *            A string containing the user's input to a search box
	 * @return A list of questions matching the user's query
	 */
	public List<Question> searchQuestions(String query) {
		if (eSearch.isConnected()) {
			searchQuestionResults = eSearch.searchQuestions(query);
			return searchQuestionResults;
		} else {
			// TODO: throw exception to notify view that network is out
			return null;
		}
	}
	
	public List<Answer> searchAnswers(String query) {
		if (eSearch.isConnected()) {
			searchAnswerResults = eSearch.searchAnswers(query);
			return searchAnswerResults;
		} else {
			// TODO: handle no network
			return null;
		}
	}

	/**
	 * Called when a user submits a question. Uploads the question to the
	 * ESDataManager and the CacheDataManager.
	 * 
	 * @param title
	 *            user input - Question title
	 * @param body
	 *            user input - Question body
	 * @param image
	 *            user input - an image to be uploaded with the question (null
	 *            if no image provided)
	 * @param location 
	 * @return the ID of the created question
	 */
	public Long addQuestion(String title, String body, byte[] image, boolean location) {
		Log.e("CONTROLLER", "addQuestion username="+user.getUsername());
		LocationDataManager.getInstance();
		SimpleLocation locationCoor = LocationDataManager.getLocation();
		String locationName = LocationDataManager.getLocationName();
		Question q;
		if (location){
			q = new Question(title, body, image, user.getUsername(), locationCoor, locationName);
		}
		else{
			q = new Question(title, body, image, user.getUsername());
		}
		
		cache.pushQuestion(q);
		user.addAuthoredQuestionID(q.getID());
		eSearch.pushQuestion(q);

		return q.getID(); // for testing
	}

	/**
	 * Called when a user submits an answer to an existing questions. Creates
	 * the answer object then passes it to the DataManagers to be attached to
	 * the appropriate question.
	 * 
	 * @param body
	 *            user input - Answer body
	 * @param image
	 *            user input - an image to be uploaded with the answer (null if
	 *            no image provided)
	 * @param qID
	 *            the ID of the question that the Answer is to be added to.
	 * @param location 
	 * @return the ID of the created Answer
	 * @throws UnsupportedEncodingException
	 */
	public Long addAnswer(String body, byte[] image, Long qID, boolean location)
			throws UnsupportedEncodingException {
		LocationDataManager.getInstance();
		SimpleLocation locationCoor = LocationDataManager.getLocation();
		String locationName = LocationDataManager.getLocationName();
		Answer a;
		if (location){
			a = new Answer(body, image, user.getUsername(),locationCoor, locationName);
		}else {
			a = new Answer(body, image, user.getUsername());
			
		}
		
		cache.pushAnswer(a, qID);
		eSearch.pushAnswer(a, qID, cache);

		return a.getID();
	}

	/**
	 * Called when a user submits a comment to an existing questions. Creates
	 * the comment object then passes it to the DataManagers to be attached to
	 * the appropriate question or answer.
	 * 
	 * @param body
	 *            user input - Comment body
	 * @param qID
	 *            the ID of the question that the comment will be associated
	 *            with.
	 * @param aID
	 *            null if the comment is on the question directly, otherwise the
	 *            ID of the answer being commented on.
	 */
	public void addComment(String body, Long qID, Long aID) {
		Comment c = new Comment(body);
		Question q = cache.getQuestionById(qID);
		if (aID == null) {
			q.addComment(c);
		} else {
			q.getAnswerByID(aID).addComment(c);
		}

		eSearch.pushComment(c, qID, aID, cache);
	}

	/**
	 * Called when a user upvotes a question or answer. Increments the rating
	 * count in the DataManagers.
	 * 
	 * @param qID
	 *            The ID of the question being upvoted, or the question
	 *            containing the answer being upvoted.
	 * @param aID
	 *            The ID of the answer being upvoted, or null if a question is
	 *            being upvoted.
	 * @throws UnsupportedEncodingException
	 */
	public void upvote(Long qID, Long aID) throws UnsupportedEncodingException {
		Question q = cache.getQuestionById(qID);
		if (aID == null) { // upvoting question
			q.upvote();
			cache.pushQuestion(q);
			eSearch.pushUpvote(qID, cache);
		} else { // upvoting answer
			Answer a = q.getAnswerByID(aID);
			a.upvote();
			cache.pushQuestion(q);
			eSearch.pushAnswer(a, qID, cache);
		}
	}
	

	/**
	 * If there is an active network connection, this method returns an empty
	 * list initially, which will then be populated asynchronously by the
	 * ESDataManager. If there is no network connection, the list of questions
	 * in the cache is returned.
	 * 
	 * @return The list of all questions on either the server or in the cache
	 */
	public List<Question> getAllQuestions() {
		Log.e("CONTROLLER", "getAllQuestions");
		tempRemoteAllQuestionList.clear();
		allQuestionList = cache.getQuestions();
		if (eSearch.isConnected()) {
			tempRemoteAllQuestionList = eSearch.getQuestions();
		} else {
			Toast.makeText(Agora.getContext(), 
					"Controller: not connected, getAllQuestions from cache", 0)
					.show();

		}
		return allQuestionList;
	}


	/**
	 * If there is an active network connection, this method returns an empty
	 * List which will be populated asynchronously with the single question
	 * matching the specified ID.
	 * 
	 * @param id
	 *            the ID of the question being requested
	 * @return a list which will contain the single question.
	 */
	public List<Question> getQuestionById(Long id) {
		questionByIdList = new ArrayList<Question>();
		tempRemoteQuestionByIdList.clear();
		Question q = cache.getQuestionById(id);
		if (q != null) {
			questionByIdList.add(q);
		}
		if (eSearch.isConnected()) {
			tempRemoteQuestionByIdList = eSearch.getQuestionById(id);
		} else {
			Toast.makeText(Agora.getContext(), "Error: no internet connection", 0).show();
		} 
		return questionByIdList;
	}

	public List<Question> searchQuestionsByLocation() {
		if (eSearch.isConnected()) {
			Log.wtf("CONTROLLER SEARCH", "lat/lon: " + LocationDataManager.getLocation().getLat() + LocationDataManager.getLocation().getLat());
			return eSearch.searchQuestionsByLocation(LocationDataManager.getLocation());
		} else {
			return new ArrayList<Question>();
		}
		
	}
	
	/**
	 * This method is called by a DataManager when it successfully populates a
	 * list requested by the controller. Notifies the registered observer.
	 */
	public void update() {
		if (observer != null) {
			if (tempRemoteAllQuestionList.size() > 0) {
				allQuestionList.clear();
				for (Question q : tempRemoteAllQuestionList) {
					allQuestionList.add(q);
				}
				cache.rememberQuestions(tempRemoteAllQuestionList);
				tempRemoteAllQuestionList.clear();
			}
			if (tempRemoteQuestionByIdList.size() > 0) {
				questionByIdList.clear();
				questionByIdList.add(tempRemoteQuestionByIdList.get(0));
				cache.rememberQuestions(tempRemoteQuestionByIdList);
			}
			if (addCacheList.size() > 0) {
				cache.pushQuestion(addCacheList.get(0));
				addCacheList.clear();
			}
			observer.update();
		} else {
			Toast.makeText(Agora.getContext(),
				"Controller: no observer registered!", 0).show();
		}
	}

	/**
	 * Before requesting data, views use this method to register themselves to
	 * receive a callback when the data is successfully retrieved.
	 * 
	 * @param observer a view that will request data from the controller
	 */
	public void setObserver(Observer observer) {
		this.observer = observer;
	}

	/**
	 * Adds a question to the local cache
	 * @param id the id of the question
	 */
	public void addCache(Long id) {
		user = DeviceUser.getUser();
		user.addCachedQuestionID(id);
		addCacheList = eSearch.getQuestionById(id);
	}

	/**
	 * Adds a question to the local cache and marks it as a favorite
	 * @param id the id of the question
	 */
	public void addFavorite(Long id) {
		user = DeviceUser.getUser();
		user.addFavoritedQuestionID(id);
		addCacheList = eSearch.getQuestionById(id);
	}

}
