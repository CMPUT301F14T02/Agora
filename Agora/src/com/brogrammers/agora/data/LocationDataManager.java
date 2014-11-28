package com.brogrammers.agora.data;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

import com.brogrammers.agora.Agora;
import com.brogrammers.agora.model.Answer;
import com.brogrammers.agora.model.Location;
import com.brogrammers.agora.model.Question;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class LocationDataManager {
	private String URI = "http://nominatim.openstreetmap.org/reverse?format=json&";
		
	private static Location currentLocation;
	
	public Location getLocation(Float lat, Float lon){
		if (currentLocation != null){
			return currentLocation;
		} else {
			return reverseGeoCode(lon, lon).get(0);
		}
	}
	
	private ArrayList<Location> reverseGeoCode(final Float lat, final Float lon){
		// compe the url
		URI += "lat=" + Float.toString(lat) + "&" + "lon=" + Float.toString(lon);
		final List<Location> locationList = new ArrayList<Location>();
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(URI, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int status, Header[] arg1,
					byte[] responseBody, Throwable arg3) {
				Log.e("SERVER", "Get location failure: " + Integer.toString(status));
				Log.e("SERVER", "responsebody: " /*+ new String(responseBody)*/);
				Toast.makeText(Agora.getContext(), "Failed to get location from server", 0).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				Log.e("SERVER", "Question get success");
				try {
					String responseBody = new String(arg2);
					JSONObject jsonRes = new JSONObject(responseBody);
					jsonRes = jsonRes.getJSONObject("address");
					String parsedLocation = jsonRes.getString("city");
					currentLocation = new Location(lat, lon, parsedLocation);
                    QuestionController.getController().update();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
        return (ArrayList<Location>) locationList;
	}
}
