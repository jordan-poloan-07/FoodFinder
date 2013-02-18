package com.android.foodfindertrial1;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class FoodList extends Activity implements
		UtilClass.VirtualKeyboardHider {

	private ArrayList<String> menu;
	private ArrayAdapter<String> adapter;
	private ActionBar bar;
	double basePrice;

	private ListView list;
	private LinearLayout ll;
	private LinearLayout ll2;
	private FoodListReceiver flr;
	private IntentFilter iFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_list);

		menu = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(FoodList.this,
				android.R.layout.simple_list_item_1, menu);
		basePrice = getIntent().getDoubleExtra(UtilClass.base_price, 0);

		list = (ListView) findViewById(R.id.foodList);
		list.setAdapter(adapter);
		list.setEmptyView(findViewById(R.id.emptyView));

		ll = (LinearLayout) findViewById(R.id.ll);
		ll2 = (LinearLayout) findViewById(R.id.ll2);

		bar = getActionBar();

		iFilter = new IntentFilter();
		iFilter.addAction(FoodListReceiver.ACTION_FOOD_LIST);
		iFilter.addCategory(Intent.CATEGORY_DEFAULT);
		flr = new FoodListReceiver();
		registerReceiver(flr, iFilter);

		if (savedInstanceState == null)
			startService(new Intent(FoodList.this, FoodListService.class)
					.putExtra(UtilClass._basePrice, basePrice));
		// new StartTask().execute();
		else {
			menu.addAll(savedInstanceState
					.getStringArrayList(UtilClass._listKey));
			adapter.notifyDataSetChanged();
			ll.setVisibility(View.INVISIBLE);
			ll2.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putStringArrayList(UtilClass._listKey, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_food_finder, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.enterAmount:
			if (bar.getCustomView() != null)
				break;
			bar.setCustomView(R.layout.action_bar);
			bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
					| ActionBar.DISPLAY_SHOW_HOME);

			final EditText money = (EditText) bar.getCustomView().findViewById(
					R.id.moneyEditText);
			final Button go = (Button) bar.getCustomView().findViewById(
					R.id.goButton);

			go.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					hideKeyboard();

					double price = 0;

					try {
						price = Double.parseDouble(money.getText().toString());
					} catch (NumberFormatException e) {
						Toast.makeText(getApplicationContext(),
								"Invalid number", Toast.LENGTH_SHORT).show();
						return;
					}

					ll.setVisibility(View.VISIBLE);
					ll2.setVisibility(View.INVISIBLE);
					startService(new Intent(FoodList.this,
							FoodListService.class).putExtra(
							UtilClass._basePrice, price));

					bar.setCustomView(null);

				}

			});
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public class FoodListReceiver extends BroadcastReceiver {

		public static final String ACTION_FOOD_LIST = "com.android.foodfindertrial1.intent.action.FSR";

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(),
					intent.getStringExtra(UtilClass._fslmsg),
					Toast.LENGTH_SHORT).show();
			menu.clear();
			menu.addAll(intent.getStringArrayListExtra(UtilClass._listKey));
			adapter.notifyDataSetChanged();
			ll.setVisibility(View.INVISIBLE);
			ll2.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(flr);
	}

	@Override
	public void hideKeyboard() {
		// TODO Auto-generated method stub
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
