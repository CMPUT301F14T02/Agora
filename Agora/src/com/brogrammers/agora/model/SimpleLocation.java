package com.brogrammers.agora.model;

import android.widget.EditText;

public class SimpleLocation {
	private double lat;
	private double lon;
	private String locationName;

	public SimpleLocation(double d, double e, String locationName){
		this.lat = d;
		this.lon = e;
		this.locationName = locationName;
	}

	public SimpleLocation(String setLocation) {
		this.locationName = setLocation;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public String getLocationName() {
		return locationName;
	}
}
