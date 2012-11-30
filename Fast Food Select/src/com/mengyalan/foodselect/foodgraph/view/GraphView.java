/**
 * @author mklu2 Michael Lu
 */

package com.mengyalan.foodselect.foodgraph.view;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.mengyalan.foodselect.data.GraphDataPoint;
import com.mengyalan.foodselect.foodgraph.controller.GraphController;

@TargetApi(8)
public class GraphView extends View {

	// Set of datapoints to plot
	private ArrayList<GraphDataPoint> mDataPoints = new ArrayList<GraphDataPoint>();

	private GraphController mController = null;

	// Values represent the view's plot area through pixel bounds. Begin =< End
	protected int mPlotXBegin = -1;
	protected int mPlotYBegin = -1;
	protected int mPlotXEnd = -1;
	protected int mPlotYEnd = -1;

	// Values represent the Graph's domain and range. LBound <= UBound
	protected float mAxisXLBound = -1;
	protected float mAxisYLBound = -1;
	protected float mAxisXUBound = -1;
	protected float mAxisYUBound = -1;

	// Values represent the range of the axis at default scale values
	protected float mAxisXDSpan = -1;
	protected float mAxisYDSpan = -1;
	protected float mAxisScaleFactor = 1;

	// Values represent the minimum or maximum values the axis can shift to
	protected float mAxisXMin = -1;
	protected float mAxisXMax = -1;
	protected float mAxisYMin = -1;
	protected float mAxisYMax = -1;

	// Values represent the positions of the tickmarks on the axis
	float[] mAxisXTicks = null;
	float[] mAxisYTicks = null;

	// Paints available to use when drawing
	protected Paint mFillPaint = null;
	protected Paint mScalePaint = null;
	protected Paint mTextPaint = null;

	// Touch variables and objects to keep track of finger position information
	// over time
	private float mFirstTouchX = -1;
	private float mFirstTouchY = -1;
	private float mLastTouchX = -1;
	private float mLastTouchY = -1;
	private boolean mTouchMoved = false;

	long mDownTime = -1;

	private int mActivePointerId = -1;
	private ScaleGestureDetector mScaleDetector;

	// ScaleListener that appropriately changes the scales when a pinch motion
	// is detected
	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {

		@Override
		public boolean onScale(ScaleGestureDetector detector) {

			float focalX = detector.getFocusX();
			float focalY = detector.getFocusY();

			if (!isInPlot(focalX, focalY)) {
				return true;
			}
			mAxisScaleFactor *= detector.getScaleFactor();

			// Don't let the object get too small or too large.
			mAxisScaleFactor = Math
					.max(0.75f, Math.min(mAxisScaleFactor, 5.0f));

			float scaleXSpan = mAxisXDSpan / mAxisScaleFactor;
			float scaleYSpan = mAxisYDSpan / mAxisScaleFactor;

			focalX = toGraphX(focalX);
			focalY = toGraphY(focalY);

			float ratioX = (focalX - mAxisXLBound)
					/ (mAxisXUBound - mAxisXLBound);
			float ratioY = (focalY - mAxisYLBound)
					/ (mAxisYUBound - mAxisYLBound);

			// determine new lower bounds after scaling
			float scaleXBoundNew = focalX - ratioX * scaleXSpan;
			float scaleYBoundNew = focalY - ratioY * scaleYSpan;

			mAxisXLBound = Math.max(mAxisXMin, scaleXBoundNew);
			mAxisYLBound = Math.max(mAxisYMin, scaleYBoundNew);

			scaleXBoundNew = mAxisXLBound + scaleXSpan;
			scaleYBoundNew = mAxisYLBound + scaleYSpan;

			mAxisXUBound = Math.min(mAxisXMax, scaleXBoundNew);
			mAxisYUBound = Math.min(mAxisYMax, scaleYBoundNew);

			mAxisXLBound = mAxisXUBound - scaleXSpan;
			mAxisYLBound = mAxisYUBound - scaleYSpan;

			invalidate();
			return true;
		}
	}

	// *********************************************//
	// // CLASS METHODS BEGIN STARTING HERE ////
	// *********************************************//

	public GraphView(Context context, AttributeSet aSet) {
		super(context, aSet);

		// initialize background paint to fill settings
		mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mFillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mFillPaint.setAntiAlias(true);

		// initialize scale paint to line settings
		mScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mScalePaint.setStyle(Paint.Style.STROKE);
		mScalePaint.setStrokeWidth(2);
		mScalePaint.setAntiAlias(true);

		// initialize text paint to line settings
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mTextPaint.setTextSize(24);
		mTextPaint.setAntiAlias(true);

		// initialize Scale Motion Detection
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

		// initialize Controller
		setController(new GraphController(this));
	}

