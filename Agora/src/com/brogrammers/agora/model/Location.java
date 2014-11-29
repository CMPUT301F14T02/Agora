package com.brogrammers.agora.model;

public class Location {
	private double lat;
	private double lon;
	private String locationName;

	public Location(double d, double e, String locationName){
		this.lat = d;
		this.lon = e;
		this.locationName = locationName;
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
