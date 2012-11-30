/**
 * @author ymeng7 Yalan Meng
 * Helper functions for our project
 */
package com.mengyalan.foodselect.utility;

public class FHelper {

	public static double round(double val, int numberOfDigitsAfterDecimal) {
		double p = (float) Math.pow(10, numberOfDigitsAfterDecimal);
		val = val * p;
		double tmp = Math.floor(val);
		return (double) tmp / p;
	}

}
