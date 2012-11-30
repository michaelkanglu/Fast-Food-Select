/**
 * @author mklu2 Michael Lu, ymeng7 Yalan Meng
 */

package com.mengyalan.foodselect.foodgraph.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.mengyalan.foodselect.R;
import com.mengyalan.foodselect.data.FoodItem;
import com.mengyalan.foodselect.data.MealItem;
import com.mengyalan.foodselect.foodgraph.controller.FoodGraphController;

public class FoodGraphView extends GraphView {

	protected static final int DEFAULT_MARGIN = 50;
	private static final int CHART_COLOR_RED = Color.parseColor("#FF4F00");
	private static final int CHART_COLOR_BLUE = Color.parseColor("#604BD8");
	private static final int CHART_COLOR_GREEN = Color.parseColor("#9BED00");
	private static final int CHART_COLOR_YELLOW = Color.parseColor("#FFCB00");
	private static final int BUTTON_MARGIN = 3;
	// Static variables references for the minimum protein/fiber contents to be
	// used in the graph
	public static float MINPROTEIN = 20.0f;
	public static float MINFIBER = 7.0f;

	FoodItem mSelection = null;
	MealItem mMeal = null;

	Bitmap mHighlight = null;
	private int margin = getWidth() / 19;
	protected int ADD_BUTTON_UP_X, ADD_BUTTON_UP_Y, ADD_BUTTON_DOWN_X,
			ADD_BUTTON_DOWN_Y;
	protected String ADD_BUTTON_LABEL_TEXT = "ADD TO MEAL";

	protected int REMOVE_BUTTON_UP_X, REMOVE_BUTTON_UP_Y, REMOVE_BUTTON_DOWN_X,
			REMOVE_BUTTON_DOWN_Y;
	protected String REMOVE_BUTTON_LABEL_TEXT = "REMOVE FOOD";

	protected int INFO_BUTTON_UP_X, INFO_BUTTON_UP_Y, INFO_BUTTON_DOWN_X,
			INFO_BUTTON_DOWN_Y;
	protected String INFO_BUTTON_LABEL_TEXT = "DETAILS";

	// Create references to paints used for plot background
	public FoodGraphView(Context context, AttributeSet aSet) {
		super(context, aSet);
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		setController(new FoodGraphController(this));
		mHighlight = BitmapFactory.decodeResource(getResources(),
				R.drawable.blank_highlight);
	}

	@Override
	public void onDraw(Canvas canvas) {
		// initialize

		if ((mPlotXBegin < 1) || (mPlotYBegin < 1)) {
			initializePlot();
		}
		drawBackground(canvas);

		plotAllDataPoints(canvas);

		drawMeal(canvas);

		drawSelection(canvas);

		drawScales(canvas);
		// this draws the data and the scales

		drawButtons(canvas);
	}

	/**
	 * Initializes all the data of the graph's bounds
	 */
	@Override
	protected void initializePlot() {
		mPlotXBegin = 0 + DEFAULT_MARGIN;
		mPlotXEnd = getWidth() - DEFAULT_MARGIN;
		mPlotYBegin = 0 + getHeight() * 1 / 4;
		mPlotYEnd = getHeight() - DEFAULT_MARGIN;

		mAxisXLBound = 0.0f * MINFIBER;
		mAxisYLBound = 0.0f * MINPROTEIN;
		mAxisXUBound = 4.0f * MINFIBER;
		mAxisYUBound = 4.0f * MINPROTEIN;

		mAxisXDSpan = mAxisXUBound - mAxisXLBound;
		mAxisYDSpan = mAxisYUBound - mAxisYLBound;
		mAxisScaleFactor = 1;

		mAxisXMin = 0.0f;
		mAxisYMin = 0.0f;
		mAxisXMax = 8.0f * MINFIBER;
		mAxisYMax = 10.0f * MINPROTEIN;
	}

