/**
 * @author mklu2 Michael Lu
 */

package com.mengyalan.foodselect.foodgraph.controller;

import android.content.Context;

import com.mengyalan.foodselect.data.GraphDataPoint;
import com.mengyalan.foodselect.data.TestPoint;
import com.mengyalan.foodselect.foodgraph.model.GraphModel;
import com.mengyalan.foodselect.foodgraph.view.GraphView;

public class GraphController {
	Context context;

	private GraphModel mModel = null;

	public GraphController(GraphView graphView) {
		context = graphView.getContext();
		setModel(new GraphModel(graphView));
	}

	public GraphModel getModel() {
		return mModel;
	}

	public void setModel(GraphModel model) {
		mModel = model;
	}

	public void onActionPress(float x, float y) {
		// TODO Auto-generated method stub
		GraphDataPoint point = new TestPoint(x, y, context);
		mModel.addDataPoint(point);
	}

	public void onActionLongPress(float x, float y) {
		GraphDataPoint point = new TestPoint(x + 1, y + 1, context);
		mModel.addDataPoint(point);
	}
}
