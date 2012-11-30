/**
 * @author ymeng7 Yalan Meng
 */

package com.mengyalan.foodselect;

import com.mengyalan.foodselect.data.RestaurantListContent;
import com.mengyalan.foodselect.utility.JSONMenuManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Toast;

public class RestaurantListActivity extends FragmentActivity implements
		RestaurantListFragment.Callbacks {

	private boolean mTwoPane;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_list);

		if (findViewById(R.id.restaurant_detail_container) != null) {
			mTwoPane = true;
			((RestaurantListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.restaurant_list))
					.setActivateOnItemClick(true);
		}
	}

	@Override
	public void onItemSelected(String id) {
		JSONMenuManager.parseJSON(getApplicationContext(),
				RestaurantListContent.ITEMS.get(Integer.parseInt(id)));

		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putString(RestaurantDetailFragment.ARG_ITEM_ID, id);
			RestaurantDetailFragment fragment = new RestaurantDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.restaurant_detail_container, fragment)
					.commit();

		} else {
			Intent detailIntent = new Intent(this,
					RestaurantDetailActivity.class);
			detailIntent.putExtra(RestaurantDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}
