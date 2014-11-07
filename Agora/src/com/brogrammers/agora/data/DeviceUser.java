package com.brogrammers.agora.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.model.Author;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * The DeviceUser is a specialized singleton instance of an Author, which
 * represents the current user of an instance of the application and stores user
 * settings. All new Questions, Answers, and Comments will use the DeviceUser's
 * name as the author.
 * 
 * @author Group02
 * 
 */
public class DeviceUser extends Author {
	protected static String favoritesPrefFileName = "FAVORITES";
	protected static String cachedPrefFileName = "CACHED";
	protected static String authoredPrefFileName = "AUTHORED";
	protected static String usernamePrefFileName = "USERNAME";
	transient private SharedPreferences favoritesPrefFile;
	transient private SharedPreferences cachedPrefFile;
	transient private SharedPreferences authoredPrefFile;
	transient private SharedPreferences usernamePrefFile;
	transient private ArrayList<Long> favoritedQuestionIDs = new ArrayList<Long>();
	transient private ArrayList<Long> cachedQuestionIDs = new ArrayList<Long>();
	transient private ArrayList<Long> authoredQuestionIDs = new ArrayList<Long>();

	static private DeviceUser self = null;

	/**
	 * 
	 * @return the singleton instance of the DeviceUser
	 */
	static public DeviceUser getUser() {
		if (self == null) {
			self = new DeviceUser();
		}
		return self;
	}

	/**
	 * The constructor loads user settings
	 */
	protected DeviceUser() {
		Context context = Agora.getContext();
		favoritesPrefFile = context.getSharedPreferences(
				favoritesPrefFileName, Context.MODE_PRIVATE);
		cachedPrefFile = context.getSharedPreferences(cachedPrefFileName,
				Context.MODE_PRIVATE);
		authoredPrefFile = context.getSharedPreferences(authoredPrefFileName,
				Context.MODE_PRIVATE);
		usernamePrefFile = context.getSharedPreferences(usernamePrefFileName,
				Context.MODE_PRIVATE);

		Map<String, Long> favorites = (Map<String, Long>) favoritesPrefFile
				.getAll();
		for (Long id : favorites.values()) {
			favoritedQuestionIDs.add(id);
		}
		Map<String, Long> cached = (Map<String, Long>) cachedPrefFile.getAll();
		for (Long id : cached.values()) {
			cachedQuestionIDs.add(id);
		}
		Map<String, Long> authored = (Map<String, Long>) authoredPrefFile
				.getAll();
		for (Long id : authored.values()) {
			authoredQuestionIDs.add(id);
		}
		Map<String, String> username_ = (Map<String, String>) usernamePrefFile
				.getAll();
		if (username_.containsKey("username")) {
			this.username = username_.get("username");
		} else {
			// TODO: implement user's ability to choose username on first launch
			this.username = "BingsF";
		}
	}

	public List<Long> getFavoritedQuestionIDs() {
		return favoritedQuestionIDs;
	}

	public List<Long> getCachedQuestionIDs() {
		return cachedQuestionIDs;
	}

	public List<Long> getAuthoredQuestionIDs() {
		return authoredQuestionIDs;
	}

	public void addFavoritedQuestionID(Long id) {
		favoritedQuestionIDs.add(id);
	}

	public void addCachedQuestionID(Long id) {
		cachedQuestionIDs.add(id);
	}

	public void addAuthoredQuestionID(Long id) {
		authoredQuestionIDs.add(id);
	}

	public void setUsername(String name) {
		this.username = name;
	}
}
