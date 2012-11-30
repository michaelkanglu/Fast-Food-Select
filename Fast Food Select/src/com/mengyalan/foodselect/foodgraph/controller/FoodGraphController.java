/**
 * @author mklu2 Michael Lu
 */

package com.mengyalan.foodselect.foodgraph.controller;

import com.mengyalan.foodselect.data.FoodItem;
import com.mengyalan.foodselect.foodgraph.model.FoodGraphModel;
import com.mengyalan.foodselect.foodgraph.view.GraphView;

public class FoodGraphController extends GraphController {

	public FoodGraphController(GraphView graphView) {
		super(graphView);
		setModel(new FoodGraphModel(graphView));
	}

	/**
	 * Action to perform on a (short)press. Currently causes the model to select a datapoint
	 */
	@Override
	public void onActionPress(float x, float y) {
		((FoodGraphModel) getModel()).removeFoodFromMeal(x, y);
	}
	
	
	@Override
	public void onActionLongPress(float x, float y) {
		((FoodGraphModel) getModel()).addFoodToMeal(x, y);
	}
	/**
	 * Action to perform when the Add button is pressed. Currently adds the current selection to the meal
	 * @param selection
	 * 	FoodItem to add to the Model's meal
	 */
	public void onAddButtonPress(FoodItem selection) {
		((FoodGraphModel) getModel()).addFoodToMeal(selection);
	}
	
	/**
	 * Action to perform when the Remove button is pressed. Currently removes the current selection fromt he meal
	 * @param selection
	 * 	FoodItem to remove from the Model's meal
	 */
	public void onRemoveButtonPress(FoodItem selection) {
		((FoodGraphModel)getModel()).removeFoodFromMeal(selection);
	}
}
