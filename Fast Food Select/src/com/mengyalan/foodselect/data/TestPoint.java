/**
 * @author mklu2 Michael Lu
 */

package com.mengyalan.foodselect.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mengyalan.foodselect.R;

public class TestPoint implements GraphDataPoint {
	private float mXCoord;
	private float mYCoord;
	private Context mContext;
	private Bitmap mGraphic;

	public TestPoint(float x, float y, Context application) {
		mXCoord = x;
		mYCoord = y;
		mContext = application;
		mGraphic = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.testflag);
	}

	@Override
	public float getX() {
		return mXCoord;
	}

	@Override
	public float getY() {
		return mYCoord;
	}

	@Override
	public Bitmap getGraphic() {
		return mGraphic;
	}

}
