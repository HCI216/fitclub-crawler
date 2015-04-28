package com.nju.FitClubReptile.FoodManufacturer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BrandsUrl {

	private HashMap<String, String> brandsList ;
	private String allContent = "";

	public void getAllContent(String url) throws Exception {
		Document doc = Jsoup.connect(url).userAgent("Mozilla")
				.data("jquery", "java").cookie("auth", "token").timeout(50000)
				.get();
		Elements elements = doc.getElementsByClass("leftCellContent");
		allContent = elements.toString();
	}

	public void getAllBrands() {
		Document doc = Jsoup.parse(allContent);
		Elements elements = doc.getElementsByTag("h2");
		for (Element element : elements) {
			doc = Jsoup.parse(element.toString());
			Elements es = doc.getElementsByTag("a");
			// System.out.println(es.toString());
			String url = "http://www.fatsecret.cn" + es.attr("href").toString();
			String brandName = es.attr("title").toString();
			brandsList.put(brandName, url);
		}
	}

	public  void printMap(HashMap<String, String> brandsLists) {
		List<Map.Entry<String, String>> tmpList = new ArrayList<Map.Entry<String, String>>(
				brandsLists.entrySet());
		for (int i=0;i<tmpList.size();i++) {
			String key = tmpList.get(i).getKey();
			String value = tmpList.get(i).getValue();
			System.out.println(key + ":" + value);
		}
	}

	public int getPageNum() {
		Document doc = Jsoup.parse(allContent);
		Elements elements = doc.getElementsByClass("searchResultsPaging");
		doc = Jsoup.parse(elements.toString());
		elements = doc.getElementsByTag("a");
		return elements.size();
	}

	public HashMap<String, String> run(String category) throws Exception {
		char ip = 'a';
		brandsList = new HashMap<String, String>();
		String cat  = "";
		if(category.equals("brand")){
			cat = "1";
		}
		if(category.equals("restaurant")){
			cat = "2";
		}
		if(category.equals("market")){
			cat = "3";
		}
		if(category.equals("others")){
			cat = "5";
		}
		for (int i = 0; i <= 26; i++) {
			
			char ss = ((char) (ip + i));
			
			if(i == 26) ss = '*';
			
			String url = "http://www.fatsecret.cn/Default.aspx?pa=brands&pg=0&f="
					+ ss + "&t=" + cat;
			getAllContent(url);
			int pageNum = getPageNum();
			for (int j = 0; j < pageNum; j++) {
				String newUrl = "http://www.fatsecret.cn/Default.aspx?pa=brands&pg="
						+ j + "&f=" + ss + "&t=" + cat;
				getAllContent(newUrl);
				getAllBrands();
			}
		}
//		printMap(brandsList);
		return brandsList;

	}
}
