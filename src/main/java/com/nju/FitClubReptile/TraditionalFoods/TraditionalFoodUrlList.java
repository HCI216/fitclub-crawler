package com.nju.FitClubReptile.TraditionalFoods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TraditionalFoodUrlList {

	private HashMap<String, HashMap<String, String>> foodsUrl = new HashMap<String, HashMap<String, String>>();

	private String allContent = "";

	public void getAllContent(String url) throws Exception {
		Document doc = Jsoup.connect(url).userAgent("Mozilla")
				.data("jquery", "java").cookie("auth", "token").timeout(5000)
				.get();
		allContent = doc.toString();
	}

	public void getAllFood() throws Exception {
		Document doc = Jsoup.parse(allContent);
		Elements elements = doc.getElementsByClass("secHolder");
		doc = Jsoup.parse(elements.toString());
		Elements category = doc.getElementsByTag("h2");
		Elements foodLinks = doc.getElementsByClass("food_links");

		for (int i = 0; i < foodLinks.size(); i++) {
			doc = Jsoup.parse(category.get(i).toString());
			String pp = doc.getElementsByTag("a").text();
			doc = Jsoup.parse(foodLinks.get(i).toString());
			HashMap<String, String> foodList = getFoodsMap(doc
					.getElementsByTag("a"));
			foodsUrl.put(pp, foodList);
		}

	}

	public HashMap<String, String> getFoodsMap(Elements foodsContent) {
		HashMap<String, String> foodLink = new HashMap<String, String>();
		for (Element element : foodsContent) {
			String site = "http://www.fatsecret.cn" + element.attr("href");
			String name = element.text();
			foodLink.put(name, site);
		}
		return foodLink;
	}

	public void printIt(String content) {
		System.out.println(content);
	}

	public void printMap() {
		List<Map.Entry<String, HashMap<String, String>>> fList = new ArrayList<Map.Entry<String, HashMap<String, String>>>(
				foodsUrl.entrySet());
		for (int i = 0; i < fList.size(); i++) {
			Map.Entry<String, HashMap<String, String>> pp = fList.get(i);
			String name = pp.getKey();
			HashMap<String, String> list = pp.getValue();
			System.out.println("-----------------------------------");
			System.out.println(name);
			List<Map.Entry<String, String>> tmpList = new ArrayList<Map.Entry<String, String>>(
					list.entrySet());
			for (int j = 0; j < tmpList.size(); j++) {
				String foodName = tmpList.get(j).getKey();
				String foodUrl = tmpList.get(j).getValue();
				System.out.println(foodName + " : " + foodUrl);
			}
		}
	}

	public HashMap<String, HashMap<String, String>> run(String url)
			throws Exception {
		getAllContent(url);
		getAllFood();
		return foodsUrl;
	}

}
