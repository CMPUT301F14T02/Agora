package com.brogrammers.agora.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.util.Log;
import android.widget.Toast;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.SimpleLocation;
import com.brogrammers.agora.model.Question;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class LocationDataManager {
	private static String URI = "http://nominatim.openstreetmap.org/reverse?format=json&";
	private static String URI2 = "http://nominatim.openstreetmap.org/search/";
	private static String parameters = "?format=json&addressdetails=0";
	private static SimpleLocation currentLocation;
	private static String currentLocationName;
	public static JSONObject testjson;
	public static LocationDataManager self;
	
	public static LocationDataManager getInstance() {
		if (self == null) {
			self = new LocationDataManager();
		}
		return self;
	}
	
	public static SimpleLocation getLocation(){
		return currentLocation;
	}
	
	public static void setLocationName(String manLocation) {
		currentLocationName = manLocation;
		
	}
	
	public static String getLocationName(){
		return currentLocationName;
	}

	public void initLocation(double d, double e){
		if (currentLocation == null){
			currentLocation = new SimpleLocation(d, e);
			reverseGeoCode(d, e);
		}
		
	}

	/**
	 * This snippet allows UI on main thread.
	 * Normally it's 2 lines but since we're supporting 2.x, we need to reflect.
	 */
	private static void disableStrictMode() {
		  // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		  // StrictMode.setThreadPolicy(policy);
		//http://twigstechtips.blogspot.co.uk/2013/08/android-how-to-fix-networkonmainthreade.html

		  try {
		    Class<?> strictModeClass = Class.forName("android.os.StrictMode", true, Thread.currentThread().getContextClassLoader());
		    Class<?> threadPolicyClass = Class.forName("android.os.StrictMode$ThreadPolicy", true, Thread .currentThread().getContextClassLoader());
		    Class<?> threadPolicyBuilderClass = Class.forName("android.os.StrictMode$ThreadPolicy$Builder", true, Thread.currentThread().getContextClassLoader());

		    Method setThreadPolicyMethod = strictModeClass.getMethod("setThreadPolicy", threadPolicyClass);

		    Method detectAllMethod = threadPolicyBuilderClass.getMethod("detectAll");
		    Method penaltyMethod = threadPolicyBuilderClass.getMethod("penaltyLog");
		    Method buildMethod = threadPolicyBuilderClass.getMethod("build");

		    Constructor<?> threadPolicyBuilderConstructor = threadPolicyBuilderClass.getConstructor();
		    Object threadPolicyBuilderObject = threadPolicyBuilderConstructor.newInstance();

		    Object obj = detectAllMethod.invoke(threadPolicyBuilderObject);

		    obj = penaltyMethod.invoke(obj);
		    Object threadPolicyObject = buildMethod.invoke(obj);
		    setThreadPolicyMethod.invoke(strictModeClass, threadPolicyObject);
		  }
		  catch (Exception ex) {
		    Log.w("disableStrictMode", ex);
		  }
		} 
	
	public static void  reverseGeoCode(final double d, final double e){
		// compe the url
		disableStrictMode();
		URI += "lat=" + String.valueOf(d) + "&" + "lon=" + String.valueOf(e);
		final List<SimpleLocation> locationList = new ArrayList<SimpleLocation>();
        HttpClient client = new DefaultHttpClient();
        try {
            HttpGet locationRequest = new HttpGet(URI);
            HttpResponse response = client.execute(locationRequest);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            } 
            JSONObject o = new JSONObject(result.toString());
            o = o.getJSONObject("address");
            String parsedLocation = o.getString("city");
            parsedLocation += ", ";
            parsedLocation += o.getString("country");
            currentLocationName = parsedLocation;

        }  catch (JSONException e1){
        	e1.printStackTrace();
        } catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/*
	public static void  geoCode(final String strLocation){
		// compe the url
		URI2+=strLocation + parameters;
		final List<SimpleLocation> locationList = new ArrayList<SimpleLocation>();
        HttpClient client = new DefaultHttpClient();
        try {
            HttpGet locationRequest = new HttpGet(URI);
            HttpResponse response = client.execute(locationRequest);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            } 
            JSONObject o = new JSONObject(result.toString());
            o = o.getJSONObject("boundingbox");
            Double parsedLat = o.getDouble("lat");
            Double parsedLon = o.getDouble("lon");
            currentLocation = new SimpleLocation(parsedLat,parsedLon, strLocation);

        }  catch (JSONException e1){
        	e1.printStackTrace();
        } catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	*/
}