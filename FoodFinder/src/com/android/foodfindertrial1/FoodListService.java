package com.android.foodfindertrial1;

import java.util.ArrayList;

import com.android.foodfindertrial1.necc.*;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class FoodListService extends IntentService {

	// create a blank array list
	// add the array list collections for each file,
	// send to Food Lis broad cast receiver

	InternalStorageMethods ism;
	HtmlParserClass hpc;
	ArrayList<String> passedList;

	public FoodListService() {
		super("Food_List");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		ism = new InternalStorageMethods(this);
		hpc = new HtmlParserClass();
		passedList = new ArrayList<String>();

		double price = intent.getDoubleExtra(UtilClass._basePrice, 0);

		// first get JBEE file and make arraylist from it, then add to
		// passedList
		// do chowking next

		// no exception cheking version

		String jbeeFile = ism.readFileFromInternal(UtilClass.JBEE_FILENAME);
		String chowFile = ism.readFileFromInternal(UtilClass.CHOWKING_FILENAME);

		passedList.addAll(hpc.generateJollibeeArrayList(
				UtilClass.JBEE_FILENAME, jbeeFile, price));

		passedList.addAll(hpc.generateChowkingArrayList(
				UtilClass.CHOWKING_FILENAME, chowFile, price));

		// Log.d("test", "Executing readFileFromInternal");
		// String file = readFilefromInternal(UtilClass.JBEE_FILENAME);
		// Log.d("test", "Executing generateArrayList");

		// if (file != null) {
		// ArrayList<String> list = generateArraylist(UtilClass.JBEE_FILENAME,
		// file, price);
		// Log.d("test", "Executing broadcast");

		// return;
		// }

		if (!(jbeeFile.equals(null) && chowFile.equals(null))) {
			broadcast(passedList, "Data loaded");
			stopSelf();
			return;
		}

		Log.d("end", "Service stopped");
		stopSelf();
	}

	// private String readFilefromInternal(String filename) {
	//
	// String read = null;
	// FileInputStream fis = null;
	//
	// try {
	// fis = this.openFileInput(filename);
	// byte[] wordBytes = new byte[fis.available()];
	// while (fis.read(wordBytes) != -1) {
	// read = new String(wordBytes);
	// }
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// Log.d("file not found", "missing file");
	// read = null;
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// Log.d("error in IO", "missing file");
	// read = null;
	// e.printStackTrace();
	// } catch (Exception e) {
	// read = null;
	// }
	//
	// try {
	// fis.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return read;
	//
	// }
	//
	// private ArrayList<String> generateArraylist(String filename, String html,
	// double basePrice) {
	//
	// ArrayList<String> temp = new ArrayList<String>();
	//
	// Document doc = Jsoup.parse(html);
	// Element master = doc.select("ul.c-list").first();
	//
	// for (Element element : master.children()) {
	// // Here you can acccess all childs of your div
	// Elements name = element.select("h5 a[title]");
	// Elements price = element.select("span.price");
	// double parsedPrice = Double.parseDouble(price.text().toString()
	// .substring(3)) / 1.1;
	//
	// if (parsedPrice <= basePrice)
	// temp.add(filename + " -- " + name.attr("title").toString()
	// + " --  PHP " + String.format("%.2f", parsedPrice));
	// }
	//
	// return temp;
	// }

	private void broadcast(ArrayList<String> passList, String message) {
		Intent i = new Intent();
		i.setAction(FoodList.FoodListReceiver.ACTION_FOOD_LIST);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		i.putStringArrayListExtra(UtilClass._listKey, passList);
		i.putExtra(UtilClass._fslmsg, message);
		sendBroadcast(i);
	}

}
