package com.nju.FitClubReptile.FoodManufacturer;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ManufacturerFoods {

	private String webContent = "";
	private String detailContent = "";

	private ManufacturerFoodsModel foodsModel = new ManufacturerFoodsModel();

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
		}
	}

	public Double formatString(String input) {
		char[] sList = input.toCharArray();
		String ss = "";
		for (int i = 0; i < sList.length - 1; i++) {
			ss += sList[i];
		}
		return Double.parseDouble(ss);
	}

	public ManufacturerFoodsModel run(String url) throws Exception {

		getSiteContent(url);
		getDetails();
		splitNutritions();

		return foodsModel;
	}

	public static void printModel(ManufacturerFoodsModel foodsModel) {
		System.out.println("ID = " + foodsModel.getFoodID());

		System.out.println("Name = " + foodsModel.getFoodName());

		System.out.println("Amount = " + foodsModel.getFoodAmount());

		System.out.println("Calorie = " + foodsModel.getCalorie());

		System.out.println("Fat = " + foodsModel.getFat());

		System.out.println("Carbohydrate = " + foodsModel.getCarbohydrate());

		System.out.println("Protein = " + foodsModel.getProtein());

		System.out.println("Brands = " + foodsModel.getBrands());
	}
	
//	public static void main(String[] args) throws Exception {
//		ManufacturerFoods manufacturer = new ManufacturerFoods();
//		ManufacturerFoodsModel foods = manufacturer
//				.run("http://www.fatsecret.cn/%E7%83%AD%E9%87%8F%E8%90%A5%E5%85%BB/%E6%84%9B%E4%B9%8B%E5%91%B3/%E7%9C%9F%E7%8F%A0%E8%96%8F%E4%BB%81%E9%9C%B2/100%E6%AF%AB%E5%8D%87");
//		printModel(foods);
//		
//	}

}
