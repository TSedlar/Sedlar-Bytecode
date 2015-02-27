/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class OffsetPair {

	private int match;
	private int offset;

	/**
	 * Constructor.
	 *
	 * @param match
	 *            the match variable.
	 * @param offset
	 *            the branch offset.
	 */
	public OffsetPair(int match, int offset) {
		this.match = match;
		this.offset = offset;
	}

	/**
	 * Get the match variable of this match-offset pair.
	 *
	 * @return the variable
	 */
	public int match() {
		return match;
	}

	/**
	 * Set the match variable of this match-offset pair.
	 *
	 * @param match
	 *            the variable
	 */
	public void setMatch(int match) {
		this.match = match;
	}

	/**
	 * Get the offset of the branch for this match-offset pair.
	 *
	 * @return the offset
	 */
	public int offset() {
		return offset;
	}

	/**
	 * Set the offset of the branch for this match-offset pair.
	 *
	 * @param offset
	 *            the offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
}