	/**
	 * Initializes the values of the tickmarks
	 */
	@Override
	protected void initializeScale() {
		// initialize the tick mark positions on graph load
		int numXTicks = 4;
		int numYTicks = 4;
		mAxisXTicks = new float[numXTicks + 1];
		mAxisYTicks = new float[numYTicks + 1];

		float spaceX = ((float) (mPlotXEnd - mPlotXBegin)) / numXTicks;
		float spaceY = ((float) (mPlotYEnd - mPlotYBegin)) / numYTicks;

		for (int i = 0; i <= numXTicks; i++) {
			mAxisXTicks[i] = mPlotXBegin + (i * spaceX);
		}

		for (int i = 0; i <= numYTicks; i++) {
			mAxisYTicks[i] = mPlotYEnd - (i * spaceY);
		}
	}

	/**
	 * Draws the color regions of the graph's background
	 * 
	 * @param canvas
	 *            Canvas to draw on.
	 */
	private void drawBackground(Canvas canvas) {
		// saving clipping and matrix transform info, as the region painters
		// modify the clips
		canvas.save();
		paintBlueRegion(canvas);
		paintGreenRegion(canvas);
		paintYellowRegion(canvas);
		paintRedRegion(canvas);
		canvas.restore();
	}

	/**
	 * Set the constant values for the buttons
	 */
	private void setButtonConstants() {
		margin = getWidth() / 22;

		ADD_BUTTON_UP_X = margin;
		ADD_BUTTON_UP_Y = 50;
		ADD_BUTTON_DOWN_X = ADD_BUTTON_UP_X + margin * 6;
		ADD_BUTTON_DOWN_Y = 150;

		REMOVE_BUTTON_UP_X = ADD_BUTTON_DOWN_X + margin;
		REMOVE_BUTTON_UP_Y = ADD_BUTTON_UP_Y;
		REMOVE_BUTTON_DOWN_X = REMOVE_BUTTON_UP_X + margin * 6;
		REMOVE_BUTTON_DOWN_Y = ADD_BUTTON_DOWN_Y;

		INFO_BUTTON_UP_X = REMOVE_BUTTON_DOWN_X + margin;
		INFO_BUTTON_UP_Y = ADD_BUTTON_UP_Y;
		INFO_BUTTON_DOWN_X = INFO_BUTTON_UP_X + margin * 6;
		INFO_BUTTON_DOWN_Y = ADD_BUTTON_DOWN_Y;
	}

	/**
	 * Draw the blue region of the graph. Represents ultra-dense nutrition
	 * 
	 * @param canvas
	 *            Canvas to draw on.
	 */
	private void paintBlueRegion(Canvas canvas) {
		float yAxis = toPixelX(0.0f);
		float xAxis = toPixelY(0.0f);
		float x2 = toPixelX(mAxisXUBound);
		float y2 = toPixelY(mAxisYUBound);

		// find it's intersects with bounds

		Path region = new Path();
		region.setFillType(Path.FillType.EVEN_ODD);
		region.moveTo(yAxis, xAxis);
		region.lineTo(x2, xAxis);
		region.lineTo(x2, y2);
		region.lineTo(yAxis, y2);
		region.close();

		mFillPaint.setColor(CHART_COLOR_BLUE);
		canvas.clipPath(region);
		canvas.drawPaint(mFillPaint);
	}

	/**
	 * Draw the green region of the graph. Represents ideal nutrition
	 * 
	 * @param canvas
	 *            Canvas to draw on.
	 */
	private void paintGreenRegion(Canvas canvas) {
		// find points of trapezoid
		float yAxis = toPixelX(0.0f);
		float xAxis = toPixelY(0.0f);
		float x2 = toPixelX(4 * MINFIBER);
		float y2 = toPixelY(4 * MINPROTEIN);

		// draw out trapezoid with a path
		Path region = new Path();
		region.setFillType(Path.FillType.EVEN_ODD);
		region.moveTo(yAxis, xAxis);
		region.lineTo(x2, xAxis);
		region.lineTo(yAxis, y2);
		region.close();

		// fill in trapezoid with a brush
		if (canvas.clipPath(region)) {
			mFillPaint.setColor(CHART_COLOR_GREEN);
			canvas.drawPaint(mFillPaint);
		}
	}

