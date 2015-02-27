/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.util;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class MutableNumber extends Number {

	private int intValue;
	private long longValue;
	private float floatValue;
	private double doubleValue;

	public MutableNumber(Number number) {
		this.intValue = number.intValue();
		this.longValue = number.longValue();
		this.floatValue = number.floatValue();
		this.doubleValue = number.doubleValue();
	}

	@Override
	public int intValue() {
		return intValue;
	}

	/**
	 * Sets the int value for this number.
	 * 
	 * @param intValue
	 *            the int value to set.
	 */
	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	@Override
	public long longValue() {
		return longValue;
	}

	/**
	 * Sets the long value for this number.
	 * 
	 * @param longValue
	 *            the long value to set.
	 */
	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}

	@Override
	public float floatValue() {
		return floatValue;
	}

	/**
	 * Sets the float value for this number.
	 * 
	 * @param floatValue
	 *            the float value to set.
	 */
	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
	}

	@Override
	public double doubleValue() {
		return doubleValue;
	}

	/**
	 * Sets the double value for this number.
	 * 
	 * @param doubleValue
	 *            the double value to set.
	 */
	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}

	/**
	 * Checks if the int value is positive.
	 * 
	 * @return <t>true</t> if the int value is positive.
	 */
	public boolean isIntPositive() {
		return intValue() >= 0;
	}

	/**
	 * Checks if the long value is positive.
	 * 
	 * @return <t>true</t> if the long value is positive.
	 */
	public boolean isLongPositive() {
		return longValue() >= 0L;
	}

	/**
	 * Checks if the float value is positive.
	 * 
	 * @return <t>true</t> if the float value is positive.
	 */
	public boolean isFloatPositive() {
		return floatValue() >= 0F;
	}

	/**
	 * Checks if the double value is positive.
	 * 
	 * @return <t>true</t> if the double vlaue is positive.
	 */
	public boolean isDoublePositive() {
		return doubleValue() >= 0D;
	}
}
