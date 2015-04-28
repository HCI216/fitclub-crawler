package com.nju.FitClubReptile.TraditionalFoods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Test {

	private static TraditionalFoodCatetoryReptile foodCategory;
	private static TraditionalFoodsPage food;
	private static TraditionalFoodUrlList foodUrl;

	public Test() {
		this.foodUrl = new TraditionalFoodUrlList();
		this.foodCategory = new TraditionalFoodCatetoryReptile();
		this.food = new TraditionalFoodsPage();
	}

	public static Connection getCon() throws Exception{
		String dbDriver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost/fitclub?seUnicode=true&characterEncoding=UTF-8";
		String username = "root";
		String password = "nzbbzlks";
		Class.forName(dbDriver).newInstance();
		return DriverManager.getConnection(url, username, password);
	}

	public static void writeToSql(TraditionalFoodsModel foodsModel) throws Exception{
		Connection con = getCon();
		String query = "insert into traditionalFood value (?,?,?,?,?,?,?,?,?)";
		PreparedStatement stmt =  con.prepareStatement(query);
		
		printModel(foodsModel);
		
		stmt.setString(1, foodsModel.getFoodID());
		stmt.setString(2, foodsModel.getFoodName());
		stmt.setString(3, foodsModel.getFoodAmount());
		stmt.setDouble(4, foodsModel.getCalorie());
		stmt.setDouble(5, foodsModel.getFat());
		stmt.setDouble(6, foodsModel.getCarbohydrate());
		stmt.setDouble(7, foodsModel.getProtein());
		stmt.setString(8, foodsModel.getSmallCategory());
		stmt.setString(9, foodsModel.getBigCategory());
		stmt.execute();
	}

	public static void printModel(TraditionalFoodsModel foodsModel){
		System.out.println("ID = " + foodsModel.getFoodID());

		System.out.println("Name = " + foodsModel.getFoodName());

		System.out.println("Amount = " + foodsModel.getFoodAmount());

		System.out.println("Calorie = " + foodsModel.getCalorie());

		System.out.println("Fat = " + foodsModel.getFat());

		System.out.println("Carbohydrate = " + foodsModel.getCarbohydrate());

		System.out.println("Protein = " + foodsModel.getProtein());

		System.out.println("SmallCategory = " + foodsModel.getSmallCategory());

		System.out.println("BigCategory = " + foodsModel.getBigCategory());
	}
	
	public static void main(String[] args) throws Exception {

		Test test = new Test();
		
		int ppp = 1;

		HashMap<String, String> foodCategoryList = foodCategory.getURL();

		List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(
				foodCategoryList.entrySet());

		for (int i = 0; i < list.size(); i++) {
			String bigCategory = list.get(i).getKey();
			String link = list.get(i).getValue();
			HashMap<String, HashMap<String, String>> ll = foodUrl.run(link);

			List<Map.Entry<String, HashMap<String, String>>> fList = new ArrayList<Map.Entry<String, HashMap<String, String>>>(
					ll.entrySet());
			for (int j = 0; j < fList.size(); j++) {
				Map.Entry<String, HashMap<String, String>> pp = fList.get(j);
				String smallCategory = pp.getKey();

				HashMap<String, String> s = pp.getValue();
				List<Map.Entry<String, String>> tmpList = new ArrayList<Map.Entry<String, String>>(
						s.entrySet());
				for (int m = 0; m < tmpList.size(); m++) {
					String foodName = tmpList.get(m).getKey();
					String foodUrl = tmpList.get(m).getValue();
					TraditionalFoodsModel foodsModel = food.run(foodUrl);
					foodsModel.setFoodName(foodName);
					foodsModel.setSmallCategory(smallCategory);
					foodsModel.setBigCategory(bigCategory);
					foodsModel.setFoodID(ppp + "");
					writeToSql(foodsModel);
					ppp++;
					
					if(ppp % 100 == 0){
						new Thread().sleep(20000);
					}
					
				}
			}

		}

	}

}
