/**
 * @author ymeng7 Yalan Meng
 */

package com.mengyalan.foodselect.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantListContent {

	public static List<RestaurantItem> ITEMS = new ArrayList<RestaurantItem>();
	public static Map<String, RestaurantItem> ITEM_MAP = new HashMap<String, RestaurantItem>();

	static {
		addItem(new RestaurantItem("Del Taco", "deltaco.json"));
		addItem(new RestaurantItem("Wendy's", "wendys.json"));
		addItem(new RestaurantItem("Chick-fil-A", "chickfila.json"));
		addItem(new RestaurantItem("Chipotle", "chipotle.json"));
		addItem(new RestaurantItem("IHOP", "ihop.json"));
		addItem(new RestaurantItem("McDonalds", "mcdonalds.json"));
		addItem(new RestaurantItem("Steak and Shake", "steakandshake.json"));
		addItem(new RestaurantItem("Subway", "subway.json"));
		addItem(new RestaurantItem("Papa John's", "papajohns.json"));
		addItem(new RestaurantItem("Pizza Hut", "pizzahut.json"));
		addItem(new RestaurantItem("Panera", "panera.json"));
		addItem(new RestaurantItem("Domino's Pizza", "dominospizza.json"));
		addItem(new RestaurantItem("Burger King", "burgerking.json"));
		addItem(new RestaurantItem("KFC", "kfc.json"));
		addItem(new RestaurantItem("Taco Bell", "tacobell.json"));
		addItem(new RestaurantItem("Dairy Queen", "dairyqueen.json"));
		addItem(new RestaurantItem("Cosi", "cosi.json"));
		addItem(new RestaurantItem("Arbys", "arbys.json"));
		addItem(new RestaurantItem("Panda Express", "pandaexpress.json"));
	}

	private static void addItem(RestaurantItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.getId(), item);
	}
}
