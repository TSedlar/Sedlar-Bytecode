/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */

package me.sedlar.bytecode.io;

import java.io.DataInputStream;
import java.io.InputStream;

import me.sedlar.util.io.CountedInputStream;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class BytecodeInputStream extends DataInputStream implements BytecodeInput {

	/**
	 * @param in
	 *            the input stream.
	 */
	public BytecodeInputStream(InputStream in) {
		super(new CountedInputStream(in));
	}

	@Override
	public int count() {
		return ((CountedInputStream) in).count();
	}
}
