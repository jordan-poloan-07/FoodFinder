package com.android.foodfindertrial1;

import com.android.foodfindertrial1.necc.CheckerMethods;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FoodFinder extends Activity implements OnClickListener,
		UtilClass.VirtualKeyboardHider {

	EditText money;
	Button confirm;
	ImageView jbee, mcdo;
	TextView text;
	TheReceiver receiver;
	IntentFilter intf;
	CheckerMethods methods;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_finder);

		methods = new CheckerMethods(this);

		money = (EditText) findViewById(R.id.money);
		confirm = (Button) findViewById(R.id.confirm);
		confirm.setOnClickListener(this);
		jbee = (ImageView) findViewById(R.id.jbee);
		jbee.setOnClickListener(this);
		mcdo = (ImageView) findViewById(R.id.chowking);
		mcdo.setOnClickListener(this);
		text = (TextView) findViewById(R.id.textView2);

		intf = new IntentFilter();
		intf.addAction(TheReceiver.ACTION_STRING);
		intf.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new TheReceiver();
		registerReceiver(receiver, intf);
	}

	public class TheReceiver extends BroadcastReceiver {

		public static final String ACTION_STRING = "com.android.foodfindertrial1.intent.action.THERECEIVER";

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(),
					"Finished downloading data", Toast.LENGTH_LONG).show();
			String message = intent.getStringExtra(UtilClass.out_msg);
			text.setText(message);
		}

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.confirm:

			// after pressing Start button do the following:

			// 1 check if Jollibee file exists in internal storage, breaks if
			// false
			if (!(methods.isFileExisting(UtilClass.JBEE_FILENAME) && methods
					.isFileExisting(UtilClass.CHOWKING_FILENAME))) {
				Toast.makeText(getApplicationContext(),
						"Please synchronize with Fastfood server(s) Jollibee and Chowking",
						Toast.LENGTH_SHORT).show();
				break;
			}

			// hide the virtual keyboard
			hideKeyboard();

			// instantiate the variable to be used for containing the user
			// input, must have been float, nvm
			double moneyInput = 0;

			try {
				// "This guy over here" malamang parse nyo string --> double
				moneyInput = Double.parseDouble(money.getText().toString());
			} catch (NumberFormatException e) {
				// if input is not a number gg
				Toast.makeText(getApplicationContext(), "Not a valid number",
						Toast.LENGTH_SHORT).show();
				break;
			}

			// starts the next activity
			startActivity(new Intent(FoodFinder.this, FoodList.class).putExtra(
					UtilClass.base_price, moneyInput));
			break;
		case R.id.jbee:
			if (!methods.isInternetAvailable()) {
				Toast.makeText(getApplicationContext(),
						"Connect to the internet please...", Toast.LENGTH_SHORT)
						.show();
				break;
			}
			Toast.makeText(getApplicationContext(),
					"Gathering info from Jollibee server...",
					Toast.LENGTH_SHORT).show();
			startService(new Intent(FoodFinder.this, JollibeeService.class));
			break;
		case R.id.chowking:
			Toast.makeText(getApplicationContext(),
					"Gathering info from Chowking server...",
					Toast.LENGTH_SHORT).show();
			startService(new Intent(FoodFinder.this, ChowkingService.class));
			// pag may mcdo na ilagay na to, tagalog naman kanina pa ako
			// english-english
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(receiver, intf);
	}

	@Override
	public void hideKeyboard() {
		// TODO Auto-generated method stub
		// code snippet from stackoverflow
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
