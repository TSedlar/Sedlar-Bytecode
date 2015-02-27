/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.util.collection;

import java.util.Collections;
import java.util.LinkedList;

import me.sedlar.util.Filter;
import me.sedlar.util.Random;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 **/
public class QueryableList<T> extends LinkedList<T> {

	/**
	 * Gets the first element after the given index while matching the given
	 * filter.
	 * 
	 * @param index
	 *            the index to search after.
	 * @param filter
	 *            the filter to match.
	 * @return the first element after the given index while matching the given
	 *         filter.
	 */
	public T firstAfter(int index, Filter<T> filter) {
		for (int i = index + 1; i < size(); i++) {
			T t = get(i);
			if (filter.accept(t))
				return t;
		}
		return null;
	}

	/**
	 * Gets the first element before the given index while matching the given
	 * filter.
	 * 
	 * @param index
	 *            the index to search before.
	 * @param filter
	 *            the filter to match.
	 * @return the first element before the given index while matching the given
	 *         filter.
	 */
	public T firstBefore(int index, Filter<T> filter) {
		for (int i = 0; i < index && i < size(); i++) {
			T t = get(i);
			if (filter.accept(t))
				return t;
		}
		return null;
	}

	/**
	 * Gets the first element matching the given filter.
	 * 
	 * @param filter
	 *            the filter to match.
	 * @return the first element matching the given filter.
	 */
	public T first(Filter<T> filter) {
		return firstAfter(-1, filter);
	}

	/**
	 * Gets all the elements after the given index while matching the given
	 * filter.
	 * 
	 * @param index
	 *            the index to search after.
	 * @param filter
	 *            the filter to match.
	 * @return all the elements after the given index while matching the given
	 *         filter.
	 */
	public QueryableList<T> allAfter(int index, Filter<T> filter) {
		QueryableList<T> list = new QueryableList<>();
		for (int i = index + 1; i < size(); i++) {
			T t = get(i);
            if (filter == null || filter.accept(t))
				list.add(t);
		}
		return list;
	}

	/**
	 * Gets all the elements before the given index while matching the given
	 * filter.
	 * 
	 * @param index
	 *            the index to search before.
	 * @param filter
	 *            the filter to match.
	 * @return all the elements before the given index while matching the given
	 *         filter.
	 */
	public QueryableList<T> allBefore(int index, Filter<T> filter) {
		QueryableList<T> list = new QueryableList<>();
		for (int i = 0; i < index && i < size(); i++) {
			T t = get(i);
			if (filter.accept(t))
				list.add(t);
		}
		return list;
	}

	/**
	 * Gets all the elements matching the given filter.
	 * 
	 * @param filter
	 *            the filter to match.
	 * @return all the elements matching the given filter.
	 */
	public QueryableList<T> all(Filter<T> filter) {
		return allAfter(-1, filter);
	}

	/**
	 * Gets the element nearest to the given index.
	 * 
	 * @param index
	 *            the index to search near.
	 * @param filter
	 *            the filter to match.
	 * @return the element nearest to the given index.
	 */
	public T nearest(int index, Filter<T> filter) {
		for (int i = 0; i < index / 2; i++) {
			if (index - i >= 0) {
				T t = get(index - i);
				if (filter.accept(t))
					return t;
			}
			if (index + i < size()) {
				T t = get(index + i);
				if (filter.accept(t))
					return t;
			}
		}
		return null;
	}

	/**
	 * Shuffles the order of this list.
	 */
	public void shuffle() {
		Collections.shuffle(this);
	}

	/**
	 * Gets a random element in the list.
	 * 
	 * @return a random element in the list.
	 */
	public T random() {
		QueryableList<T> list = new QueryableList<>();
		list.addAll(this);
		list.shuffle();
		return Random.nextElement(list);
	}

	/**
	 * Gets the first element in this list.
	 * 
	 * @return the first element in this list.
	 */
	public T first() {
		return isEmpty() ? null : get(0);
	}

	@Override
	public T getFirst() {
		return first();
	}

	/**
	 * Gets the last element in this list.
	 * 
	 * @return the last element in this list.
	 */
	public T last() {
		return isEmpty() ? null : get(size() - 1);
	}

	@Override
	public T getLast() {
		return last();
	}
}