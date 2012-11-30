/**
 * @author ymeng7 Yalan Meng
 */

package com.mengyalan.foodselect.data;

import java.util.ArrayList;

public class RestaurantItem {
	private static int _count = 0;

	private String _id;
	private String _name;
	private ArrayList<FoodItem> _menu;
	private String _sourceURL;
	private String _lastUpdated;
	private ArrayList<String> _type;
	public String jsonFileName;

	/**
	 * 
	 * @param content
	 *            name
	 * @param filename
	 *            json file name in assets
	 */
	public RestaurantItem(String content, String filename) {
		this.setId("" + _count++);
		this._name = content;
		this.jsonFileName = filename;
		this._menu = new ArrayList<FoodItem>();
		this._type = new ArrayList<String>();
	}

	/**
	 * Get the String representation of the item
	 */
	@Override
	public String toString() {
		return _name;
	}

	/**
	 * Get the id of the item
	 * 
	 * @return
	 */
	public String getId() {
		return _id;
	}

	/**
	 * Set the id of the item
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this._id = id;
	}

	/**
	 * Get the name of the restaurant
	 * 
	 * @return
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Set the name of the restaurant
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this._name = name;
	}

	/**
	 * Get the url of the restaurant menu's source
	 * 
	 * @return
	 */
	public String getSourceURL() {
		return _sourceURL;
	}

	/**
	 * Set the url of the restaurant menu's source
	 * 
	 * @param _sourceURL
	 */
	public void setSourceURL(String _sourceURL) {
		this._sourceURL = _sourceURL;
	}

	/**
	 * Get the menu of the restaurant
	 * 
	 * @return
	 */
	public ArrayList<FoodItem> getMenus() {
		return _menu;
	}

	/**
	 * Get all the food of certain type
	 * 
	 * @param type
	 *            the type being searched for
	 * @return a list of result
	 */
	public ArrayList<FoodItem> getAllFoodOfType(String type) {
		ArrayList<FoodItem> result = new ArrayList<FoodItem>();
		for (FoodItem food : getMenus()) {
			if (food.getType().equals(type)) {
				result.add(food);
			}
		}

		return result;
	}

	/**
	 * Add a fooditem to the restaurant's menu
	 * 
	 * @param item
	 */
	public void addFoodItem(FoodItem item) {
		this._menu.add(item);
	}

	/**
	 * Get the last updated date
	 * 
	 * @return
	 */
	public String getLastUpdated() {
		return _lastUpdated;
	}

	/**
	 * Set the last updated date
	 * 
	 * @param _lastUpdated
	 */
	public void setLastUpdated(String _lastUpdated) {
		this._lastUpdated = _lastUpdated;
	}

	public ArrayList<String> getTypes() {
		return _type;
	}

	/**
	 * Get the i-th type of food on the menu
	 * 
	 * @param i
	 * @return
	 */
	public String getType(int i) {
		return _type.get(i);
	}

	/**
	 * Add a type of food to the menu
	 * 
	 * @param type
	 */
	public void addType(String type) {
		this._type.add(type);
	}

	/**
	 * Get a certain FoodItem in a certain type of food based on its position
	 * 
	 * @param type
	 * @param pos
	 * @return
	 */
	public FoodItem getFoodFromType(String type, int pos) {
		return getAllFoodOfType(type).get(pos);
	}
}