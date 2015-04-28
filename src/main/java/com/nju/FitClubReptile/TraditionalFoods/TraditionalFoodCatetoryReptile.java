package com.nju.FitClubReptile.TraditionalFoods;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TraditionalFoodCatetoryReptile {

	private  String allContent = "";

	private  HashMap<String, String> categoryListUrl = new HashMap<String, String>();

	public  void getAllContent(String url) throws Exception {
		Document doc = Jsoup.connect(url).userAgent("Mozilla")
				.cookie("auth", "token").data("jquery", "java").timeout(5000)
				.get();
		allContent = doc.toString();
	}

	public  void getCategorySites() throws Exception {
		Document doc = Jsoup.parse(allContent);
		Elements elements = doc.getElementsByClass("leftCell");
		doc = Jsoup.parse(elements.toString());
		elements = doc.getElementsByClass("linkHolder");
		doc = Jsoup.parse(elements.toString());
		elements = doc.getElementsByClass("borderBottom");

		for (Element e : elements) {
			doc = Jsoup.parse(e.toString());
			Elements path = doc.getElementsByTag("a");
			for (Element p : path) {
				String name = Jsoup.parse(p.toString()).getElementsByTag("b")
						.text();
				categoryListUrl.put(name, "http://www.fatsecret.cn"
						+ path.attr("href").toString());
			}

		}
	}

	public  void printIt(String content) {
		System.out.println(content);
	}

	public  HashMap<String,String> getURL() throws Exception {
		String url = "http://www.fatsecret.cn/%E7%83%AD%E9%87%8F%E8%90%A5%E5%85%BB/%E7%B1%BB%E5%9E%8B/%E8%9B%8B%E7%B1%BB";
		getAllContent(url);
		getCategorySites();
		return categoryListUrl;
	}

}
