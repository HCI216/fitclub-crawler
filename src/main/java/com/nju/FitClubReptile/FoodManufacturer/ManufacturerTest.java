package com.nju.FitClubReptile.FoodManufacturer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nju.FitClubReptile.TraditionalFoods.TraditionalFoodsModel;

public class ManufacturerTest {

	private static ManufacturerFoods manufacturefoods = null;
	private static BrandsFoodsList brandsList = null;
	private static BrandsUrl brandsUrl = null;

	static {
		manufacturefoods = new ManufacturerFoods();
		brandsList = new BrandsFoodsList();
		brandsUrl = new BrandsUrl();
	}

	public static Connection getCon() throws Exception {
		String dbDriver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost/fitclub?seUnicode=true&characterEncoding=UTF-8";
		String username = "root";
		String password = "nzbbzlks";
		Class.forName(dbDriver).newInstance();
		return DriverManager.getConnection(url, username, password);
	}

	public static void writeToBrandSql(ManufacturerFoodsModel foodsModel,
			String category) throws Exception {
		Connection con = getCon();
		String query = "";
		if (category.equals("brands"))
			query = "insert into brandsFood value (?,?,?,?,?,?,?,?)";
		else if (category.equals("restaurant")) {
			query = "insert into restaurantFood value (?,?,?,?,?,?,?,?)";
		} else if (category.equals("market")) {
			query = "insert into marketFood value (?,?,?,?,?,?,?,?)";
		}
		PreparedStatement stmt = con.prepareStatement(query);

		printModel(foodsModel);

		stmt.setString(1, foodsModel.getFoodID());
		stmt.setString(2, foodsModel.getFoodName());
		stmt.setString(3, foodsModel.getFoodAmount());
		stmt.setDouble(4, foodsModel.getCalorie());
		stmt.setDouble(5, foodsModel.getFat());
		stmt.setDouble(6, foodsModel.getCarbohydrate());
		stmt.setDouble(7, foodsModel.getProtein());
		stmt.setString(8, foodsModel.getBrands());
		stmt.execute();
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

	public static void getFoods(String category) throws Exception {
		HashMap<String, String> brandsUrls = brandsUrl.run(category);
		new Thread().sleep(20000);
		List<Map.Entry<String, String>> brands = new ArrayList<Map.Entry<String, String>>(
				brandsUrls.entrySet());

		int ID = 1;

		for (int i = 0; i < brands.size(); i++) {
			String brandsName = brands.get(i).getKey();
			String brandsUrl = brands.get(i).getValue();

			HashMap<String, HashMap<String, String>> foodsList = brandsList
					.run(brandsUrl);

			new Thread().sleep(2000);

			List<Map.Entry<String, HashMap<String, String>>> foods = new ArrayList<Map.Entry<String, HashMap<String, String>>>(
					foodsList.entrySet());

			for (int j = 0; j < foods.size(); j++) {
				String foodsName = foods.get(j).getKey();
				HashMap<String, String> map = foods.get(j).getValue();

				List<Map.Entry<String, String>> aU = new ArrayList<Map.Entry<String, String>>(
						map.entrySet());

				for (int k = 0; k < aU.size(); k++) {
					String foodsAmount = aU.get(k).getKey();
					String url = aU.get(k).getValue();

					ManufacturerFoodsModel food = manufacturefoods.run(url);

					food.setFoodID(ID + "");
					ID++;
					food.setBrands(brandsName);
					food.setFoodAmount(foodsAmount);
					food.setFoodName(foodsName);
					writeToBrandSql(food, category);
					if (ID % 50 == 0) {
						new Thread().sleep(20000);
					}

				}

			}

		}
	}

	public static void main(String[] args) throws Exception {

//		getFoods("brand");
		getFoods("restaurant");
		getFoods("market");
	}

}
