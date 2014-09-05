/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.util;

import java.util.List;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 **/
public class Random {

	/**
	 * Gets a random number up to the given value.
	 * 
	 * @param max
	 *            the value to generate a random number up to.
	 * @return a random number up to the given value.
	 */
	public static int nextInt(int max) {
		return nextInt(0, max);
	}

	/**
	 * Gets a random number between the two given values.
	 * 
	 * @param min
	 *            the minimum value
	 * @param max
	 *            the maximum value
	 * @return a random number between the two given values.
	 */
	public static int nextInt(int min, int max) {
		if (min > max) {
			int temp = max;
			max = min;
			min = temp;
		}
		int rand = (int) (Math.random() * (max - min + 1));
		return min + rand;
	}

	/**
	 * Gets a random element from the given array.
	 * 
	 * @param array
	 *            the array to get from.
	 * @return a random element from the given array.
	 */
	public static <T> T nextElement(T[] array) {
		return array[nextInt(array.length - 1)];
	}

	/**
	 * Gets a random element from the given list.
	 * 
	 * @param list
	 *            the list to get from.
	 * @return a random element from the given list.
	 */
	public static <T> T nextElement(List<T> list) {
		return list.get(nextInt(list.size() - 1));
	}
}