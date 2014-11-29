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
public class Bytes {

	/**
	 * Gets a human-readable string for the given byte length.
	 * 
	 * @param bytes
	 *            the bytes to parse.
	 * @return a human-readable string for the given byte length.
	 */
	public static String readable(long bytes) {
		int unit = 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = "KMGTPE".charAt(exp - 1) + "i";
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
