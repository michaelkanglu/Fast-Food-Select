/**
 * @author ymeng7 Yalan Meng
 */
package com.mengyalan.foodselect;

import com.mengyalan.foodselect.data.FoodItem;
import com.mengyalan.foodselect.data.RestaurantItem;
import com.mengyalan.foodselect.data.RestaurantListContent;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Activity page that presents the menu info
 */
public class FoodMenuActivity extends FragmentActivity implements
		ActionBar.TabListener {

	protected static final String TYPE = "type";
	protected static final String POSITION = "position";
	public static String RESTAURANT_ID = "rest_id";
	private RestaurantItem rItem;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = this.getIntent().getExtras();
		String id = (String) extras.get(RESTAURANT_ID);

		rItem = RestaurantListContent.ITEM_MAP.get(id);

		setContentView(R.layout.food_item_activity);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(rItem,
				getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab.
		// We can also use ActionBar.Tab#select() to do this if we have a
		// reference to the
		// Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.food_item_activity, menu);
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

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		RestaurantItem rItem;

		public SectionsPagerAdapter(RestaurantItem rItem, FragmentManager fm) {
			super(fm);
			this.rItem = rItem;
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = new SubSectionFragment();
			Bundle args = new Bundle();
			args.putInt(SubSectionFragment.ARG_SECTION_NUMBER, i);
			args.putString(FoodMenuActivity.RESTAURANT_ID, rItem.getId());
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return rItem.getTypes().size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return rItem.getTypes().get(position);
		}
	}

	/**
	 * The fragment representing a sub section of the menu. Generated based on
	 * the JSON provided.
	 */
	public static class SubSectionFragment extends Fragment {
		public SubSectionFragment() {
		}

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			Bundle args = getArguments();
			final RestaurantItem rItem = RestaurantListContent.ITEM_MAP
					.get(args.getString(FoodMenuActivity.RESTAURANT_ID));
			int type_num = args.getInt(ARG_SECTION_NUMBER);
			final String type = rItem.getType(type_num);

			ListView lv = new ListView(getActivity());
			// populate with a list
			lv.setAdapter(new ArrayAdapter<FoodItem>(getActivity(),
					android.R.layout.simple_list_item_1, rItem
							.getAllFoodOfType(type)));
			lv.setOnItemClickListener(new OnItemClickListener() {
				/**
				 * Pass data to the food item detail activity
				 */
				@Override
				public void onItemClick(AdapterView<?> adapterView, View v,
						int pos, long pos_long) {
					
					Intent intent = new Intent();
					intent.setClass(getActivity(),
							FoodItemDetailsActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(RESTAURANT_ID, rItem.getId());
					bundle.putString(TYPE, type);
					bundle.putInt(POSITION, pos);
					intent.putExtras(bundle);
					
					startActivity(intent);
					getActivity().finish();
				}

			});

			return lv;
		}
	}
}
