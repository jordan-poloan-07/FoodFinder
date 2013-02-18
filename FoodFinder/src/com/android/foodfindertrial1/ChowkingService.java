package com.android.foodfindertrial1;

import com.android.foodfindertrial1.FoodFinder.TheReceiver;
import com.android.foodfindertrial1.necc.*;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ChowkingService extends IntentService {

	InternalStorageMethods ism;
	HtmlParserClass hpc;

	public ChowkingService() {
		super("Chowking");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent i) {
		// TODO Auto-generated method stub

		ism = new InternalStorageMethods(this);
		hpc = new HtmlParserClass();

		String chowkingHtml = "";

		try {

			chowkingHtml = connectToChowking("http://chowkingdelivery.com/all.html?limit=all&mode=list");

			String parsedElement = hpc.neededElements(chowkingHtml,
					"ol.products-list");

			ism.writeFileToInternal(UtilClass.CHOWKING_FILENAME, parsedElement);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.d("clientexception", e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("ioexception", e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			Log.d("generalexception", e.toString());
			e.printStackTrace();
		} finally {
			Log.d("state", "reached onFinish()");
			sendBroadCast(TheReceiver.ACTION_STRING,
					"Synchronized with Chowking");
		}

		stopSelf();
	}

	private String connectToChowking(String website)
			throws ClientProtocolException, IOException {
		String tempString;

		HttpClient httpClient = new DefaultHttpClient();
		ResponseHandler<String> resp = new BasicResponseHandler();
		HttpGet httpGet = new HttpGet(website);

		tempString = httpClient.execute(httpGet, resp);

		Log.d("state", "returning tempString");
		return tempString;
	}

	private void sendBroadCast(String ACTION, String html) {
		Intent i = new Intent();
		i.setAction(ACTION);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		i.putExtra(UtilClass.out_msg, html);
		sendBroadcast(i);
	}
}
