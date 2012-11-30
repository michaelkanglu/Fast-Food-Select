/**
 * @author mklu2 Michael Lu
 */

package com.mengyalan.foodselect.foodgraph.model;

import android.annotation.SuppressLint;
import com.mengyalan.foodselect.data.FoodItem;
import com.mengyalan.foodselect.data.MealItem;
import com.mengyalan.foodselect.foodgraph.view.FoodGraphView;
import com.mengyalan.foodselect.foodgraph.view.GraphView;

public class FoodGraphModel extends GraphModel {

	MealItem mMeal;

	public FoodGraphModel(GraphView graphView) {
		super(graphView);
		mMeal = new MealItem();
	}

	// stops it from whining about casting Math.sqrt to float. Loss of precision not a big issue
	@SuppressLint("FloatMath")
	/**
	 * Selects a FoodItem and update FoodGraphView with new selection
	 * @param x
	 * 	The x coordinate of the selection in Graph coordinates
	 * @param y
	 * 	The y coordinate of the selection in Graph coordiantes
	 * @return
	 * 	The foodItem selected. Returns null if there was no item sufficiently close to the selection point
	 */
	public FoodItem selectDataPoint(float x, float y) {
		FoodItem selection = (FoodItem) findDataPoint(x, y);
		float distance = (float) (Math.pow(x - selection.getX(), 2) + Math.pow(
				y - selection.getY(), 2));
		distance = (float) Math.sqrt(distance);

		if (distance < 1) {
			((FoodGraphView) getView()).updateSelection(selection);
			return selection;
		}
		return null;
	}

	/**
	 * Adds a FoodItem to the Meal based on the selection coordinates
	 * @param x
	 * 	The x coordinate of the selection in Graph coordinates
	 * @param y
	 * 	The y coordinate of the selection in Graph coordiantes
	 */
	public void addFoodToMeal(float x, float y) {
		FoodItem selection = selectDataPoint(x, y);
		if (selection != null) {
			mMeal.addFood(selection);
			((FoodGraphView) getView()).updateMeal(mMeal);
		}
	}

	/**
	 * Removes a FoodItem to the Meal based on the selection coordinates
	 * @param x
	 * 	The x coordinate of the selection in Graph coordinates
	 * @param y
	 * 	The y coordinate of the selection in Graph coordiantes
	 */
	public void removeFoodFromMeal(float x, float y) {
		FoodItem selection = selectDataPoint(x, y);
		if (selection != null) {
			mMeal.removeFood(selection);
			((FoodGraphView) getView()).updateMeal(mMeal);
		}
	}
	
	/**
	 * Adds a FoodItem to the Meal
	 * @param food
	 * 	FoodItem to add
	 */
	public void addFoodToMeal(FoodItem food) {
		if(food != null) {
			mMeal.addFood(food);
			((FoodGraphView) getView()).updateMeal(mMeal);
		}
	}
	
	/**
	 * Removes a FoodItem to the Meal
	 * @param food
	 * 	FoodItem to remove
	 */
	public void removeFoodFromMeal(FoodItem food) {
		if(food != null) {
			mMeal.removeFood(food);
			((FoodGraphView) getView()).updateMeal(mMeal);
		}
	}
}
