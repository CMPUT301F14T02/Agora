package com.brogrammers.agora.model;

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
