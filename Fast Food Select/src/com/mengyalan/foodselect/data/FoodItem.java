/**
 * @author ymeng7 Yalan Meng
 */

package com.mengyalan.foodselect.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.FloatMath;

import com.mengyalan.foodselect.utility.FHelper;

/**
 * The food item represent each food entry in the menu It can both be a class
 * holder that contains data information, and a class to respresent a point on
 * the graph.
 */
public class FoodItem implements GraphDataPoint, Comparable<FoodItem> {

	private static int _count = 0;
	private String _id;

	private String name;
	protected int totalCalories;
	protected int fiber, protein, saturatedFat, sodium; // g per serving
	private String type;
	private Bitmap graphIcon;

	public static double MINFAT = 10.0;
	public static double MINSODIUM = 500.0;

	/**
	 * 
	 * @param name
	 *            food name
	 * @param cal
	 *            total calories
	 * @param f
	 *            fiber
	 * @param p
	 *            protein
	 * @param sf
	 *            saturated fat
	 * @param na
	 *            sodium
	 * @param attr
	 *            type
	 */
	public FoodItem(String name, int cal, int f, int p, int sf, int na,
			String attr, Context application) {
		this.setId("" + _count++);
		this.name = name;
		this.totalCalories = cal;
		this.fiber = f;
		this.protein = p;
		this.saturatedFat = sf;
		this.sodium = na;
		this.type = attr;
		drawIcon();
	}

	/**
	 * Constructor used primarily by MealItem to set nutritional values to a
	 * default 0
	 */
	public FoodItem() {
		name = null;
		totalCalories = 0;
		fiber = 0;
		protein = 0;
		saturatedFat = 0;
		sodium = 0;
		type = null;
		drawIcon();
	}

	/**
	 * Draw's FoodItem's graphIcon depending on it calorie, fat, and sodium
	 * content
	 */
	protected void drawIcon() {
		// determine width and height of graphIcon
		float radius = FloatMath.sqrt(totalCalories);
		graphIcon = Bitmap.createBitmap((int) (radius * 2) + 1,
				(int) (radius * 2) + 1, Bitmap.Config.ARGB_8888);
		Canvas iconCanvas = new Canvas(graphIcon);

		// initialize background paint to fill settings
		Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		fillPaint.setAntiAlias(true);

		// initialize scale paint to line settings
		Paint scalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		scalePaint.setStyle(Paint.Style.STROKE);
		scalePaint.setStrokeWidth(2);
		scalePaint.setAntiAlias(true);
		scalePaint.setColor(android.graphics.Color.BLACK);

		drawFatSemicircle(radius, iconCanvas, fillPaint, scalePaint);
		drawSodiumSemicircle(radius, iconCanvas, fillPaint, scalePaint);
	}

	/**
	 * Draw's the semicircle to represent sodium content
	 * 
	 * @param radius
	 *            radius of the semicircle
	 * @param iconCanvas
	 *            the canvas to draw to
	 * @param fillPaint
	 *            the Paint to fill with
	 * @param scalePaint
	 *            the Paint to stroke boundry with
	 * @author mklu2 Michael Lu
	 */
	private void drawSodiumSemicircle(float radius, Canvas iconCanvas,
			Paint fillPaint, Paint scalePaint) {
		Path semiCircle = new Path();
		RectF oval = new RectF();
		float startAngle;
		semiCircle.setFillType(Path.FillType.EVEN_ODD);

		startAngle = getSemicircle(1 + radius, 1, 1 + radius, 2 * radius - 1,
				oval, false);
		semiCircle.addArc(oval, startAngle, 180);
		semiCircle.close();

		double sodiumFactor = getNaPer500() / MINSODIUM;
		if (sodiumFactor < 1.0) {
			fillPaint.setColor(android.graphics.Color.BLUE);
		} else if (sodiumFactor > 2.0) {
			fillPaint.setColor(android.graphics.Color.RED);
		} else {
			fillPaint.setColor(android.graphics.Color.YELLOW);
		}
		iconCanvas.drawPath(semiCircle, fillPaint);
		iconCanvas.drawPath(semiCircle, scalePaint);
	}

