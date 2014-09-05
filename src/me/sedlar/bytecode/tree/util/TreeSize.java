/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.tree.util;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class TreeSize {

	public final int consuming, producing;

	public TreeSize(int consuming, int producing) {
		this.consuming = consuming;
		this.producing = producing;
	}

	@Override
	public String toString() {
		return "[" + consuming + "][" + producing + "]";
	}
}