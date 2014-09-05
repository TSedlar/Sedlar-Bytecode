/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.util.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 **/
public class CountedOutputStream extends FilterOutputStream {

	private int count = 0;

	public CountedOutputStream(OutputStream out) {
		super(out);
	}

	/**
	 * Gets the amount read.
	 * 
	 * @return the amount read.
	 */
	public int count() {
		return count;
	}

	@Override
	public void write(int b) throws IOException {
		out.write(b);
		count++;
	}
}