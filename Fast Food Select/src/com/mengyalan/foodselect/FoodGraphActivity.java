package com.mengyalan.foodselect;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.mengyalan.foodselect.data.FoodItem;
import com.mengyalan.foodselect.data.RestaurantItem;
import com.mengyalan.foodselect.data.RestaurantListContent;
import com.mengyalan.foodselect.foodgraph.controller.GraphController;
import com.mengyalan.foodselect.foodgraph.model.GraphModel;
import com.mengyalan.foodselect.foodgraph.view.GraphView;

public class FoodGraphActivity extends Activity {

	RestaurantItem rItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_graph);

        // Get the RestaurantItem from bundle
		Bundle extras = this.getIntent().getExtras();
		String id = (String) extras.get(FoodMenuActivity.RESTAURANT_ID);
		rItem = RestaurantListContent.ITEM_MAP.get(id);
		
        initialzeGraph();
    }

    private void initialzeGraph() {
    	
		GraphView graphView = (GraphView) findViewById(R.id.food_view_graph_canvas);
		GraphController gVController = graphView.getController();
		GraphModel gModel = gVController.getModel();
		ArrayList<FoodItem> menus = rItem.getMenus();
		for (int i = 0; i < rItem.getMenus().size(); i++) {
			gModel.addDataPoint(menus.get(i));
		}

	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_food_graph, menu);
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
