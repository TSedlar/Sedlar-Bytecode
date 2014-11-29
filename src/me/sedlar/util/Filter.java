/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.util;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 **/
public interface Filter<T> {

	/**
	 * Checks if the given element is acceptable.
	 * 
	 * @param t
	 *            the element to check against.
	 * @return <t>true</t> if the given element is acceptable, otherwise
	 *         <t>false</t>.
	 */
	public boolean accept(T t);
}