/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class ConstantLongInfo extends ConstantLargeNumeric {

	@Override
	public byte tag() {
		return CONSTANT_LONG;
	}

	@Override
	public String verboseTag() {
		return CONSTANT_LONG_VERBOSE;
	}

	@Override
	public String verbose() throws InvalidByteCodeException {
		return String.valueOf(value());
	}

	/**
	 * Get the long variable of this constant pool entry.
	 *
	 * @return the variable
	 */
	public long value() {
		return ((long) highBytes << 32) | ((long) lowBytes & 0xFFFFFFFF);
	}

	/**
	 * Set the long variable of this constant pool entry.
	 *
	 * @param number
	 *            the variable
	 */
	public void setValue(long number) {
		highBytes = (int) (number >>> 32);
		lowBytes = (int) (number & 0xFFFFFFFF);
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeByte(CONSTANT_LONG);
		super.write(out);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + verboseTag() + " with high_bytes " + highBytes + " and low_bytes " + lowBytes);
	}
}
