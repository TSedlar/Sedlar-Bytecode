/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.util.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 **/
public class CountedInputStream extends FilterInputStream {

	private int count = 0;

	public CountedInputStream(InputStream in) {
		super(in);
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
	public int read() throws IOException {
		int b = in.read();
		count++;
		return b;
	}

	@Override
	public int read(byte[] bytes) throws IOException {
		return read(bytes, 0, bytes.length);
	}

	@Override
	public int read(byte[] bytes, int offset, int len) throws IOException {
		int read = in.read(bytes, 0, bytes.length);
		count += read;
		return read;
	}

	@Override
	public long skip(long n) throws IOException {
		long skip = in.skip(n);
		count += (int) skip;
		return skip;
	}

	@Override
	public boolean markSupported() {
		return false;
	}
}