	/**
	 * Draw the yellow region of the graph. Represents moderate nutrition
	 * 
	 * @param canvas
	 *            Canvas to draw on.
	 */
	private void paintYellowRegion(Canvas canvas) {
		// find points of trapezoid
		float yAxis = toPixelX(0.0f);
		float xAxis = toPixelY(0.0f);
		float x2 = toPixelX(2 * MINFIBER);
		float y2 = toPixelY(2 * MINPROTEIN);

		// draw out trapezoid with a path
		Path region = new Path();
		region.setFillType(Path.FillType.EVEN_ODD);
		region.moveTo(yAxis, xAxis);
		region.lineTo(x2, xAxis);
		region.lineTo(yAxis, y2);
		region.close();

		// fill in trapezoid with a brush
		if (canvas.clipPath(region)) {
			mFillPaint.setColor(CHART_COLOR_YELLOW);
			canvas.drawPaint(mFillPaint);
		}
	}

	/**
	 * Draw the red region of the graph. Represents low nutrition
	 * 
	 * @param canvas
	 *            Canvas to draw on.
	 */
	private void paintRedRegion(Canvas canvas) {
		// find points of trapezoid
		float yAxis = toPixelX(0.0f);
		float xAxis = toPixelY(0.0f);
		float x1 = toPixelX(1 * MINFIBER);
		float y1 = toPixelY(1 * MINPROTEIN);

		// draw out trapezoid with a path
		Path region = new Path();
		region.setFillType(Path.FillType.EVEN_ODD);
		region.moveTo(yAxis, xAxis);
		region.lineTo(x1, xAxis);
		region.lineTo(yAxis, y1);
		region.close();

		// fill in trapezoid with a brush
		if (canvas.clipPath(region)) {
			mFillPaint.setColor(CHART_COLOR_RED);
			canvas.drawPaint(mFillPaint);
		}
	}

	/**
	 * Draws the assets needed for the Meal item. Namely the meal graphic,
	 * connecting lines to meal components, and component counts
	 * 
	 * @param canvas
	 *            The canvas to draw on
	 */
	protected void drawMeal(Canvas canvas) {
		if (mMeal == null || mMeal.getFoodItems().size() < 1) {
			return;
		}
		if (mMeal.getFoodItems().size() == 1) {
			drawMealItem(canvas, mMeal.getFoodItems().get(0));
		} else {
			ArrayList<FoodItem> mealFoodItems = mMeal.getFoodItems();
			drawMealLines(canvas, mealFoodItems);
			drawMealItems(canvas, mealFoodItems);
		}
	}

	/**
	 * Draws black lines from the coordinates of the meal to the coordinates
	 * 
	 * @param canvas
	 *            The Canvas to draw on
	 * @param foodItems
	 *            A List of FoodItem's to draw lines to
	 */
	private void drawMealLines(Canvas canvas, List<FoodItem> foodItems) {
		float mealX = mMeal.getX();
		float mealY = mMeal.getY();
		mealX = toPixelX(mealX);
		mealY = toPixelY(mealY);

		float foodX;
		float foodY;

		Path lines = new Path();
		for (int i = 0; i < foodItems.size(); i++) {
			foodX = foodItems.get(i).getX();
			foodY = foodItems.get(i).getY();

			foodX = toPixelX(foodX);
			foodY = toPixelY(foodY);

			lines.moveTo(mealX, mealY);
			lines.lineTo(foodX, foodY);
		}

		mScalePaint.setColor(android.graphics.Color.BLACK);
		canvas.drawPath(lines, mScalePaint);
	}

	/**
	 * Redraws the components of the meal so that they are on top and the meal
	 * itself. Also adds counts to each meal
	 * 
	 * @param canvas
	 *            The Canvas to draw on
	 * @param foodItems
	 *            A List of FoodItem's to redraw
	 */
	private void drawMealItems(Canvas canvas, List<FoodItem> foodItems) {

		for (FoodItem dataPoint : foodItems) {
			drawMealItem(canvas, dataPoint);
		}

		drawMealHighlight(canvas);
		drawMealItem(canvas, mMeal);
	}

