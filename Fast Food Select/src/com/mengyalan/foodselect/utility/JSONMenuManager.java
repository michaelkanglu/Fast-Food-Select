/**
 * @author ymeng7 Yalan Meng
 * Parse JSON menu file
 */

package com.mengyalan.foodselect.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mengyalan.foodselect.data.FoodItem;
import com.mengyalan.foodselect.data.RestaurantItem;

import android.R;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class JSONMenuManager {

	public static final String START_PARSING = "Start parsing...";
	public static final String NULL_POINTER_DETECTED = "Null pointer detected!";
	public static final String MENU = "Menu";
	public static final String LAST_UPDATED = "Last Updated";
	public static final String SOURCE = "Source";
	public static final String SODIUM = "Sodium";
	public static final String SATURATED_FAT = "Saturated Fat";
	public static final String PROTEIN = "Protein";
	public static final String FIBER = "Fiber";
	public static final String TOTAL_CALORIES = "Total Calories";
	public static final String TYPE = "Type";
	public static final String FOOD_ITEM = "Food item";

	/**
	 * Helper function to get the JSON string from assets folder
	 * 
	 * @param appContext
	 * @param filename
	 * @return
	 */
	protected static String getJSONString(Context appContext, String filename) {
		InputStream is;
		try {
			is = appContext.getResources().getAssets().open(filename);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			return new String(buffer);
		} catch (IOException e) {
			Log.i("stack trace", e.getStackTrace().toString());
		}
		// if error occurred
		return "";
	}

	/**
	 * Parse the RestaurantItem's data from JSON
	 * 
	 * @param appContext
	 *            context of the application
	 * @param rItem
	 *            RestaurantItem
	 */
	public static void parseJSON(Context appContext, RestaurantItem rItem) {
		if (rItem == null) {
			Toast.makeText(appContext, NULL_POINTER_DETECTED, Toast.LENGTH_LONG)
					.show();
		} else {
			Toast.makeText(appContext, START_PARSING, Toast.LENGTH_LONG).show();

			String bufferString = getJSONString(appContext, rItem.jsonFileName);
			// Parsing starts here
			try {
				JSONObject jsonRoot = new JSONObject(bufferString);
				rItem.setSourceURL(jsonRoot.getString(SOURCE));
				rItem.setLastUpdated(jsonRoot.getString(LAST_UPDATED));

				JSONArray menuArray = jsonRoot.getJSONArray(MENU);
				JSONArray typeArray = jsonRoot.getJSONArray(TYPE);

				for (int i = 0; i < typeArray.length(); i++) {
					rItem.addType(typeArray.get(i).toString());
				}

				for (int i = 0; i < menuArray.length(); i++) {

					// parse an Object from index i in the JSONArray
					JSONObject jObj = menuArray.getJSONObject(i);
					String name = jObj.getString(FOOD_ITEM);
					String type = jObj.getString(TYPE);
					int cal = Integer.parseInt(jObj.getString(TOTAL_CALORIES));
					int fiber = Integer.parseInt(jObj.getString(FIBER));
					int protein = Integer.parseInt(jObj.getString(PROTEIN));
					int sf = Integer.parseInt(jObj.getString(SATURATED_FAT));
					int na = Integer.parseInt(jObj.getString(SODIUM));

					FoodItem food = new FoodItem(name, cal, fiber, protein, sf,
							na, type, appContext);
					// save the fooditem
					rItem.addFoodItem(food);
				}

			} catch (JSONException e1) {
				Log.i("stack trace", e1.getStackTrace().toString());
			}
		}
	}
}
