package com.android.foodfindertrial1;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.android.foodfindertrial1.FoodFinder.TheReceiver;
import com.android.foodfindertrial1.necc.HtmlParserClass;
import com.android.foodfindertrial1.necc.InternalStorageMethods;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class JollibeeService extends IntentService {

	InternalStorageMethods ism;
	HtmlParserClass hpc;

	public JollibeeService() {
		super("Jollibee");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		ism = new InternalStorageMethods(this);
		hpc = new HtmlParserClass();

		String jollibeeHtml = "";

		try {
			jollibeeHtml = connectToJollibee("http://jollibeedelivery.com/catalogsearch/result/index/?limit=all&mode=list&q=%27+or+1+%3D%3D+1+%27");

			String parsedItems = hpc.neededElements(jollibeeHtml, "ul.c-list");

			ism.writeFileToInternal(UtilClass.JBEE_FILENAME, parsedItems);
		} catch (ClientProtocolException cp) {
			Log.d("clientexception", cp.toString());
		} catch (IOException e) {
			Log.d("IOexception", e.toString());
		} catch (Exception e) {
			Log.d("generalexception", e.toString());
		} finally {
			Log.d("state", "reached onFinish()");
			sendBroadCast(TheReceiver.ACTION_STRING,
					"Synchronized with Jollibee");
		}

		stopSelf();
	}

	private String connectToJollibee(String website)
			throws ClientProtocolException, IOException {

		String tempString;

		HttpClient httpClient = new DefaultHttpClient();
		ResponseHandler<String> resp = new BasicResponseHandler();
		HttpGet httpGet = new HttpGet(website);

		tempString = httpClient.execute(httpGet, resp);

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
