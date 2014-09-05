/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */

package me.sedlar.bytecode.io;

import java.io.DataInput;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public interface BytecodeInput extends DataInput {

	/**
	 * Get the number of bits read.
	 *
	 * @return the number of bits
	 */
	public int count();
}
