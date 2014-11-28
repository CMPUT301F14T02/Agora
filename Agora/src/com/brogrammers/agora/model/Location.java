package com.brogrammers.agora.model;

public class Location {
	private Float lat;
	private Float lon;
	private String locationName;

	public Location(Float lat, Float lon, String locationName){
		this.lat = lat;
		this.lon = lon;
		this.locationName = locationName;
	}

	public Float getLat() {
		return lat;
	}

	public Float getLon() {
		return lon;
	}

	public String getLocationName() {
		return locationName;
	}
}
