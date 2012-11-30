/**
 * @author ymeng7 Yalan Meng
 */
package com.mengyalan.foodselect.data;

import java.util.ArrayList;
import java.util.HashMap;

public class MealItem extends FoodItem implements GraphDataPoint {
	private ArrayList<FoodItem> foodItems;
	private HashMap<FoodItem, Integer> foodCount;

	/**
	 * Constructs a new MealItem with an empty ArrayList of foods
	 */
	public MealItem() {
		super();
		foodItems = new ArrayList<FoodItem>();
		foodCount = new HashMap<FoodItem, Integer>();
	}

	/**
	 * Constructs a new MealItem from a pre-existing FoodItem and automatically inserts that food into the meal
	 * @param food
	 * 	The food to construct from
	 */
	public MealItem(FoodItem food) {
		super();
		foodItems = new ArrayList<FoodItem>();
		addFood(food);
	}

	/**
	 * Add an instance of the FoodItem to the meal
	 * @param food
	 * 	The FoodItem to add to the meal
	 */
	public void addFood(FoodItem food) {
		if(food == null) {
			return;
		}
		if (!foodItems.contains(food)) {
			foodItems.add(food);
			foodCount.put(food, Integer.valueOf(1));
		} else {
			Integer count = foodCount.get(food);
			foodCount.put(food, Integer.valueOf(count.intValue() + 1));
		}

		totalCalories += food.getTotalCalories();
		fiber += food.getFiber();
		protein += food.getProtein();
		saturatedFat += food.getSaturatedFat();
		sodium += food.getSodium();

		drawIcon();
	}

	/**
	 * Remove an instance of the FoodItem from the meal
	 * @param food
	 * 	The FoodItem to remove from the meal
	 */
	public void removeFood(FoodItem food) {
		if(food == null) {
			return;
		}
		if (foodCount.containsKey(food)) {
			//If food exists in the meal
			Integer count = foodCount.get(food);
			foodCount.put(food, Integer.valueOf(count.intValue() - 1));
			
			if (count.intValue() <= 1) {
				//If, after subtraction, count <= 0
				foodItems.remove(food);
			}
			
			if (count.intValue() > 0) {
				//If a food item was deducted from the meal
				totalCalories -= food.getTotalCalories();
				fiber -= food.getFiber();
				protein -= food.getProtein();
				saturatedFat -= food.getSaturatedFat();
				sodium -= food.getSodium();

				drawIcon();
			}
		}
	}

	/**
	 * Gets the number of a specific FoodItem currently in the meal
	 * @param food
	 * 	The FoodItem being looked up
	 * @return
	 *  The number of instances in the meal
	 */
	public int getFoodCount(FoodItem food) {
		if(foodCount.containsKey(food)) {
		return foodCount.get(food).intValue();
		}
		return 0;
	}
	
	/**
	 * Gets the list of FoodItems in the meal
	 * @return
	 */
	public ArrayList<FoodItem> getFoodItems() {
		return foodItems;
	}

	public void setFoodItems(ArrayList<FoodItem> foodItems) {
		this.foodItems = foodItems;
	}
}