	/**
	 * Draws the highlight aura underneath the meal item
	 * 
	 * @param canvas
	 *            The Canvas to draw on
	 */
	private void drawMealHighlight(Canvas canvas) {

		Bitmap mealGraphic = mMeal.getGraphic();

		Bitmap highlight = Bitmap.createScaledBitmap(mHighlight,
				mealGraphic.getWidth() * 2, mealGraphic.getHeight() * 2, false);
		drawGraphic(canvas, highlight, toPixelX(mMeal.getX()),
				toPixelY(mMeal.getY()));
	}

	/**
	 * Draws the individual FoodItem and it's count if it is greater than 0
	 * 
	 * @param canvas
	 * @param dataPoint
	 */
	private void drawMealItem(Canvas canvas, FoodItem dataPoint) {
		Bitmap graphic;
		float x;
		float y;
		graphic = dataPoint.getGraphic();
		x = dataPoint.getX();
		y = dataPoint.getY();
		x = toPixelX(x);
		y = toPixelY(y);
		drawGraphic(canvas, graphic, x, y);

		int count = mMeal.getFoodCount(dataPoint);
		if (count > 0) {
			mTextPaint.setColor(android.graphics.Color.BLACK);
			canvas.drawText("x" + count, x + graphic.getWidth() / 2, y
					+ graphic.getHeight() / 2, mTextPaint);
		}
	}

	/**
	 * Highlight and display proper information about the currently selected
	 * FoodItem on the view
	 * 
	 * For debugging
	 * 
	 * @param canvas
	 */
	protected void drawSelection(Canvas canvas) {
		if (mSelection == null) {
			return;
		}
		Bitmap graphic = mSelection.getGraphic();

		Bitmap highlight = Bitmap.createScaledBitmap(mHighlight,
				graphic.getWidth() * 2, graphic.getHeight() * 2, false);
		drawGraphic(canvas, highlight, toPixelX(mSelection.getX()),
				toPixelY(mSelection.getY()));
		drawGraphic(canvas, graphic, toPixelX(mSelection.getX()),
				toPixelY(mSelection.getY()));

		Path trace = new Path();
		trace.moveTo(getWidth() / 4, mPlotYBegin + 50);
		trace.lineTo(mPlotXEnd - 10, mPlotYBegin + 50);

		// debugging purposes to make sure we're selecting the right foods
		String foodInfo = mSelection.getName() + " " + mSelection.getX() + ", "
				+ mSelection.getY();
		canvas.drawTextOnPath(foodInfo, trace, 0, -6, mTextPaint);

	}

	/**
	 * Draws the buttons on top
	 * 
	 * @param canvas
	 *            Canvas to draw on.
	 */
	protected void drawButtons(Canvas canvas) {
		// saving clipping and matrix transform info, as the region painters
		// modify the clips
		canvas.save();
		setButtonConstants();
		paintAddButton(canvas);
		paintRemoveButton(canvas);
		paintInfoButton(canvas);
		canvas.restore();
	}

	/**
	 * Paint the add to meal button
	 * 
	 * @param canvas
	 *            Canvas to draw on.
	 */
	private void paintAddButton(Canvas canvas) {
		Log.i("MYL", "" + this.ADD_BUTTON_UP_X + "    "
				+ this.ADD_BUTTON_DOWN_X);
		Log.i("Margin", "" + margin);
		Log.i("width", "" + getWidth());
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(3);
		canvas.drawRect(ADD_BUTTON_UP_X, ADD_BUTTON_UP_Y, ADD_BUTTON_DOWN_X,
				ADD_BUTTON_DOWN_Y, paint);
		paint.setStrokeWidth(0);
		paint.setColor(Color.LTGRAY);
		canvas.drawRect(ADD_BUTTON_UP_X + BUTTON_MARGIN, ADD_BUTTON_UP_Y
				+ BUTTON_MARGIN, ADD_BUTTON_DOWN_X - BUTTON_MARGIN,
				ADD_BUTTON_DOWN_Y - BUTTON_MARGIN, paint);

		Path trace = new Path();
		trace.moveTo(margin / 2 + ADD_BUTTON_UP_X,
				(ADD_BUTTON_UP_Y + ADD_BUTTON_DOWN_Y) / 2);
		trace.lineTo((ADD_BUTTON_UP_X + ADD_BUTTON_DOWN_X),
				(ADD_BUTTON_UP_Y + ADD_BUTTON_DOWN_Y) / 2);

		canvas.drawTextOnPath(ADD_BUTTON_LABEL_TEXT, trace, 0, 0, mTextPaint);
	}

