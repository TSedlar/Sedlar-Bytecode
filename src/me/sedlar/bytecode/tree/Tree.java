/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class Tree<E extends Tree<E>> extends CopyOnWriteArrayList<E> {

	protected Tree<E> parent;

	public Tree() {
		super();
	}

	public Tree(Collection<? extends E> collection) {
		super(collection);
	}

	/**
	 * Adds the given element to the first index.
	 * 
	 * @param e
	 *            The element to add.
	 */
	public void addFirst(E e) {
		Collection<E> list = new ArrayList<>();
		for (E element : this)
			list.add(element);
		clear();
		e.parent = this;
		add(e);
		addAll(list);
	}

	/**
	 * Sets the successor for the given predecessor.
	 * 
	 * @param predecessor
	 *            The predecessor
	 * @param successor
	 *            The successor
	 */
	public void set(E predecessor, E successor) {
		Iterator<E> it = parent.iterator();
		Collection<E> es = new LinkedList<>();
		while (it.hasNext()) {
			E e = it.next();
			if (e.equals(predecessor)) {
				es.add(successor);
			} else {
				es.add(e);
			}
		}
		parent.clear();
		parent.addAll(es);
	}

	/**
	 * Gets the parent of this tree.
	 * 
	 * @return the parent of this tree.
	 */
	@SuppressWarnings("unchecked")
	public E parent() {
		return (E) parent;
	}

	/**
	 * Checks if this tree has a parent.
	 * 
	 * @return <t>true</t> if this tree has a parent, otherwise <t>false</t>.
	 */
	public boolean hasParent() {
		return parent() != null;
	}

	/**
	 * Gets the previous element.
	 * 
	 * @return the previous element.
	 */
	public E previous() {
		Tree<E> p = parent;
		if (p == null)
			return null;
		Iterator<E> it = parent.iterator();
		E prev = null;
		while (it.hasNext()) {
			E e = it.next();
			if (e.equals(this)) {
				return prev;
			}
			prev = e;
		}
		return null;
	}

	/**
	 * Checks if the next element is valid.
	 * 
	 * @return <t>true</t> if the next element is valid, otherwise <t>false</t>.
	 */
	public boolean hasPrevious() {
		return previous() != null;
	}

	/**
	 * Gets the next element.
	 * 
	 * @return the next element.
	 */
	public E next() {
		Tree<E> p = parent;
		if (p == null)
			return null;
		Iterator<E> it = parent.iterator();
		while (it.hasNext()) {
			E e = it.next();
			if (e.equals(this))
				return it.hasNext() ? it.next() : null;
		}
		return null;
	}

	/**
	 * Checks if the next element is valid.
	 * 
	 * @return <t>true</t> if the next element is valid, otherwise <t>false</t>.
	 */
	public boolean hasNext() {
		return next() != null;
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		for (E e : this)
			hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
		return hashCode;
	}
}