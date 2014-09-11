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
public class AlphaLabel {

	/**
	 * Gets the alpha label for the given number [1 = A, 26 = Z, 27 = AA]
	 *
	 * @param n The number to get the alpha label for
	 * @return the alpha label for the given number
	 */
	public static String get(int n) {
		char[] buf = new char[(int) Math.floor(Math.log(25 * (n + 1)) / Math.log(26))];
		for (int i = buf.length - 1; i >= 0; i--) {
			buf[i] = (char) ('A' + (--n) % 26);
			n /= 26;
		}
		return new String(buf);
	}

	/**
	 * Gets the numeric value of the given label
	 *
	 * @param label the label to get the numeric value for
	 * @return the numeric value of the given label
	 */
	public static int numeric(String label) {
		int result = 0;
		for (int i = label.length() - 1; i >= 0; i--)
			result = result + (label.charAt(i) - 64) * (int) Math.pow(26, label.length() - (i + 1));
		return result;
	}
}
