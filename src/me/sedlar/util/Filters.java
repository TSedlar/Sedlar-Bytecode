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
public class Filters {

	/**
	 * Constructs a filter that will match any of the given filters.
	 *
	 * @param filters
	 *            The filters to check matching against.
	 * @param <T>
	 *            The type of filter.
	 * @return A filter that will match any of the given filters.
	 */
	@SafeVarargs
	public static <T> Filter<T> or(final Filter<T>... filters) {
		return new Filter<T>() {
			public boolean accept(T t) {
				for (Filter<T> filter : filters) {
					if (filter.accept(t))
						return true;
				}
				return false;
			}
		};
	}

	/**
	 * Constructs a filter that must match all of the given filters.
	 *
	 * @param filters
	 *            The filters to check matching against.
	 * @param <T>
	 *            The type of filter.
	 * @return A filter that must match all of the given filters.
	 */
	@SafeVarargs
	public static <T> Filter<T> and(final Filter<T>... filters) {
		return new Filter<T>() {
			public boolean accept(T t) {
				for (Filter<T> filter : filters) {
					if (!filter.accept(t))
						return false;
				}
				return true;
			}
		};
	}
}
