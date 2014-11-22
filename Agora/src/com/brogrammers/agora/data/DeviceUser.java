package com.brogrammers.agora.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.model.Author;
import com.brogrammers.agora.views.UserNameSelector;

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
	private Activity activity;

	static public DeviceUser getUser(Activity activity) {
		if (self == null) {
			self = new DeviceUser(activity);
		}
		return self;
	}
	
	/**
	 * 
	 * @return the singleton instance of the DeviceUser
	 */
	static public DeviceUser getUser() {
		return self;
	}

	/**
	 * The constructor loads user settings
	 */
	@SuppressWarnings("unchecked")
	protected DeviceUser(Activity activity) {
		this.activity = activity;
		Context context = Agora.getContext();
		favoritesPrefFile = context.getSharedPreferences(
				favoritesPrefFileName, Context.MODE_PRIVATE);
		cachedPrefFile = context.getSharedPreferences(cachedPrefFileName,
				Context.MODE_PRIVATE);
		authoredPrefFile = context.getSharedPreferences(authoredPrefFileName,
				Context.MODE_PRIVATE);
		usernamePrefFile = context.getSharedPreferences(usernamePrefFileName,
				Context.MODE_PRIVATE);

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
//			(new UserNameSelector(activity)).show();
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
		SharedPreferences.Editor editor = favoritesPrefFile.edit();
		editor.putLong(username+id, id);
		editor.commit();
	}

	public void addCachedQuestionID(Long id) {
		cachedQuestionIDs.add(id);
		SharedPreferences.Editor editor = cachedPrefFile.edit();
		editor.putLong(username+id, id);
		editor.commit();
	}

	public void addAuthoredQuestionID(Long id) {
		authoredQuestionIDs.add(id);
		SharedPreferences.Editor editor = authoredPrefFile.edit();
		editor.putLong(username+id, id);
		editor.commit();
	}

	public void setUsername(String name) {
		this.username = name;
		SharedPreferences.Editor editor = usernamePrefFile.edit();
		editor.putString(username, name);
		editor.commit();
	}
	
	public void clearAllPreferences() {
		SharedPreferences.Editor editor = favoritesPrefFile.edit();
		editor.clear().commit();
		editor = cachedPrefFile.edit();
		editor.clear().commit();
		editor = authoredPrefFile.edit();
		editor.clear().commit();
		editor = usernamePrefFile.edit();
		editor.clear().commit();
	}
	
}
