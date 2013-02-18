package com.android.googleplaces;

public class PlaceDetails {

	public String name;
	public String vicinity;
	public double lat;
	public double lng;
	public String reference;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public PlaceDetails(String name, String vicinity, double lat, double lng,
			String reference) {
		this.name = name;
		this.vicinity = vicinity;
		this.lat = lat;
		this.lng = lng;
		this.reference = reference;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String result = name + "\n" + vicinity + "\n" + "\tLatitude: " + lat
				+ "\n" + "\tLongitude: " + lng;
		return result;
	}

}
