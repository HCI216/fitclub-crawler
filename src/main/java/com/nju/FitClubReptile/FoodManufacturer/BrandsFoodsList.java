package com.nju.FitClubReptile.FoodManufacturer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BrandsFoodsList {

	private String allContent = "";
	private HashMap<String, HashMap<String, String>> foodsUrl = null;

	public void getAllContent(String url) throws Exception {
		Document doc = Jsoup.connect(url).userAgent("Mozilla")
				.cookie("auth", "token").data("jquery", "java").timeout(50000)
				.get();
		allContent = doc.toString();
	}

	public void getAllFoodsUrl() {
		Document doc = Jsoup.parse(allContent);
		Elements elements = doc.getElementsByClass("leftCellContent");

		if (elements.size() == 0)
			return;
		doc = Jsoup.parse(elements.toString());
		elements = doc.getElementsByClass("prominent");
		if(elements.size() == 0) return ;

		Elements els = doc.getElementsByClass("borderBottom");
		int tmp = 0;
		for (Element element : elements) {
			String amount = els.get(tmp * 3 + 1).text();
			String url = "http://www.fatsecret.cn" + element.attr("href");
			Elements ell = Jsoup.parse(element.toString())
					.getElementsByTag("b");
			String name = ell.text();
			HashMap<String, String> tmpMap = new HashMap<String, String>();
			tmpMap.put(amount, url);
			foodsUrl.put(name, tmpMap);
			tmp++;
		}
	}

	public void printMap() {
		List<Map.Entry<String, HashMap<String, String>>> fList = new ArrayList<Map.Entry<String, HashMap<String, String>>>(
				foodsUrl.entrySet());
		for (int i = 0; i < fList.size(); i++) {
			Map.Entry<String, HashMap<String, String>> pp = fList.get(i);
			String name = pp.getKey();
			HashMap<String, String> list = pp.getValue();
			System.out.println("-----------------------------------");
			System.out.print(name + " , ");
			List<Map.Entry<String, String>> tmpList = new ArrayList<Map.Entry<String, String>>(
					list.entrySet());
			String amount = tmpList.get(0).getKey();
			String foodUrl = tmpList.get(0).getValue();
			System.out.println(amount + " : " + foodUrl);

		}
	}
	

	public HashMap<String, HashMap<String, String>> run(String url) throws Exception {
		// TODO Auto-generated method stub
		BrandsFoodsList pp = new BrandsFoodsList();
		pp.foodsUrl = new HashMap<String, HashMap<String, String>>();;
		pp.getAllContent(url);
		pp.getAllFoodsUrl();
		return pp.foodsUrl;
	}

}
