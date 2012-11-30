/**
 * @author ymeng7 Yalan Meng
 */
package com.mengyalan.foodselect;

import com.mengyalan.foodselect.data.FoodItem;
import com.mengyalan.foodselect.data.RestaurantItem;
import com.mengyalan.foodselect.data.RestaurantListContent;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

/**
 * Activity page that present the detailed nutritional information for a food
 * item
 */
public class FoodItemDetailsActivity extends Activity {

	public static final String SATURATED_FAT_PERCENT = "Saturated Fat%: ";
	public static final String SATURATED_FAT_CONTENT = "Saturated Fat (g/serving): ";
	public static final String SODIUM_500_KCAL = "Sodium/500 kcal: ";
	public static final String SODIUM_MG_SERVING = "Sodium (mg/serving): ";
	public static final String PROTEIN_500_KCAL = "Protein/500 kcal: ";
	public static final String FIBER_500_KCAL = "Fiber/500 kcal: ";
	public static final String PROTEIN_CONTENT = "Protein (g/serving): ";
	public static final String FIBER_CONTENT = "Fiber (g/serving): ";
	public static final String TOTAL_CALORIES = "Total Calories (kcal): ";
	public static final String NUTRITION_FACTS = "Nutrition Facts:\n    ";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food_item_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle extras = this.getIntent().getExtras();
		String type = extras.getString(FoodMenuActivity.TYPE);
		int pos = extras.getInt(FoodMenuActivity.POSITION);
		String id = (String) extras.get(FoodMenuActivity.RESTAURANT_ID);
		// Retrieve the food item from a restaurant
		RestaurantItem rItem = RestaurantListContent.ITEM_MAP.get(id);
		FoodItem food = rItem.getFoodFromType(type, pos);

		populateWithFoodItem(food);
	}

	private void populateWithFoodItem(FoodItem food) {
		if (food == null)
			return;
		// SET NAME
		((TextView) findViewById(R.id.nutrition_fact)).setText(NUTRITION_FACTS
				+ food.getName());
		// SET TOTAL CALORIES
		((TextView) findViewById(R.id.calorie_content)).setText(TOTAL_CALORIES
				+ food.getTotalCalories());
		// SET FIBER INFO
		((TextView) findViewById(R.id.fiber_content)).setText(FIBER_CONTENT
				+ food.getFiber());
		((TextView) findViewById(R.id.fb_500)).setText(FIBER_500_KCAL
				+ food.getFiberPer500());
		// SET PROTEIN INFO
		((TextView) findViewById(R.id.protein_content)).setText(PROTEIN_CONTENT
				+ food.getProtein());
		((TextView) findViewById(R.id.pt_500)).setText(PROTEIN_500_KCAL
				+ food.getProteinPer500());
		// SET SODIUM INFO
		((TextView) findViewById(R.id.sodium_content))
				.setText(SODIUM_MG_SERVING + food.getSodium());
		((TextView) findViewById(R.id.na_500)).setText(SODIUM_500_KCAL
				+ food.getNaPer500());
		// SET SATURATED FAT INFO
		((TextView) findViewById(R.id.saturated_fat_content))
				.setText(SATURATED_FAT_CONTENT + food.getSaturatedFat());
		((TextView) findViewById(R.id.sf_500)).setText(SATURATED_FAT_PERCENT
				+ food.getSFpercent() + "%");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.food_item_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
