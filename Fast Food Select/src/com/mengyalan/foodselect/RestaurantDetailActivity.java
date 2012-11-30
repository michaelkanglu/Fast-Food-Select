/**
 * @author ymeng7 Yalan Meng
 */

package com.mengyalan.foodselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Activity page for the detail of a restaurant
 */
public class RestaurantDetailActivity extends FragmentActivity {

	private Button mButton, gButton;

	Button.OnClickListener menuButtonListner = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(RestaurantDetailActivity.this,
					FoodMenuActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString(FoodMenuActivity.RESTAURANT_ID, getIntent()
					.getStringExtra(RestaurantDetailFragment.ARG_ITEM_ID));

			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}
	};
	Button.OnClickListener graphButtonListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(RestaurantDetailActivity.this,
					FoodGraphActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString(FoodMenuActivity.RESTAURANT_ID, getIntent()
					.getStringExtra(RestaurantDetailFragment.ARG_ITEM_ID));

			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_detail);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(
					RestaurantDetailFragment.ARG_ITEM_ID,
					getIntent().getStringExtra(
							RestaurantDetailFragment.ARG_ITEM_ID));
			RestaurantDetailFragment fragment = new RestaurantDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.restaurant_detail_container, fragment).commit();
		}

		mButton = (Button) findViewById(R.id.menu_button);
		mButton.setOnClickListener(menuButtonListner);
		

		gButton = (Button) findViewById(R.id.graph_view_button);
		gButton.setOnClickListener(graphButtonListener);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpTo(this, new Intent(this,
					RestaurantListActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
