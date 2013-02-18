package com.android.foodfindertrial1.necc;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class HtmlParserClass {

	public HtmlParserClass() {
		// TODO Auto-generated constructor stub
	}

	public String neededElements(String htmlCode, String elementIdentifier) {
		Document doc = Jsoup.parse(htmlCode);
		Elements elements = doc.select(elementIdentifier);
		return elements.toString();
	}

	public ArrayList<String> generateJollibeeArrayList(String filename,
			String html, double basePrice) {

		// this method has many variation depending on the food chain might as
		// well create individual
		// generateArrayList for each ;

		ArrayList<String> temp = new ArrayList<String>();

		Document doc = Jsoup.parse(html);
		Element master = doc.select("ul.c-list").first();

		for (Element element : master.children()) {
			// Here you can acccess all childs of your div
			Elements name = element.select("h5 a[title]");
			Elements price = element.select("span.price");
			double parsedPrice = Double.parseDouble(price.text().toString()
					.substring(3)) / 1.1;

			if (parsedPrice <= basePrice)
				temp.add(filename + " -- " + name.attr("title").toString()
						+ " --  PHP " + String.format("%.2f", parsedPrice));
		}

		return temp;
	}

	public ArrayList<String> generateChowkingArrayList(String filename,
			String html, double basePrice) {

		// this method has many variation depending on the food chain might as
		// well create individual
		// generateArrayList for each ;

		ArrayList<String> temp = new ArrayList<String>();

		Document doc = Jsoup.parse(html);
		Element master = doc.select("ol.products-list").first();
		Log.d("master chow", master.toString());
		for (Element element : master.children()) {
			// Here you can acccess all childs of your div
			
//			if(element.select("p.price-from") && element.select("p.price-to")){
//				
//			}
//			
			Elements name = element.select("h2.product-name a");
			Log.d("name chow", name.toString());
			Element price = element.select("span.price").first();
			Log.d("price chow", price.toString());
			
//			Element price2 = element.select("span.price").last();
//			Log.d("price2 chow", price.toString());
			
			double parsedPrice = Double.parseDouble(price.text().toString()
					.substring(3)) / 1.0;

			if (parsedPrice <= basePrice)
				temp.add(filename + " -- " + name.attr("title").toString()
						+ " --  PHP " + String.format("%.2f", parsedPrice));
		}

		return temp;
	}
}
