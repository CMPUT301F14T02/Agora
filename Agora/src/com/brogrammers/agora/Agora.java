package com.brogrammers.agora;

import android.app.Application;
import android.content.Context;

public class Agora extends Application {
	private static Context context;
	
	public static Context getContext() {
		return context;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		context = getApplicationContext();
	}
}
