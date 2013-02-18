package com.android.googleplaces;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapResult extends MapActivity {

	MapView view;
	Drawable overlayDrw;
	List<Overlay> mapOverlays;
	MapResultOverlay itemizedOverlays;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_result);

		overlayDrw = getResources().getDrawable(R.drawable.ic_launcher);

		view = (MapView) findViewById(R.id.map_result);
		view.setBuiltInZoomControls(true);
		
		mapOverlays = view.getOverlays();
		itemizedOverlays = new MapResultOverlay(overlayDrw, this);

		double user_lat = getIntent().getDoubleExtra(Places._user_lat_tag, 0);
		double user_lng = getIntent().getDoubleExtra(Places._user_lng_tag, 0);
		Log.d("map results class", user_lat + " " + user_lng);
		double lat = getIntent().getDoubleExtra(Places._lat_tag, 0);
		double lng = getIntent().getDoubleExtra(Places._lng_tag, 0);

		setOverlays(user_lat, user_lng, lat, lng);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	protected void setOverlays(double user_lat, double user_lng, double lat,
			double lng) {
		GeoPoint gpUser = new GeoPoint((int) (user_lat * 1E6),
				(int) (user_lng * 1E6));
		GeoPoint gpStoreBranch = new GeoPoint((int) (lat * 1E6),
				(int) (lng * 1E6));

		MapController mapcon = view.getController();
		mapcon.animateTo(gpUser);
		mapcon.setCenter(gpUser);
		mapcon.setZoom(16);

		OverlayItem userCurrentLoc = new OverlayItem(gpUser, "User's location",
				"This your current location");
		OverlayItem targetbranch = new OverlayItem(gpStoreBranch,
				"Store location", "This is your destination");

		itemizedOverlays.addOverlay(userCurrentLoc);
		itemizedOverlays.addOverlay(targetbranch);

		mapOverlays.add(itemizedOverlays);
	}
}
