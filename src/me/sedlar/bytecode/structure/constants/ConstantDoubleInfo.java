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
public class ConstantDoubleInfo extends ConstantLargeNumeric {

	@Override
	public byte tag() {
		return CONSTANT_DOUBLE;
	}

	@Override
	public String verboseTag() {
		return CONSTANT_DOUBLE_VERBOSE;
	}

	@Override
	public String verbose() throws InvalidByteCodeException {
		return String.valueOf(value());
	}

	/**
	 * Get the double variable of this constant pool entry.
	 *
	 * @return the variable
	 */
	public double value() {
		long longBits = (long) highBytes << 32 | (long) lowBytes & 0xFFFFFFFFL;
		return Double.longBitsToDouble(longBits);
	}

	/**
	 * Set the double variable of this constant pool entry.
	 *
	 * @param number
	 *            the variable
	 */
	public void setValue(double number) {
		long longBits = Double.doubleToLongBits(number);
		highBytes = (int) (longBits >>> 32 & 0xFFFFFFFFL);
		lowBytes = (int) (longBits & 0xFFFFFFFFL);
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeByte(CONSTANT_DOUBLE);
		super.write(out);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + verboseTag() + " with high_bytes " + highBytes + " and low_bytes " + lowBytes);
	}
}