	/**
	 * Draw's the semicircle to represent Saturated Fat content
	 * 
	 * @param radius
	 *            radius of the semicircle
	 * @param iconCanvas
	 *            the canvas to draw to
	 * @param fillPaint
	 *            the Paint to fill with
	 * @param scalePaint
	 *            the Paint to stroke boundry with
	 */
	private void drawFatSemicircle(float radius, Canvas iconCanvas,
			Paint fillPaint, Paint scalePaint) {
		Path semiCircle = new Path();
		RectF oval = new RectF();
		float startAngle;
		semiCircle.setFillType(Path.FillType.EVEN_ODD);

		startAngle = getSemicircle(0 + radius, 1, 0 + radius, 2 * radius - 1,
				oval, true);
		semiCircle.addArc(oval, startAngle, 180);
		semiCircle.close();

		double fatFactor = getSFpercent() / MINFAT;
		if (fatFactor < 1.0) {
			fillPaint.setColor(android.graphics.Color.BLUE);
		} else if (fatFactor > 2.0) {
			fillPaint.setColor(android.graphics.Color.RED);
		} else {
			fillPaint.setColor(android.graphics.Color.YELLOW);
		}

		iconCanvas.drawPath(semiCircle, fillPaint);
		iconCanvas.drawPath(semiCircle, scalePaint);
	}

	/**
	 * Returns an oval as a RectF and a start angle float to help with
	 * semi-circle construction
	 * 
	 * @param xStart
	 * @param yStart
	 * @param xEnd
	 * @param yEnd
	 * @param ovalRectOUT
	 * @param direction
	 * @return startAngle
	 */
	private float getSemicircle(float xStart, float yStart, float xEnd,
			float yEnd, RectF ovalRectOUT, boolean direction) {

		float centerX = xStart + ((xEnd - xStart) / 2);
		float centerY = yStart + ((yEnd - yStart) / 2);

		double xLen = (xEnd - xStart);
		double yLen = (yEnd - yStart);
		float radius = (float) (Math.sqrt(xLen * xLen + yLen * yLen) / 2);

		RectF oval = new RectF((float) (centerX - radius),
				(float) (centerY - radius), (float) (centerX + radius),
				(float) (centerY + radius));

		ovalRectOUT.set(oval);

		double radStartAngle = 0;

		if (direction) {
			// right half
			radStartAngle = Math.atan2(yStart - centerY, xStart - centerX);
		} else {
			// left half
			radStartAngle = Math.atan2(yEnd - centerY, xEnd - centerX);
		}
		float startAngle = (float) Math.toDegrees(radStartAngle);

		return startAngle;
	}

	/**
	 * Get the id of the food item
	 * 
	 * @return
	 */
	public String getId() {
		return _id;
	}

	/**
	 * Set the id of the food item
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this._id = id;
	}

	/**
	 * Get the name of the food
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the total calorie of the food
	 * 
	 * @return
	 */
	public int getTotalCalories() {
		return totalCalories;
	}

	/**
	 * Get the fiber content of the food
	 * 
	 * @return
	 */
	public int getFiber() {
		return fiber;
	}

	/**
	 * Get the protein content of the food
	 * 
	 * @return
	 */
	public int getProtein() {
		return protein;
	}

	/**
	 * Get the saturated fat content of the food
	 * 
	 * @return
	 */
	public int getSaturatedFat() {
		return saturatedFat;
	}

	/**
	 * Get the sodium content of the food
	 * 
	 * @return
	 */
	public int getSodium() {
		return sodium;
	}

	/**
	 * Get the fiber content per 500 kcal
	 * 
	 * @return fiber/500
	 */
	public double getFiberPer500() {
		if (this.totalCalories <= 0)
			return 0;

		return FHelper.round(fiber / (double) totalCalories * 500, 2);
	}

	/**
	 * Get the protein content per 500 kcal
	 * 
	 * @return protein/500
	 */
	public double getProteinPer500() {
		if (this.totalCalories <= 0)
			return 0;
		return FHelper.round(protein / (double) totalCalories * 500, 2);
	}

	/**
	 * Get the sodium content per 500 kcal
	 * 
	 * @return na/500
	 */
	public double getNaPer500() {
		if (this.totalCalories <= 0)
			return 0;
		return FHelper.round(sodium / (double) totalCalories * 500, 2);
	}

	/**
	 * Get the saturated fat content per 500 kcal
	 * 
	 * @return sf/500
	 */
	public double getSFpercent() {
		if (this.totalCalories <= 0)
			return 0;

		return FHelper.round((saturatedFat * 9) / ((double) totalCalories)
				* 100, 2);
	}

	/**
	 * Get the type of the food
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Get the x location of the food item
	 * 
	 * @return x
	 */
	@Override
	public float getX() {
		return (float) this.getFiberPer500();
	}

	/**
	 * Get the y location of the food item
	 * 
	 * @return y
	 */
	@Override
	public float getY() {
		return (float) this.getProteinPer500();
	}

	/**
	 * Get the graphics of the food item
	 * 
	 * @return bitmap graphics
	 */
	@Override
	public Bitmap getGraphic() {
		return graphIcon;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int compareTo(FoodItem arg0) {
		return _id.compareTo(arg0.getId());
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof FoodItem) {
			return _id.equals(((FoodItem) arg0).getId());
		}
		return false;
	}
}