	/**
	 * Paint the remove food item from meal button
	 * 
	 * @param canvas
	 *            Canvas to draw on.
	 */
	private void paintRemoveButton(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(3);
		canvas.drawRect(REMOVE_BUTTON_UP_X, REMOVE_BUTTON_UP_Y,
				REMOVE_BUTTON_DOWN_X, REMOVE_BUTTON_DOWN_Y, paint);
		paint.setStrokeWidth(0);
		paint.setColor(Color.LTGRAY);
		canvas.drawRect(REMOVE_BUTTON_UP_X + BUTTON_MARGIN, REMOVE_BUTTON_UP_Y
				+ BUTTON_MARGIN, REMOVE_BUTTON_DOWN_X - BUTTON_MARGIN,
				REMOVE_BUTTON_DOWN_Y - BUTTON_MARGIN, paint);

		Path trace = new Path();
		trace.moveTo(margin / 2 + REMOVE_BUTTON_UP_X,
				(REMOVE_BUTTON_UP_Y + REMOVE_BUTTON_DOWN_Y) / 2);
		trace.lineTo((REMOVE_BUTTON_UP_X + REMOVE_BUTTON_DOWN_X),
				(REMOVE_BUTTON_UP_Y + REMOVE_BUTTON_DOWN_Y) / 2);

		canvas.drawTextOnPath(REMOVE_BUTTON_LABEL_TEXT, trace, 0, 0, mTextPaint);
	}

	/**
	 * Paint the get detailed info button
	 * 
	 * @param canvas
	 *            Canvas to draw on.
	 */
	private void paintInfoButton(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(3);
		canvas.drawRect(INFO_BUTTON_UP_X, INFO_BUTTON_UP_Y, INFO_BUTTON_DOWN_X,
				INFO_BUTTON_DOWN_Y, paint);
		paint.setStrokeWidth(0);
		paint.setColor(Color.LTGRAY);
		canvas.drawRect(INFO_BUTTON_UP_X + BUTTON_MARGIN, INFO_BUTTON_UP_Y
				+ BUTTON_MARGIN, INFO_BUTTON_DOWN_X - BUTTON_MARGIN,
				INFO_BUTTON_DOWN_Y - BUTTON_MARGIN, paint);

		Path trace = new Path();
		trace.moveTo((margin * 6) / 4 + INFO_BUTTON_UP_X,
				(INFO_BUTTON_UP_Y + INFO_BUTTON_DOWN_Y) / 2);
		trace.lineTo((INFO_BUTTON_UP_X + INFO_BUTTON_DOWN_X),
				(INFO_BUTTON_UP_Y + INFO_BUTTON_DOWN_Y) / 2);

		canvas.drawTextOnPath(INFO_BUTTON_LABEL_TEXT, trace, 0, 0, mTextPaint);
	}

	/**
	 * Update the FoodGraphView with a new FoodItem selection
	 * 
	 * @param selection
	 *            The new selection received from the model
	 */
	public void updateSelection(FoodItem selection) {
		mSelection = selection;
		invalidate();
	}

	/**
	 * Update the FoodGraphView with an updated MealItem
	 * 
	 * @param meal
	 *            The newly updated meal
	 */
	public void updateMeal(MealItem meal) {
		mMeal = meal;
		invalidate();
	}
}
