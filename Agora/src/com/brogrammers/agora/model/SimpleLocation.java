package com.brogrammers.agora.model;

import android.widget.EditText;

public class SimpleLocation {
	private double lat;
	private double lon;

	public SimpleLocation(double d, double e){
		this.lat = d;
		this.lon = e;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}
}
