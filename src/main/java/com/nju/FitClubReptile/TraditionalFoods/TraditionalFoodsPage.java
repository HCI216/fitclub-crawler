package com.nju.FitClubReptile.TraditionalFoods;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TraditionalFoodsPage {

	private String webContent = "";
	private String detailContent = "";

	private TraditionalFoodsModel foodsModel = new TraditionalFoodsModel();

	private void getSiteContent(String site) throws Exception {
		Connection con = Jsoup.connect(site).data("jquery", "java")
				.userAgent("Mozilla").cookie("auth", "token").timeout(50000); 
		Document doc = con.get();
		
		webContent = doc.toString();
	}

	public void getDetails() throws Exception {
		Document doc = Jsoup.parse(webContent);
		Elements elements = doc.getElementsByClass("details");
		detailContent = elements.toString();
	}

	public void splitNutritions() throws Exception {
		Document doc = Jsoup.parse(detailContent);
		Elements elements = doc.getElementsByClass("fact");
		int i = 0;
		for (Element e : elements) {
			doc = Jsoup.parse(e.toString());
			Elements name = doc.getElementsByClass("factTitle");
			Elements value = doc.getElementsByClass("factValue");

			if (i == 0)
				foodsModel.setCalorie(Double.parseDouble(value.text()));
			if (i == 1)
				foodsModel.setFat(formatString(value.text()));
			if (i == 2)
				foodsModel.setCarbohydrate(formatString(value.text()));
			if (i == 3)
				foodsModel.setProtein(formatString(value.text()));

			i++;
			// System.out.println("营养 : [" + name.text() + "] , 值 : ["
			// + value.text() + "]");
		}
	}

	public Double formatString(String input) {
		char[] sList = input.toCharArray();
		String ss = "";
		for (int i = 0; i < sList.length - 1; i++) {
			ss += sList[i];
		}
//		System.out.println(ss);
		return Double.parseDouble(ss);
	}

	public String getSelected() {
		String result = "";
		Document doc = Jsoup.parse(detailContent);
		Elements elements = doc.getElementsByClass("selected");
		for (Element e : elements) {
			doc = Jsoup.parse(e.toString());
			Elements es = doc.getElementsByTag("a");
			for (Element p : es) {
				result = p.text();
				break;
			}
		}
		return result;
	}

	public void getMeasurement() throws Exception {
		Document doc = Jsoup.parse(detailContent);
		Elements elements = doc.getElementsByAttributeValue("valign", "middle");
		for (Element e : elements) {
			doc = Jsoup.parse(e.toString());
			Elements es = doc.getElementsByTag("a");
			int index = 0;
			for (Element p : es) {
				if (index == 0)
					System.out.print("食用量 : [" + p.text() + "] , ");
				else
					System.out.print("卡路里 : [" + p.text() + "]");
				index++;
			}
			System.out.println("");
		}
	}

	public TraditionalFoodsModel run(String url) throws Exception {

		getSiteContent(url);
		getDetails();
		splitNutritions();
//		getMeasurement();
		foodsModel.setFoodAmount(getSelected());

		return foodsModel;
	}

	// public static void main(String[] args) throws Exception {
	// // Foods food = new Foods();
	// //
	// food.run("http://www.fatsecret.cn/%E7%83%AD%E9%87%8F%E8%90%A5%E5%85%BB/%E6%99%AE%E9%80%9A%E7%9A%84/%E8%8D%B7%E5%8C%85%E8%9B%8B");
	// }

}
