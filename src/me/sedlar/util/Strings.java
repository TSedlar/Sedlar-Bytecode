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
public class Strings {

	/**
	 * Checks if the given string matches to given value.
	 * 
	 * @param string
	 *            the string to check with a selector [* contains, ^ starts, $
	 *            ends, ~ matches, ! not, i.e (^re, red)]
	 * @param check
	 *            the string to check against.
	 * @return
	 */
	public static boolean matches(String string, String check) {
		char selector = string.charAt(0);
		string = string.substring(1);
		switch (selector) {
			case '*': {
				return check.contains(string);
			}
			case '^': {
				return check.startsWith(string);
			}
			case '$': {
				return check.endsWith(string);
			}
			case '~': {
				return check.matches(string);
			}
			case '!': {
				return !check.equals(string);
			}
			default: {
				return check.equals(string);
			}
		}
	}

	/**
	 * Replaces the last occurance of the given string.
	 * 
	 * @param text
	 *            the original string
	 * @param regex
	 *            the regex to replace
	 * @param replacement
	 *            the string to replace with
	 * @return a string with the last occurance replaced with the given
	 *         replacement.
	 */
	public static String replaceLast(String text, String regex, String replacement) {
		return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
	}
}
