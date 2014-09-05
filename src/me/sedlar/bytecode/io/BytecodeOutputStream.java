/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */

package me.sedlar.bytecode.io;

import java.io.DataOutputStream;
import java.io.OutputStream;

import me.sedlar.util.io.CountedOutputStream;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class BytecodeOutputStream extends DataOutputStream implements BytecodeOutput {

	/**
	 * @param out
	 *            the output stream.
	 */
	public BytecodeOutputStream(OutputStream out) {
		super(new CountedOutputStream(out));
	}

	@Override
	public int count() {
		return ((CountedOutputStream) out).count();
	}
}
