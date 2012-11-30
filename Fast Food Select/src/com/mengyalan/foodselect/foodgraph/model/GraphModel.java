/**
 * @author mklu2 Michael Lu
 */

package com.mengyalan.foodselect.foodgraph.model;

import java.util.ArrayList;

import com.mengyalan.foodselect.data.GraphDataPoint;
import com.mengyalan.foodselect.foodgraph.view.GraphView;

public class GraphModel {

	// Set of Datapoints in the graph
	private ArrayList<GraphDataPoint> mDataPoints = new ArrayList<GraphDataPoint>();

	// Observing view
	GraphView mView;

	public GraphModel(GraphView graphView) {
		// TODO Auto-generated constructor stub
		mView = graphView;
	}

	/**
	 * Finds the closest GraphDataPoint within it to the closest (x,y)
	 * 
	 * @param x
	 *            The x coord of the point in the graph
	 * @param y
	 *            The y coord of the point in the graph
	 * @return The closest GraphDataPoint to (x,y)
	 */
	public GraphDataPoint findDataPoint(float x, float y) {
		float closest_distance = -1;
		GraphDataPoint closestPoint = null;

		for (GraphDataPoint dataPoint : mDataPoints) {
			float distance = (float) (Math.pow(x - dataPoint.getX(), 2) + Math
					.pow(y - dataPoint.getY(), 2));
			if (distance < closest_distance || closest_distance == -1) {
				closestPoint = dataPoint;
				closest_distance = distance;
			}
		}

		return closestPoint;
	}

	/**
	 * Adds a new GraphDataPoint to the GraphModel and tells GraphView to update
	 * 
	 * @param point
	 *            The GraphDataPoint to add
	 */
	public void addDataPoint(GraphDataPoint point) {
		mDataPoints.add(point);
		updateView();
	}

	/**
	 * Removes the specific GraphDataPoint from the GraphModel
	 * 
	 * @param point
	 *            The GraphDataPoint to remove
	 */
	public void removeDataPoint(GraphDataPoint point) {
		// TODO: implement
	}

	/**
	 * Removes the specific GraphDataPoint from the GraphModel
	 * 
	 * @param point
	 *            The GraphDataPoint to remove
	 */
	public void removeDataPoint(float x, float y) {
		// TODO: implement
	}

	/**
	 * Prompts the view to update with specific information
	 */
	public void updateView() {
		mView.update(mDataPoints);
	}

	public GraphView getView() {
		return mView;
	}

	public void setView(GraphView view) {
		mView = view;
	}
}