	/**
	 * The touch listener for graphview. The primary purpose of the listener is
	 * to detect a Scale action or a Scroll action. Scales are handled by
	 * mScaleDetector. Scroll's are tracked by following the finger with each
	 * successive move action and transforming the graph an appropriate distance
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		// Let the ScaleGestureDetector inspect all events.
		// Method that performs the scaling is onScale() in ScaleListner class.
		mScaleDetector.onTouchEvent(ev);

		final int action = ev.getAction();
		switch (action & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN: {
			onActionDown(ev);
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			onActionMove(ev);
			break;
		}
		case MotionEvent.ACTION_UP: {
			onActionUp(ev);
			mActivePointerId = -1;
			break;
		}
		case MotionEvent.ACTION_CANCEL: {
			mActivePointerId = -1;
			break;
		}
		case MotionEvent.ACTION_POINTER_UP: {
			int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			int pointerId = ev.getPointerId(pointerIndex);
			if (pointerId == mActivePointerId) {
				// This was our active pointer going up. Choose a new
				// active pointer and adjust accordingly.
				final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
				mLastTouchX = ev.getX(newPointerIndex);
				mLastTouchY = ev.getY(newPointerIndex);
				mActivePointerId = ev.getPointerId(newPointerIndex);
			}
			break;
		}
		}

		return true;
	}

	/**
	 * Record mLastTouch members when touch screen is first pressed down on.
	 * 
	 * @param ev
	 */
	protected void onActionDown(MotionEvent ev) {
		// initialize LastTouch coordinates and fetch cursor id.
		mFirstTouchX = ev.getX();
		mFirstTouchY = ev.getY();
		mLastTouchX = ev.getX();
		mLastTouchY = ev.getY();
		mTouchMoved = false;
		mDownTime = System.currentTimeMillis();
		mActivePointerId = ev.getPointerId(0);
	}

	/**
	 * Monitor touch movement across screen. If appropriate, pan the screen
	 * 
	 * @param ev
	 */
	protected void onActionMove(MotionEvent ev) {
		int pointerIndex = ev.findPointerIndex(mActivePointerId);
		float x = ev.getX(pointerIndex);
		float y = ev.getY(pointerIndex);

		// Only move if the ScaleGestureDetector isn't processing a gesture.
		if (!mScaleDetector.isInProgress()) {
			// find distance the cursor has moved in graph coordinates
			if (isInPlot(x, y)) {
				float dx = toGraphX(mLastTouchX) - toGraphX(x);
				float dy = toGraphY(mLastTouchY) - toGraphY(y);

				translateGraph(dx, dy);

				invalidate();
			}
		} else {
			mTouchMoved = true;
		}

		// Set mTouchMoved = true if cursor has moved a significant distance
		// from start
		mLastTouchX = x;
		mLastTouchY = y;
		if (!mTouchMoved && Math.abs(mFirstTouchX - mLastTouchX) > 2
				&& Math.abs(mFirstTouchY - mLastTouchY) > 2) {
			mTouchMoved = true;
		}
	}

	private boolean isInPlot(float x, float y) {
		return x > mPlotXBegin && x < mPlotXEnd && y > mPlotYBegin
				&& y < mPlotYEnd;
	}

	/**
	 * Shifts the graph's scales by dx and dy if there is room to shift
	 * 
	 * @param dx
	 * @param dy
	 */
	private void translateGraph(float dx, float dy) {
		if (mAxisXLBound + dx >= mAxisXMin && mAxisXUBound + dx <= mAxisXMax) {
			mAxisXLBound += dx;
			mAxisXUBound += dx;
		}

		if (mAxisYLBound + dy >= mAxisYMin && mAxisYUBound + dy <= mAxisYMax) {
			mAxisYLBound += dy;
			mAxisYUBound += dy;
		}
	}

