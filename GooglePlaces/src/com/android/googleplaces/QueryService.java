package com.android.googleplaces;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class QueryService extends IntentService implements LocationListener {

	// this is from androidhive tutorials, bigyan sila ng jacket

	private Context mContext;

	boolean isGPSEnabled = false;

	boolean isNetworkEnabled = false;

	boolean canGetLocation = false;

	Location location; // location
	double latitude; // latitude
	double longitude; // longitude

	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	LocationManager locationManager;

	public QueryService() {
		super("The service");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		mContext = getApplicationContext();

		locationManager = (LocationManager) mContext
				.getSystemService(LOCATION_SERVICE);

		location = getLocation();

		if (location != null) {
			Log.d("Is location null?", "not null location");
			String results = googlePlaces(location);
			sendBroadcastIntent(Places._okay_status, results, location);
		} else {
			Log.d("Is location null?", "null location");
			sendBroadcastIntent(Places._not_okays_status, null, null);
		}
	}

	public Location getLocation() {

		Location tempLoc = null;

		try {
			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
				tempLoc = null;
			} else {
				this.canGetLocation = true;
				// First get location from Network Provider
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						tempLoc = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (tempLoc != null) {
							latitude = tempLoc.getLatitude();
							longitude = tempLoc.getLongitude();
						}
					}
				}
				// if GPS Enabled get latitude/longitude using GPS Services
				if (isGPSEnabled) {
					if (tempLoc == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							tempLoc = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (tempLoc != null) {
								latitude = tempLoc.getLatitude();
								longitude = tempLoc.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			tempLoc = null;
		}

		if (tempLoc != null)
			Log.d("Locations: ", String.valueOf(tempLoc.getLatitude()) + " "
					+ String.valueOf(tempLoc.getLongitude()));

		return tempLoc;
	}

	public String googlePlaces(Location loc) {

		double lat = loc.getLatitude();
		double lng = loc.getLongitude();

		HttpClient client = new DefaultHttpClient();
		ResponseHandler<String> resp = new BasicResponseHandler();

		String googleQuery = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
				+ String.valueOf(lat)
				+ ","
				+ String.valueOf(lng)
				+ "&name=Jollibee&radius=500&sensor=false&key=AIzaSyAGNgirBrZml-EdGp25QNmAl76g_sEyKjc";
		HttpPost post = new HttpPost(googleQuery);

		String result = "";

		try {
			result = client.execute(post, resp);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "";
		}

		return result;

	}

	public void sendBroadcastIntent(String status, String results,
			Location location) {
		Intent i = new Intent();
		i.setAction(Places.QueryReceiver.ACTION_RESP);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		i.putExtra(Places._statusTag, status);
		if (location != null) {
			i.putExtra(Places._resultsTag, results);
			i.putExtra(Places._user_lat_tag, location.getLatitude());
			i.putExtra(Places._user_lng_tag, location.getLongitude());
		}
		sendBroadcast(i);
	}

	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
