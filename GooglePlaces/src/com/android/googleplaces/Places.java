package com.android.googleplaces;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Places extends Activity {

	public static final String _resultsTag = "!#@%@$!@";
	public static final String _statusTag = "!#@445t4f%@$!@";
	public static final String _lat_tag = "dasfstw";
	public static final String _lng_tag = "dsadas879";
	public static final String _user_lat_tag = "dsaddsadas879";
	public static final String _user_lng_tag = "dsadfasfdgfdas879";

	public static final String _okay_status = "okay";
	public static final String _not_okays_status = "notokaay";

	static Context mCtx;

	JSONObject resultQuery;
	ListView places;
	ArrayAdapter<PlaceDetails> placesAdapter;
	ArrayList<PlaceDetails> placesList;
	QueryReceiver receiver;
	IntentFilter iF;

	double user_lat;
	double user_lng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mCtx = getApplicationContext();

		placesList = new ArrayList<PlaceDetails>();
		places = (ListView) findViewById(R.id.listPlaces);
		places.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> aview, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.d("places class click item", user_lat + " " + user_lng);
				double lat = placesList.get(position).getLat();
				double lng = placesList.get(position).getLng();
				Intent i = new Intent(Places.this, MapResult.class);
				i.putExtra(_lat_tag, lat);
				i.putExtra(_lng_tag, lng);
				i.putExtra(_user_lat_tag, user_lat);
				i.putExtra(_user_lng_tag, user_lng);
				startActivity(i);
			}

		});

		placesAdapter = new ArrayAdapter<PlaceDetails>(this,
				android.R.layout.simple_list_item_1, placesList);
		places.setAdapter(placesAdapter);

		startService(new Intent(this, QueryService.class));

		receiver = new QueryReceiver();
		iF = new IntentFilter();
		iF.addAction(QueryReceiver.ACTION_RESP);
		iF.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(receiver, iF);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		super.onPause();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		registerReceiver(receiver, iF);
		super.onResume();
	}

	private static ArrayList<PlaceDetails> generateListPlaces(String jsonstring)
			throws JSONException {

		ArrayList<PlaceDetails> temp = new ArrayList<PlaceDetails>();

		JSONObject resultQuery = new JSONObject(jsonstring);
		JSONArray resultArray = resultQuery.getJSONArray("results");

		for (int i = 0; i < resultArray.length(); i++) {
			JSONObject obj = resultArray.getJSONObject(i);

			String name = obj.getString("name");
			String address = obj.getString("vicinity");
			String reference = obj.getString("reference");

			JSONObject geometry = obj.getJSONObject("geometry");
			double lat = geometry.getJSONObject("location").getDouble("lat");
			double lng = geometry.getJSONObject("location").getDouble("lng");

			temp.add(new PlaceDetails(name, address, lat, lng, reference));
		}

		return temp;

	}

	public class QueryReceiver extends BroadcastReceiver {

		public static final String ACTION_RESP = "com.android.googleplaces.intent.action.RECEIVED";

		@Override
		public void onReceive(Context ctx, Intent i) {
			// TODO Auto-generated method stub
			Log.d("Receive test", "received it");
			Log.d("Ewan ko", i.getStringExtra(_statusTag));

			if (i.getStringExtra(_statusTag).equals(Places._okay_status)) {
				// tangina nadali ako ng String.equals() at "=="
				Log.d("Receive test", "okay naman");
				String queryResults = i.getStringExtra(Places._resultsTag);
				placesList.clear();
				try {
					placesList.addAll(generateListPlaces(queryResults));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				user_lat = i.getDoubleExtra(_user_lat_tag, 0);
				user_lng = i.getDoubleExtra(_user_lng_tag, 0);

				placesAdapter.notifyDataSetChanged();
				Log.d("Receive test", "finished it");
			}

			else {
				Log.d("Receive test", "error here");
				Toast.makeText(
						getApplicationContext(),
						"Error, its either GPS is not enabled or \nyou are disconnected to the internet",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

}