	/**
	 * When touch is lifted, if no movement was sinced (simple press), run
	 * mController's onActionPress
	 * 
	 * @param ev
	 */
	protected void onActionUp(MotionEvent ev) {
		int pointerIndex = ev.findPointerIndex(mActivePointerId);
		mLastTouchX = ev.getX(pointerIndex);
		mLastTouchY = ev.getY(pointerIndex);

		if (!mTouchMoved) {
			// If the cursor has not moved from start ie. the user simply
			// touched the screen
			
			long totalTime = System.currentTimeMillis() - mDownTime;
			if(totalTime < 750) {
				//If shortpress
				mController.onActionPress(toGraphX(mLastTouchX),
					toGraphY(mLastTouchY));
			}
			else {
				//If longpress
				mController.onActionLongPress(toGraphX(mLastTouchX),
					toGraphY(mLastTouchY));
			}
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		if ((mPlotXEnd < 1) || (mPlotYEnd < 1)) {
			initializePlot();
		}
		plotAllDataPoints(canvas);
		drawScales(canvas);
	}

	/**
	 * Initializes all the data of the graph's bounds
	 */
	protected void initializePlot() {
		// initialize scale positions
		mPlotXBegin = 0 + 50;
		mPlotXEnd = getWidth() - 5;
		mPlotYBegin = 0 + 5;
		mPlotYEnd = getHeight() - 50;

		mAxisXLBound = 0.0f;
		mAxisYLBound = 0.0f;
		mAxisXUBound = 10.0f;
		mAxisYUBound = 10.0f;

		mAxisXDSpan = mAxisXUBound - mAxisXLBound;
		mAxisYDSpan = mAxisYUBound - mAxisYLBound;
		mAxisScaleFactor = 1;

		mAxisXMin = 0.0f;
		mAxisYMin = 0.0f;
		mAxisXMax = 20.0f;
		mAxisYMax = 20.0f;

	}

	/**
	 * Plots the graphics of all the GraphDataPoint objects stored in the
	 * GraphView.
	 * 
	 * @param canvas
	 *            Canvas to draw on.
	 */
	protected void plotAllDataPoints(Canvas canvas) {
		Bitmap graphic;
		float x;
		float y;
		for (GraphDataPoint dataPoint : mDataPoints) {
			graphic = dataPoint.getGraphic();
			x = dataPoint.getX();
			y = dataPoint.getY();
			drawGraphic(canvas, graphic, toPixelX(x), toPixelY(y));
		}
	}

	/**
	 * Draws the Bitmap graphic at the (x,y) pixel coordinates.
	 * 
	 * @param canvas
	 *            Canvas to drawn on.
	 * @param graphic
	 *            Bitmap to draw.
	 * @param x
	 *            X coordinate to draw at.
	 * @param y
	 *            y coordinate to draw at.
	 */
	protected void drawGraphic(Canvas canvas, Bitmap graphic, float x, float y) {
		if (x > 0 && x < getWidth() && y > 0 && y < getHeight()) {
			canvas.drawBitmap(graphic, x - graphic.getWidth() / 2,
					y - graphic.getHeight() / 2, null);
		}
	}

	/**
	 * Draws out the scales on the sides of the graphs, indicating the values of
	 * the GraphDataPoints to the user
	 * 
	 * @param canvas
	 *            Canvas to draw on.
	 */
	protected void drawScales(Canvas canvas) {
		if (mAxisXTicks == null || mAxisYTicks == null) {
			initializeScale();
		}

		// mask region scale lies on
		mFillPaint.setColor(android.graphics.Color.WHITE);

		Path region = new Path();
		region.setFillType(Path.FillType.EVEN_ODD);

		region.moveTo(0, 0);
		region.lineTo(mPlotXBegin, 0);
		region.lineTo(mPlotXBegin, mPlotYEnd);
		region.lineTo(getWidth(), mPlotYEnd);
		region.lineTo(getWidth(), getHeight());
		region.lineTo(0, getHeight());
		region.lineTo(0, 0);
		region.close();

		region.moveTo(mPlotXBegin, 0);
		region.lineTo(getWidth(), 0);
		region.lineTo(getWidth(), mPlotYEnd);
		region.lineTo(mPlotXEnd, mPlotYEnd);
		region.lineTo(mPlotXEnd, mPlotYBegin);
		region.lineTo(mPlotXBegin, mPlotYBegin);
		region.lineTo(mPlotXBegin, 0);
		region.close();

		canvas.drawPath(region, mFillPaint);
		drawAxis(canvas);
	}

	/**
	 * Initializes the values of the tickmarks
	 */
	protected void initializeScale() {

		// initialize the tick mark positions on graph load
		int numXTicks = (mPlotXEnd - mPlotXBegin) / 100;
		int numYTicks = (mPlotYEnd - mPlotYBegin) / 100;
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
	 * Draws the major components of the Axis, it's boundries, tickmarks, and
	 * values
	 * 
	 * @param canvas
	 *            The Canvas to draw on
	 */
	private void drawAxis(Canvas canvas) {
		// Draw axis lines and tickmarks
		mScalePaint.setColor(android.graphics.Color.BLACK);
		Path trace = new Path();

		trace.moveTo(mPlotXBegin, mPlotYBegin);
		trace.lineTo(mPlotXBegin, mPlotYEnd);
		trace.lineTo(mPlotXEnd, mPlotYEnd);

		for (int i = 0; i < mAxisXTicks.length; i++) {
			trace.moveTo(mAxisXTicks[i], mPlotYEnd);
			trace.lineTo(mAxisXTicks[i], mPlotYEnd + 10);
		}

		for (int i = 0; i < mAxisYTicks.length; i++) {
			trace.moveTo(mPlotXBegin, mAxisYTicks[i]);
			trace.lineTo(mPlotXBegin - 10, mAxisYTicks[i]);
		}

		canvas.drawPath(trace, mScalePaint);
		trace.reset();

		// Draw tick mark values
		mTextPaint.setColor(android.graphics.Color.BLACK);
		DecimalFormat axisFormat = new DecimalFormat("#.#");

		for (int i = 0; i < mAxisXTicks.length; i++) {
			trace.moveTo(mAxisXTicks[i], mPlotYEnd + 10);
			trace.lineTo(mAxisXTicks[i] + 40, mPlotYEnd + 50);

			float axisValue = toGraphX(mAxisXTicks[i]);
			canvas.drawTextOnPath(axisFormat.format(axisValue), trace, 5, 0,
					mTextPaint);
			trace.reset();
		}

		for (int i = 0; i < mAxisYTicks.length; i++) {
			trace.moveTo(mPlotXBegin - 50, mAxisYTicks[i] - 40);
			trace.lineTo(mPlotXBegin - 10, mAxisYTicks[i]);

			float axisValue = toGraphY(mAxisYTicks[i]);
			canvas.drawTextOnPath(axisFormat.format(axisValue), trace, 0, 0,
					mTextPaint);
			trace.reset();
		}
	}

	/**
	 * Model data has changed in a way that requires the view to update.
	 * 
	 * @param list
	 */
	public void update(ArrayList<GraphDataPoint> list) {
		mDataPoints = list;
		invalidate();
	}

	/**
	 * Accepts a pixel's x-coordinate and returns the value it represents on the
	 * plot.
	 * 
	 * @param x
	 *            The pixel's x-coordinate
	 * @return The x-coordinate of the point on the graph.
	 */
	public float toGraphX(float x) {
		// if ((x < mPlotXBegin) || (x > mPlotXEnd)) {
		// return -1.0f;
		// }
		float ratio = (x - mPlotXBegin) / ((float) (mPlotXEnd - mPlotXBegin));
		float graphX = mAxisXLBound + ratio * (mAxisXUBound - mAxisXLBound);
		return graphX;
	}

	/**
	 * Accepts a pixel's y-coordinate and returns the value it represents on the
	 * plot.
	 * 
	 * @param y
	 *            The pixel's y-coordinate
	 * @return The y-coordinate of the point on the graph.
	 */
	public float toGraphY(float y) {
		// if ((y < mPlotYBegin) || (y > mPlotYEnd)) {
		// return -1.0f;
		// }
		float ratio = (mPlotYEnd - y) / ((float) (mPlotYEnd - mPlotYBegin));
		float graphY = mAxisYLBound + ratio * (mAxisYUBound - mAxisYLBound);
		return graphY;
	}

	/**
	 * Accepts a graph-points x-coordinate and returns the x-coordinate of the
	 * corresponding pixel.
	 * 
	 * @param x
	 *            The graph point's x-coordinate
	 * @return The x-coordinate of the pixel.
	 */
	public float toPixelX(float x) {
		// if ((x < mScaleXLBound) || (x > mScaleXUBound)) {
		// return -1;
		// }
		float ratio = (x - mAxisXLBound) / (mAxisXUBound - mAxisXLBound);
		float pixelX = mPlotXBegin + ratio * (mPlotXEnd - mPlotXBegin);
		return (int) pixelX;
	}

	/**
	 * Accepts a graph-points y-coordinate and returns the y-coordinate of the
	 * cooresponding pixel.
	 * 
	 * @param y
	 *            The graph point's y-coordinate
	 * @return The y-coordinate of the pixel.
	 */
	public float toPixelY(float y) {
		// if ((y < mScaleYLBound) || (y > mScaleYUBound)) {
		// return -1;
		// }
		float ratio = (y - mAxisYLBound) / (mAxisYUBound - mAxisYLBound);
		float pixelY = mPlotYEnd - ratio * (mPlotYEnd - mPlotYBegin);
		return (int) pixelY;
	}

	/**
	 * Get the GraphController the GraphView accesses
	 * 
	 * @return
	 */
	public GraphController getController() {
		return mController;
	}

	/**
	 * Set the GraphController the GraphView accesses
	 * 
	 * @param controller
	 */
	public void setController(GraphController controller) {
		mController = controller;
	}
}
