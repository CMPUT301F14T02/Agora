package com.brogrammers.agora.data;

import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.data.QueryItem.RequestType;
import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.Comment;
import com.brogrammers.agora.model.Question;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.entity.StringEntity;

/**
 * Elastic search networking module. Handles all server data uploads and
 * downloads required by the app.
 * 
 * @author Group 02
 * 
 */
public class ESDataManager { // implements DataManager
	protected String DOMAIN = "http://cmput301.softwareprocess.es:8080/"; // domain
	protected String INDEXNAME = "cmput301f14t02/"; // name of the ES
													// database/index
	protected String TYPENAME = "agora/"; // name of the ES table/type

	public boolean connected; // connected status
	// Queue of statements that need to be run on
	// the server
	private OfflineQueue offlineQueue;

	private static ConnectionReceiver connectionReceiver;
	private static ESDataManager self;

	/**
	 * Generates singleton of the class
	 */
	public static ESDataManager getInstance() {
		if (self == null) {
			self = new ESDataManager();
		}
		return self;
	}

	/**
	 * Gets connected status of the phone.
	 */
	public boolean isConnected() {
		return this.connected;
	}

	/**
	 * Constructor
	 * <p>
	 * Initiates an instance of the ES class which manages all networking with
	 * the ES server.
	 * <p>
	 * Gets the connection status and instantiates a broadcast receiver to
	 * monitor changes.
	 */
	protected ESDataManager() {
		ConnectivityManager cm = (ConnectivityManager) Agora.getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		connected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
		connectionReceiver = new ConnectionReceiver();
		Agora.getContext().registerReceiver(connectionReceiver,
				new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	/**
	 * Protective constructor for dependency injection during testing.
	 * <p>
	 * Allows tests to pass in specific addresses on the server to test in
	 * isolation.
	 * <p>
	 * 
	 * @param domain
	 *            The site domain
	 * @param indexName
	 *            The index name
	 * @return typeName The ES type on the server
	 */
	protected ESDataManager(String domain, String indexName, String typeName) {
		this();
		DOMAIN = domain;
		INDEXNAME = indexName;
		TYPENAME = typeName;
	}

	/**
	 * Broadcast receiver used for updating connections status.
	 * <p>
	 * Provides the application with updates on connection status.
	 */
	public class ConnectionReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager conManager = (ConnectivityManager) context
					.getSystemService(context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = conManager.getActiveNetworkInfo();
			// code taken from
			// http://stackoverflow.com/questions/15698790/broadcast-receiver-for-checking-internet-connection-in-android-app
			// Author user1381827
			// November 2, 2014
			if (netInfo != null && netInfo.isConnected()) {
				connected = true;
				Toast.makeText(Agora.getContext(), "Connected",
						Toast.LENGTH_SHORT).show();
			} else {
				connected = false;
				Toast.makeText(Agora.getContext(), "Disconnected",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * Gets all questions on the ES server.
	 * <p>
	 * Creates a generic match all search and returns all questions available.
	 * 
	 * @return a List of all questions on the server
	 */
	public List<Question> getQuestions() throws UnsupportedEncodingException {
		// assuming the question view by default sorts by date
		String requestBody = "{" + "\"query\": {\"match_all\": {}},"
				+ "\"sort\": [" + "{" + "\"date\": {" + "\"order\": \"desc\""
				+ "}" + "}]}";
		String endPoint = "_search";
		return getQuestions(Question.class, requestBody, endPoint, null);
	}

	/**
	 * Provides Async search facility for the ES server
	 * <p>
	 * Based on parameters generates appropriate server requests and then sends
	 * the requests asynchronously.
	 * <p>
	 * Initially a pointer to an empty result list is passed to the caller. Once
	 * the async call returns it populates the list and notifies the controller
	 * of the update.
	 * 
	 * @param returnType
	 *            Provides the type that the returned list will be composed of.
	 * @param requestBody
	 *            The body of the request usually containing ES DSL queries.
	 * @param endPoint
	 *            Post fix to indicate what type of query is being done on the
	 *            server.
	 * @param answerQuery
	 *            When searching answers provides the answer search terms.
	 * @return ArrayList<T> Returns a list of generic type. Either Answer or
	 *         Question objects.
	 */
	private <T> ArrayList<T> getQuestions(Class<T> returnType,
			String requestBody, String endPoint, final String answerQuery)
			throws UnsupportedEncodingException {
		final List<Question> questionList = new ArrayList<Question>();
		final List<Answer> answerList = new ArrayList<Answer>();
		// TODO figure out a better
		if (!connected) {
			Log.e("SERVER", "No internet connection");
			return (ArrayList<T>) questionList;
		}
		AsyncHttpClient client = new AsyncHttpClient();
		StringEntity stringEntityBody = new StringEntity(requestBody);
		String URI = DOMAIN + INDEXNAME + TYPENAME + endPoint;
		Log.e("SERVER GET URI", DOMAIN + INDEXNAME + TYPENAME + endPoint);
		Log.e("SERVER GET BODY", requestBody);
		client.post(Agora.getContext(), URI, stringEntityBody,
				"application/json", new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int status, Header[] arg1,
							byte[] responseBody, Throwable arg3) {

						Log.e("SERVER",
								"getQuestion failure: "
										+ Integer.toString(status));
						Log.e("SERVER", "responsebody: "
								/*+ new String(responseBody)*/);
						Toast.makeText(Agora.getContext(),
								"Failed to pull data from server", 0).show();
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						Log.e("SERVER", "Question get success");
						try {
							String responseBody = new String(arg2);
							JSONObject jsonRes = new JSONObject(responseBody);
							jsonRes = jsonRes.getJSONObject("hits");
							JSONArray jsonArray = jsonRes.getJSONArray("hits");
							Gson gson = new Gson();
							for (int i = 0; i < jsonArray.length(); i++) {
								// get each object in the response, convert to
								// object
								// and add to list.
								JSONObject q = jsonArray.getJSONObject(i);
								q = q.getJSONObject("_source");
								Question qObject = gson.fromJson(q.toString(),
										Question.class);
								questionList.add(qObject);
//								Log.e("SERVER", qObject.getID().toString()
//										+ " " + qObject.getBody());
							}
							// if this is an answer search remove any answers
							// that don't
							// contain the search term.
							if (answerQuery != null) {
								for (Question question : questionList) {
									List<Answer> answers = ((Question) question)
											.getAnswers();
									for (int i = 0; i < answers.size(); i++) {
										if (answers
												.get(i)
												.getBody()
												.toLowerCase()
												.contains(
														answerQuery
																.toLowerCase())) {
											answerList.add(answers.get(i));
										}
									}
								}
							}
							QuestionController.getController().update();
//							Toast.makeText(Agora.getContext(),
//									"ES Get Question Sucess", 0).show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
		if (answerQuery != null) {
			return (ArrayList<T>) answerList;
		} else {
			return (ArrayList<T>) questionList;
		}
	}

	/**
	 * Generates a search for answers based on passed in string of terms.
	 * <p>
	 * Builds the body of the request based on the search term. And creates a
	 * server request based on the terms and appropriate endpoint.
	 * 
	 * @param query
	 *            The search terms.
	 * @return A list of answers meeting the specified search
	 *         criteria.
	 */
	public List<Answer> searchAnswers(String query)
			throws UnsupportedEncodingException {
		String requestBody = "{" + "\"query\": {" + "\"filtered\": {"
				+ "\"query\": {" + "\"match_all\": {}" + "}," + "\"filter\": {"
				+ "\"term\": {" + "\"answers.body\": \"" + query + "\""
				+ "}}}}}";
		String endpoint = "_search";
		List<Answer> list = getQuestions(Answer.class, requestBody, endpoint,
				query);
		// sort through questions and extract answers
		return list;
	}

	/**
	 * Generates a search for questions based passed in string of terms.
	 * <p>
	 * Builds the body of the request based on the search term. And creates a
	 * server request based on the terms and appropriate endpoint.
	 * 
	 * @param query
	 *            The search terms.
	 * @return  A list of questions meeting the specified search
	 *         criteria.
	 */
	public List<Question> searchQuestions(String query)
			throws UnsupportedEncodingException {
		String requestBody = "{" + "\"query\": {" + "\"multi_match\": {"
				+ "\"query\":" + "\"" + query + "\"" + ","
				+ "\"type\": \"most_fields\"," + "\"fields\": ["
				+ "\"title\", \"body\"" + "]}}}";
		String endPoint = "_search";
		return getQuestions(Question.class, requestBody, endPoint, null);
	}

	/**
	 * Gets a question by its unique id value.
	 * <p>
	 * 
	 * @param id
	 *            The questions unique ID value.
	 * @return  A list containing the single question object from
	 *         the server.
	 */
	public List<Question> getQuestionById(Long id)
			throws UnsupportedEncodingException {
		// assuming the question view by default sorts by date
		String requestBody = "{" + "\"query\": {" + "\"match\": {" + "\"_id\":"
				+ id.toString() + "}}}";
		String endPoint = "_search";
		return getQuestions(Question.class, requestBody, endPoint, null);
	}

	/**
	 * Sends a locally cretaed question object to the server.
	 * <p>
	 * Converts to json, generates the appropriate endpoint and makes a server
	 * request.
	 * 
	 * @param q
	 *            The question object to send to the server.
	 */
	public void pushQuestion(Question q) throws UnsupportedEncodingException {
		Gson gson = new Gson();
		String questionSerialized = gson.toJson(q);
		String endPoint = Long.toString(q.getID());
		push(questionSerialized, endPoint);
	}

	/**
	 * Sends a locally cretaed answer object to the server.
	 * <p>
	 * Converts to json, generates the appropriate endpoint and makes a server
	 * request.
	 * 
	 * @param a
	 *            The answer object to push to the server.
	 * @param qID
	 *            The id of the question that the answer to be pushed belongs
	 *            to.
	 * @param cache
	 *            Synthetic cache for testing purposes.
	 */
	public void pushAnswer(Answer a, Long qID, CacheDataManager cache)
			throws UnsupportedEncodingException {
		Question q = cache.getQuestionById(qID);
		Gson gson = new Gson();
		String answerSerialized = gson.toJson(q.getAnswers());
		answerSerialized = "{" + "\"doc\": { " + "\"answers\":"
				+ answerSerialized + "}" + "}";
		String endPoint = Long.toString(qID) + "/" + "_update";
		push(answerSerialized, endPoint);
	}

	/**
	 * Sends a locally cretaed comment object to the server.
	 * <p>
	 * Converts to json, generates the appropriate endpoint and makes a server
	 * request.
	 * 
	 * @param c
	 *            The comment object to push to the server.
	 * @param qID
	 *            The id of the question that the comment to be pushed belongs
	 *            to.
	 * @param aID
	 *            The id of the answer that the comment to be pushed belongs to.
	 * @param cache
	 *            Synthetic cache for testing purposes.
	 */
	public void pushComment(Comment c, Long qID, Long aID,
			CacheDataManager cache) throws UnsupportedEncodingException {
		// if comment is on an answer pass aID, if on question itself pass null
		String endPoint = Long.toString(qID) + "/" + "_update";
		Question q = cache.getQuestionById(qID);
		Gson gson = new Gson();
		if (aID == null) {
			String commentSerialized = gson.toJson(q.getComments());
			commentSerialized = "{" + "\"doc\": { " + "\"comments\":"
					+ commentSerialized + "}" + "}";
			push(commentSerialized, endPoint);

		} else {
			// if qid is not null re-upload the answer array
			String answerSerialized = gson.toJson(q.getAnswers());
			answerSerialized = "{" + "\"doc\": { " + "\"answers\":"
					+ answerSerialized + "}" + "}";
			push(answerSerialized, endPoint);
		}
	}

	/**
	 * Sends a locally cretaed upvote to the server.
	 * <p>
	 * Converts to json, generates the appropriate endpoint and makes a server
	 * request.
	 * 
	 * @param qID
	 *            The id of the question that the upvote to be pushed belongs
	 *            to.
	 */
	public void pushUpvote(Long qID, CacheDataManager cache)
			throws UnsupportedEncodingException {
		Question q = cache.getQuestionById(qID);
		int upvotes = q.getRating();
		String upVoteSerialized = "{" + "\"doc\": { " + "\"rating\":"
				+ Integer.toString(upvotes) + "}" + "}";
		String endPoint = Long.toString(qID) + "/" + "_update";
		push(upVoteSerialized, endPoint);
	}

	/**
	 * Pushes queryItem objects to the server asynchronously.
	 * <p>
	 * Builds a queryItem object based on the ES server address, the body of the
	 * request to be sent, and the endpoint.
	 * 
	 * @param body
	 *            The body of the request to be send to the server.
	 * @param endPoint
	 *            Specifies the endpoint of the server request.
	 */
	public void push(String body, String endPoint)
			throws UnsupportedEncodingException {
		StringEntity stringEntityBody = new StringEntity(body);
		String URI = DOMAIN + INDEXNAME + TYPENAME + endPoint;
		QueryItem queryItem = new QueryItem(stringEntityBody, URI,
				RequestType.POST);
		updateServer(queryItem);
	}

	/**
	 * Accepts query item objects and attempts to send them to the server.
	 * <p>
	 * Uses the QueryItem object to determine the address to send the request
	 * to, and the body of the request. If the server is offline the requests
	 * are cached for processing once connectivity returns.
	 * 
	 * @param qItem
	 *            A query object containing all information to create the server
	 *            request.
	 */
	private void updateServer(final QueryItem qItem) {
		Log.e("SERVER UPDATED", "updateServer called");
		if (connected) {
			AsyncHttpClient client = new AsyncHttpClient();
			client.post(Agora.getContext(), qItem.getURI(), qItem.getBody(),
					"application/json", new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] response) {
							// called when response HTTP status is "200 OK"
							Log.e("SERVER UPDATED",
									"updateServer method success.");
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] errorResponse, Throwable e) {
							// called when response HTTP status is "4XX" (eg.
							// 401, 403, 404)
							Log.e("SERVER UPDATE",
									"updateServer method failure: "
											+ Integer.toString(statusCode));
							Toast.makeText(Agora.getContext(),
									"Update server method failure.", 0).show();
							// offlineQueue.addToQueue(qItem);
						}
					});

		} else {
			offlineQueue.addToQueue(qItem);
		}

	}
}