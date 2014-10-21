package com.brogrammers.agora;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class DeviceUser extends Author {
	static final String favoritesPrefFileName = "FAVORITES";
	static final String cachedPrefFileName = "CACHED";
	static final String authoredPrefFileName = "AUTHORED";
	static final String usernamePrefFileName = "USERNAME";
	transient private SharedPreferences favoritesPrefFile;
	transient private SharedPreferences cachedPrefFile;
	transient private SharedPreferences authoredPrefFile;
	transient private SharedPreferences usernamePrefFile;
	transient private ArrayList<Long> favoritedQuestionIDs;
	transient private ArrayList<Long> cachedQuestionIDs;
	transient private ArrayList<Long> authoredQuestionIDs;
	
	// singleton
	static private DeviceUser self = null;

	// The first call to getUser (probably from BrowseQuestionsView) should pass an activity
	static public DeviceUser getUser(Activity activity) {
		if (self == null) {
			self = new DeviceUser(activity);
		}
		return self;
	}
	// Subsequent calls may omit an Activity
	static public DeviceUser getUser() {
		return self;
	}
	
	
	private DeviceUser(Activity activity) {
		favoritesPrefFile = activity.getSharedPreferences(favoritesPrefFileName, Context.MODE_PRIVATE);
		cachedPrefFile = activity.getSharedPreferences(cachedPrefFileName, Context.MODE_PRIVATE);
		authoredPrefFile = activity.getSharedPreferences(authoredPrefFileName, Context.MODE_PRIVATE);
		usernamePrefFile = activity.getSharedPreferences(usernamePrefFileName, Context.MODE_PRIVATE);
		
		Map<String, Long> favorites = (Map<String, Long>) favoritesPrefFile.getAll();
		for (Long id : favorites.values()) {
			favoritedQuestionIDs.add(id);
		}
		Map<String, Long> cached = (Map<String, Long>) cachedPrefFile.getAll();
		for (Long id : cached.values()) {
			cachedQuestionIDs.add(id);
		}
		Map<String, Long> authored = (Map<String, Long>) authoredPrefFile.getAll();
		for (Long id : authored.values()) {
			authoredQuestionIDs.add(id);
		}
		Map<String, String> username_ = (Map<String, String>) usernamePrefFile.getAll();
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



