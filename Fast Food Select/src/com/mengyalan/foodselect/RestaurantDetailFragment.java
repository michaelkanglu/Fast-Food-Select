/**
 * @author ymeng7 Yalan Meng
 */

package com.mengyalan.foodselect;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mengyalan.foodselect.data.FoodItem;
import com.mengyalan.foodselect.data.RestaurantItem;
import com.mengyalan.foodselect.data.RestaurantListContent;
import com.mengyalan.foodselect.foodgraph.controller.GraphController;
import com.mengyalan.foodselect.foodgraph.model.GraphModel;
import com.mengyalan.foodselect.foodgraph.view.GraphView;

public class RestaurantDetailFragment extends Fragment {

	public static final String ARG_ITEM_ID = "item_id";

	RestaurantItem rItem;

	public RestaurantDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			rItem = RestaurantListContent.ITEM_MAP.get(getArguments()
					.getString(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_restaurant_detail,
				container, false);

//		GraphView graphView = (GraphView) rootView
//				.findViewById(R.id.food_view_graph_canvas);
//		GraphController gVController = graphView.getController();
//		GraphModel gModel = gVController.getModel();
//		ArrayList<FoodItem> menus = rItem.getMenus();
//		for (int i = 0; i < rItem.getMenus().size(); i++) {
//			gModel.addDataPoint(menus.get(i));
//		}

		if (rItem != null) {
			((TextView) rootView.findViewById(R.id.restaurant_name))
					.setText(rItem.getName());

			((TextView) rootView.findViewById(R.id.sourceURL)).setText(rItem
					.getSourceURL());

			((TextView) rootView.findViewById(R.id.lastUpdated))
					.setText("Last Updated: " + rItem.getLastUpdated());

		}
		return rootView;

	}
}